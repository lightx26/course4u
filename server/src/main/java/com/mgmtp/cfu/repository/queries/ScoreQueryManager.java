package com.mgmtp.cfu.repository.queries;

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO;
import com.mgmtp.cfu.dto.userdto.ScorePerYearDTO;
import com.mgmtp.cfu.dto.userdto.UserScore;
import com.mgmtp.cfu.entity.Registration;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScoreQueryManager {
    private final EntityManager entityManager;


    // Method to retrieve leaderboard users for a specific year and top ranking
    public List<LeaderboardUserDTO> getLeaderboardUsers(int year, int top) {
        String jpql = "SELECT new com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO(u.id, " +
                "COALESCE(SUM(r.re_score), 0), " +
                "u.avatarUrl, u.email, u.username, u.fullName) " +
                "FROM User u LEFT JOIN " +
                "(SELECT re.id as re_id, re.score as re_score, " +
                "re.startDate as re_startDate, re.endDate as re_endDate, re.user.id as re_user_id " +
                "FROM Registration re WHERE EXTRACT(YEAR FROM re.endDate) = :year AND re.status IN :acceptStatus) r " +
                "ON u.id = r.re_user_id " +
                "WHERE u.role = 'USER' " +
                "GROUP BY u.id, u.avatarUrl, u.email, u.username, u.fullName " +
                "HAVING COALESCE(SUM(r.re_score), 0) > 0 " +
                "ORDER BY COALESCE(SUM(r.re_score), 0) DESC "
                ;


        TypedQuery<LeaderboardUserDTO> query = entityManager.createQuery(jpql, LeaderboardUserDTO.class);
        query.setParameter("year", year);
        query.setParameter("acceptStatus", RegistrationStatusUtil.ACCEPTED_STATUSES);
        query.setMaxResults(top);

        try {
            return query.getResultList().stream().peek(leaderboardUserDTO -> {
                log.info("Leaderboard entry: " + leaderboardUserDTO.toString());
                List<List<Integer>> learningTime = initializeLearningTime();
                getRegistrationByUserIdAndYear(leaderboardUserDTO.getUserId(), String.valueOf(year)).forEach(registration -> {
                    try {
                        updateLearningTime(learningTime, registration.getStartDate(), registration.getEndDate(), year);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
                leaderboardUserDTO.setLearningTime(
                        learningTime.stream().map(integers -> integers.get(1)).mapToInt(Integer::intValue).sum()
                );
            }).toList();

        } catch (Exception e) {
            log.error("Error fetching leaderboard users: ", e);
            return new ArrayList<>();
        }
    }

    // Method to initialize learning time
    private List<List<Integer>> initializeLearningTime() {
        List<List<Integer>> learningTime = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            learningTime.add(Arrays.asList(32, 0));
        }
        return learningTime;
    }

    // Method to get existing years
    public Set<Integer> getExistedYears() {
        String jpql = "SELECT DISTINCT YEAR(r.endDate) FROM Registration r " +
                "WHERE r.endDate IS NOT NULL ORDER BY YEAR(r.endDate) ASC";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        return new HashSet<>(query.getResultList());
    }

    // Method to get score statistics for a user by year
    public List<ScorePerYearDTO> getMyScoreStatistics(Long id) {
        try {
            String jpql = "SELECT DISTINCT YEAR(r.endDate) FROM Registration r " +
                    "WHERE r.endDate IS NOT NULL AND r.user.id = :userId ORDER BY YEAR(r.endDate) ASC";
            TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
            query.setParameter("userId", id);
            List<Integer> years = query.getResultList();
            List<ScorePerYearDTO> myScorePerYear = new ArrayList<>();
            var currentYear = ZonedDateTime.now().getYear();
            var currentMonth = ZonedDateTime.now().getMonthValue();
            for (int year : years) {
                List<Object> scoresAndLearningTime = getScoreAndLearningTimePerMonth(String.valueOf(year), id);
                myScorePerYear.add(new ScorePerYearDTO(year,
                        ((List<?>) scoresAndLearningTime.get(1)).stream().map(day -> Long.parseLong(day != null ? day.toString() : "0"))
                                .mapToLong(Long::longValue).sum(),
                        Long.parseLong(String.valueOf(
                                        (
                                                (currentYear != year ? ((List<Integer>) scoresAndLearningTime.get(0)).get(11) : ((List<Integer>) scoresAndLearningTime.get(0)).get(currentMonth - 1))
                                        )
                                )
                        )));
            }
            return myScorePerYear;
        } catch (Exception e) {
            log.error("Error fetching score statistics: ", e);
            return new ArrayList<>();
        }
    }

    // Method to get user score for a specific year
    public UserScore getMyScore(String year, Long id) {
        try {
            List<Object[]> listOfUserScores = getListOfUserScore(year, id);
            List<Object> dataPerMonth = getScoreAndLearningTimePerMonth(year, id);
            for (int i = 0; i < listOfUserScores.size(); i++) {
                Object[] userScore = listOfUserScores.get(i);
                Long userId = (Long) userScore[0];
                if (userId.equals(id)) {
                    return UserScore.builder()
                            .score((Long) userScore[1])
                            .rank(i + 1)
                            .year(year)
                            .months((List<String>) dataPerMonth.get(2))
                            .scores((List<Integer>) dataPerMonth.get(0))
                            .learningTime((List<Integer>) dataPerMonth.get(1))
                            .build();
                }
            }
            return UserScore.builder().year(year).build();
        } catch (Exception e) {
            log.error("Error fetching user score: ", e);
            return null;
        }
    }

    // Helper method to get score and learning time per month
    private List<Object> getScoreAndLearningTimePerMonth(String year, Long id) {
        List<Registration> registrations = getRegistrationByUserIdAndYear(id, year);
        List<Integer> scores = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> learningTimeResult = new ArrayList<>();
        List<List<Integer>> learningTime = initializeLearningTime();
        List<String> months = Arrays.stream(Month.values())
                .map(month -> month.name().substring(0, 1).toUpperCase() + month.name().substring(1).toLowerCase())
                .collect(Collectors.toList());

        int intYear = Integer.parseInt(year);

        registrations.forEach(registration -> {
            try {
                updateLearningTime(learningTime, registration.getStartDate(), registration.getEndDate(), intYear);
                if (registration.getEndDate().getYear() == intYear) {
                    int endMonth = registration.getEndDate().getMonthValue();
                    scores.set(endMonth - 1, scores.get(endMonth - 1) + registration.getScore());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });

        var currentYear = ZonedDateTime.now().getYear();
        var currentMonth = ZonedDateTime.now().getMonthValue();

        for (int i = 1; i <= 12; i++) {
            if (currentYear == intYear && i > currentMonth) {
                learningTimeResult.add(null);
                scores.set(i - 1, null);
            } else {
                if (i > 1) {
                    scores.set(i - 1, scores.get(i - 1) + scores.get(i - 2));
                }
                int startDay = learningTime.get(i - 1).get(0);
                int endDay = learningTime.get(i - 1).get(1);
                int learningDays = endDay - startDay + 1;
                learningTimeResult.add(Math.max(learningDays, 0));
            }
        }

        return List.of(scores, learningTimeResult, months);
    }

    // Helper method to get registrations by user ID and year
    private List<Registration> getRegistrationByUserIdAndYear(Long id, String year) {
        String jpql = "SELECT r FROM Registration r " +
                "WHERE r.status IN :acceptStatus " +
                "AND EXTRACT(YEAR FROM r.endDate) >= :year " +
                "AND EXTRACT(YEAR FROM r.startDate) <= :year " +
                "AND r.user.id = :userId";
        TypedQuery<Registration> query = entityManager.createQuery(jpql, Registration.class);
        query.setParameter("acceptStatus", RegistrationStatusUtil.ACCEPTED_STATUSES);
        query.setParameter("year", year);
        query.setParameter("userId", id);
        return query.getResultList();
    }

    // Method to update learning time based on start and end dates
    private void updateLearningTime(List<List<Integer>> learningTime, ZonedDateTime startDay, ZonedDateTime endDay, int year) {
        if (startDay.getYear() < year && endDay.getYear() > year) {
            updateLearningTime(learningTime, 1, 12, year, 1, 31);
        } else if (startDay.getYear() < year) {
            updateLearningTime(learningTime, 1, endDay.getMonthValue(), year, 1, endDay.getDayOfMonth());
        } else if (endDay.getYear() > year) {
            updateLearningTime(learningTime, startDay.getMonthValue(), 12, year, startDay.getDayOfMonth(), 31);
        } else {
            updateLearningTime(learningTime, startDay.getMonthValue(), endDay.getMonthValue(), year, startDay.getDayOfMonth(), endDay.getDayOfMonth());
        }
    }

    // Method to update learning time for a specific range of months
    private void updateLearningTime(List<List<Integer>> learningTime, int startMonth, int endMonth, int year, int startDay, int endDay) {
        for (int i = startMonth; i <= endMonth; i++) {
            if (i == startMonth) {
                if (i == endMonth)
                    updateLearningTime(i, endDay, startDay, learningTime);
                else
                    updateLearningTime(i, YearMonth.of(year, i).lengthOfMonth(), startDay, learningTime);

            }
            if (i == endMonth) {
                updateLearningTime(i, endDay, 1, learningTime);
            } else {
                updateLearningTime(i, YearMonth.of(year, i).lengthOfMonth(), 1, learningTime);
            }
        }
    }

    // Method to update learning time for a specific month
    private void updateLearningTime(int month, int endDay, int startDay, List<List<Integer>> learningTime) {
        int oldStartDay = learningTime.get(month - 1).get(0);
        int oldEndDay = learningTime.get(month - 1).get(1);
        oldStartDay = Math.min(oldStartDay, startDay);
        oldEndDay = Math.max(oldEndDay, endDay);
        learningTime.set(month - 1, Arrays.asList(oldStartDay, oldEndDay));
    }

    // Method to get a list of user scores
    private List<Object[]> getListOfUserScore(String year, Long id) {
        String jpql = "SELECT u.id, COALESCE(SUM(r.score), 0) AS scores " +
                "FROM User u JOIN Registration r ON r.user.id = u.id " +
                "WHERE r.status IN :acceptStatus AND EXTRACT(YEAR FROM r.endDate) = :year " +
                "GROUP BY u.id " +
                "ORDER BY scores DESC";
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("acceptStatus", RegistrationStatusUtil.ACCEPTED_STATUSES);
        query.setParameter("year", year);
        return query.getResultList();
    }
}
