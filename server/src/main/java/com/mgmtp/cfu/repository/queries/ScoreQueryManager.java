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
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScoreQueryManager {
    private final EntityManager entityManager;

    public List<LeaderboardUserDTO> getLeaderboardUsers(int year, int top) {
        var jpql = "SELECT new com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO(u.id, " +
                "COALESCE(SUM(r.re_score), 0), " +
                "COALESCE(SUM(FUNCTION('DATEDIFF', day, r.re_startDate, r.re_endDate)), 0), " +
                "u.avatarUrl, u.email, u.username, u.fullName) " +
                "FROM User u LEFT JOIN (SELECT re.id as re_id, re.score as re_score, re.startDate as re_startDate, re.endDate as re_endDate, re.user.id as re_user_id " +
                "FROM Registration re WHERE EXTRACT(YEAR FROM re.endDate) = :year AND re.status IN :acceptStatus) r  " +
                "ON u.id = r.re_user_id " +
                "WHERE u.role='USER' " +
                "GROUP BY u.id, u.avatarUrl, u.email, u.username, u.fullName " +
                "having COALESCE(SUM(r.re_score), 0)>0 " +
                "ORDER BY COALESCE(SUM(r.re_score), 0) DESC, u.username asc ";


        TypedQuery<LeaderboardUserDTO> query = entityManager.createQuery(jpql, LeaderboardUserDTO.class);
        query.setParameter("year", year);
        query.setParameter("acceptStatus", RegistrationStatusUtil.ACCEPTED_STATUSES);
        query.setMaxResults(top);
        return query.getResultList();
    }


    public Set<Integer> getExistedYears() {
        var jpql = "select  distinct  YEAR(r.endDate) " +
                " from Registration r" +
                " WHERE r.endDate IS NOT NULL" +
                " ORDER BY YEAR(r.endDate) ASC";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        return new HashSet<>(query.getResultList());
    }


    public List<ScorePerYearDTO> getMyScoreStatistics(Long id) {
        try {
            // Define the JPQL query to select year, total days, and total score per year
            var jpql = "SELECT new com.mgmtp.cfu.dto.userdto.ScorePerYearDTO(" +
                    "YEAR(r.endDate), " +
                    "COALESCE(SUM(DATEDIFF(day,r.startDate, r.endDate)), 0), " +
                    "COALESCE(SUM(r.score), 0)) " +
                    "FROM Registration r " +
                    "JOIN User u ON r.user.id = u.id " +
                    "WHERE u.id = :userId AND r.status IN :acceptStatus AND r.endDate IS NOT NULL AND r.score IS NOT NULL " +
                    "GROUP BY YEAR(r.endDate)" +
                    " order by YEAR(r.endDate)";

            // Create a TypedQuery with the specified JPQL and result class
            TypedQuery<ScorePerYearDTO> query = entityManager.createQuery(jpql, ScorePerYearDTO.class);

            // Set parameters for the query: user ID and accepted statuses
            query.setParameter("userId", id);
            query.setParameter("acceptStatus", RegistrationStatusUtil.ACCEPTED_STATUSES);

            // Execute the query and return the result list
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public UserScore getMyScore(String year, Long id) {
        try {

            // Get the result list
            List<Object[]> listOfUserScores = getListOfUserScore(year, id);
            List<Object> dataPerMoth = getScoreAndLearningTimePerMoth(year, id);

            // Iterate through the list of user scores
            for (int i = 0; i < listOfUserScores.size(); i++) {
                Object[] userScore = listOfUserScores.get(i);
                Long userId = (Long) userScore[0];
                if (userId.equals(id)) {
                    return UserScore.builder().score((Long) userScore[1]).rank(i + 1).year(year)
                            .months((List<String>) dataPerMoth.get(2))
                            .scores((List<Double>) dataPerMoth.get(0))
                            .learningTime((List<Double>) dataPerMoth.get(1)).build();

                }
            }

            //because user don't have score or completed any course for this year, so we don't set score, and learning time per month for they.
            return UserScore.builder().year(year).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Object> getScoreAndLearningTimePerMoth(String year, Long id) {
        String jpql = "SELECT r from Registration r " +
                " WHERE r.status IN :acceptStatus " +
                "   and EXTRACT(YEAR FROM r.endDate) <= :year " +
                "   and EXTRACT(YEAR FROM r.startDate) >= :year " +
                "   and r.user.id=:userId";
        TypedQuery<Registration> query = entityManager.createQuery(jpql, Registration.class);
        query.setParameter("acceptStatus", RegistrationStatusUtil.ACCEPTED_STATUSES);
        query.setParameter("year", year);
        query.setParameter("userId", id);
        var registrations = query.getResultList();
        List<Double> scores = new ArrayList<>();
        List<Double> learningTime = new ArrayList<>();
        List<String> months = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            scores.add(0.0);
            learningTime.add(0.0);
            var moth=Month.of(i).name();
            months.add(moth.substring(0,1).toUpperCase() +moth.substring(1).toLowerCase());
        }
        var intYear = Integer.parseInt(year);

        registrations.forEach(registration -> {
                    var startDay = registration.getStartDate();
                    var endDay = registration.getEndDate();
                    var learningDays = (ChronoUnit.DAYS.between(startDay, endDay));
                    learningDays = learningDays != 0 ? learningDays : 1;
                    float scorePerDay =((float)registration.getScore() / (learningDays));
                    if ((startDay.getYear() < intYear) && endDay.getYear() > intYear) {
                        updateScoresAndLearningTime(scores, learningTime, 1, 12, intYear, scorePerDay, 1, 31);
                    } else if (startDay.getYear() < intYear && endDay.getYear() == intYear) {
                        updateScoresAndLearningTime(scores, learningTime, 1, endDay.getMonthValue(), intYear, scorePerDay, 1, endDay.getDayOfMonth());
                    } else if (
                            startDay.getYear() == intYear && endDay.getYear() > intYear
                    ) {
                        updateScoresAndLearningTime(scores, learningTime, startDay.getMonthValue(), 12, intYear, scorePerDay, startDay.getDayOfMonth(), 31);
                    } else {
                        updateScoresAndLearningTime(scores, learningTime, startDay.getMonthValue(), endDay.getMonthValue(), intYear, scorePerDay, startDay.getDayOfMonth(), endDay.getDayOfMonth());
                    }
                }
        );
        for (int i = 1; i <= 12; i++) {
            scores.set(i-1, (double) Math.round(scores.get(i-1)));
        }
        return List.of(scores, learningTime,months);

    }

    private void updateScoresAndLearningTime(List<Double> scores, List<Double> learningTime, int startMonth, int endMonth, Integer year, float scorePerDay, int startDay, int endDay) {

        if (startMonth == endMonth) {

            var learningDays = endDay - startDay +1> 0 ? endDay - startDay +1 : 1;
            scores.set(startMonth - 1, (scores.get(startMonth - 1) + learningDays * scorePerDay));
            learningTime.set(startMonth - 1, learningTime.get(startMonth - 1) + learningDays);

        } else
            for (int i = startMonth; i <= endMonth; i++) {
                int dayPerMonth = YearMonth.of(year, i).lengthOfMonth();
                if (i == startMonth)
                    dayPerMonth = dayPerMonth - startDay;
                else if (i == endDay)
                    dayPerMonth = endDay;
                scores.set(i - 1, (scores.get(i - 1) + dayPerMonth * scorePerDay));
                learningTime.set(i - 1, learningTime.get(i - 1) + dayPerMonth);
            }
    }

    private List<Object[]> getListOfUserScore(String year, Long id) {
        String jpql = "SELECT u.id, COALESCE(SUM(r.score), 0) AS scores " +
                "FROM User u JOIN Registration r ON r.user.id = u.id " +
                "WHERE r.status IN :acceptStatus and EXTRACT(YEAR FROM r.endDate) = :year " +
                "GROUP BY u.id " +
                "ORDER BY scores DESC";


        // Create a TypedQuery with the specified JPQL and result class
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("acceptStatus", RegistrationStatusUtil.ACCEPTED_STATUSES);
        query.setParameter("year", year);

        // Get the result list
        return query.getResultList();
    }
}
