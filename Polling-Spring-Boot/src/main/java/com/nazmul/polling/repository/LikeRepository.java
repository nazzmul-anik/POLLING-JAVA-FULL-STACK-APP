package com.nazmul.polling.repository;

import com.nazmul.polling.entity.Like;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByPoll_Id(Long id);
    Optional<Like> findByPoll_IdAndUser_Id(Long pollId, Long userId);
}
