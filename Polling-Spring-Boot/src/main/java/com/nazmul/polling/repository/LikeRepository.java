package com.nazmul.polling.repository;

import com.nazmul.polling.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByPollId(Long id);
    Optional<Like> findByPollIdAndUserId(Long pollId, Long userId);
}
