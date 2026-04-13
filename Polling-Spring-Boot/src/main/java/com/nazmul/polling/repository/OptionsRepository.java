package com.nazmul.polling.repository;

import com.nazmul.polling.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepository extends JpaRepository<Options, Long> {
}
