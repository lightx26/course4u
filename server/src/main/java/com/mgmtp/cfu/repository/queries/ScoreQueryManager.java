package com.mgmtp.cfu.repository.queries;

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO;
import com.mgmtp.cfu.dto.userdto.ScorePerYearDTO;
import com.mgmtp.cfu.dto.userdto.UserScore;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
                "ORDER BY COALESCE(SUM(r.re_score), 0) DESC, u.username asc "
                ;



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
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public UserScore getMyScore(String year, Long id) {
        try {
            // Define the JPQL query to select user ID and total scores
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
            List<Object[]> listOfUserScores = query.getResultList();


            // Iterate through the list of user scores
            for (int i = 0; i < listOfUserScores.size(); i++) {
                Object[] userScore = listOfUserScores.get(i);
                Long userId = (Long) userScore[0];
                if (userId.equals(id)) {
                   return UserScore.builder().score((Long) userScore[1]).rank(i+1).year(year).build();
                }
            }

            return UserScore.builder().year(year).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
