package com.lgp.webmanager.repository;

import com.lgp.webmanager.domain.Letter;
import com.lgp.webmanager.domain.view.LetterSummaryView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface LetterRepository extends JpaRepository<Letter,Long> {

    String findSql = "select l.id as id,l.sendUserId as sendUserId,u.userName as sendUserName,u.profilePicture as profilePicture,l.content as content,l.createTime as createTime,l.pid as pid,l.type as type from Letter l,User u where l.sendUserId = u.id";

    @Transactional
    @Modifying
    @Query("update Letter l set l.pid = ?2 where l.id = ?1")
    int updatePidById(Long id,Long pid);

    @Query(findSql+" and l.receiveUserId = ?1")
    List<LetterSummaryView> findLetterByReceiveUserId(Long userId, Pageable pageable);

}
