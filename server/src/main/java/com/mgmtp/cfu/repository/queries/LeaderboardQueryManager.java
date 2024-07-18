package com.mgmtp.cfu.repository.queries;

import com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO;
import com.mgmtp.cfu.util.RegistrationStatusUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaderboardQueryManager {
    private final EntityManager entityManager;

    public List<LeaderboardUserDTO> getLeaderboardUsers(int year, int top) {
        var jpql = "SELECT new com.mgmtp.cfu.dto.leaderboarddto.LeaderboardUserDTO(u.id, " +
                "COALESCE(SUM(r.re_score), 0), " +
                "COALESCE(SUM(FUNCTION('DATEDIFF', day, r.re_startDate, r.re_endDate)), 0), " +
                "u.avatarUrl, u.email, u.username, u.fullName) " +
                "FROM User u LEFT JOIN (SELECT re.id as re_id, re.score as re_score, re.startDate as re_startDate, re.endDate as re_endDate, re.user.id as re_user_id " +
                "FROM Registration re WHERE EXTRACT(YEAR FROM re.endDate) = :year AND re.status IN :acceptStatus) r " +
                "ON u.id = r.re_user_id " +
                "GROUP BY u.id, u.avatarUrl, u.email, u.username, u.fullName " +
                "ORDER BY COALESCE(SUM(r.re_score), 0) DESC";



        TypedQuery<LeaderboardUserDTO> query = entityManager.createQuery(jpql, LeaderboardUserDTO.class);
        query.setParameter("year", year);
        query.setParameter("acceptStatus", RegistrationStatusUtil.ACCEPTED_STATUSES);
        query.setMaxResults(top);
        return query.getResultList();
    }


    public Set<String> getExistedYears() {
        var jpql = "select  distinct  YEAR(r.endDate) " +
                " from Registration r" +
                " WHERE r.endDate IS NOT NULL";
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
        return new HashSet<>(query.getResultList());
    }
}
