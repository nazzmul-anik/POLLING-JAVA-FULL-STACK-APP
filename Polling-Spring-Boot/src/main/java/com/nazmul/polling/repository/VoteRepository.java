package com.nazmul.polling.repository;

import com.nazmul.polling.entity.Poll;
import com.nazmul.polling.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByPoll_IdAndUser_Id(Long pollId, Long userId);
    boolean  existsByPoll_IdAndUser_IdAndOption_Id(Long pollId, Long userId, Long optionId);
}
