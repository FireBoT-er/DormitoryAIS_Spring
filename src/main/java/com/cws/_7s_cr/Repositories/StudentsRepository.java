package com.cws._7s_cr.Repositories;

import com.cws._7s_cr.Models.Students;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentsRepository extends JpaRepository<Students, Long> {
    Optional<Students> findFirstByOrderByIdDesc();
}
