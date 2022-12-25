package com.cws._7s_cr.Repositories;

import com.cws._7s_cr.Models.Students;
import com.cws._7s_cr.Models.Visitors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorsRepository extends JpaRepository<Visitors, Long> {
    List<Visitors> findByStudent(Students student);
}
