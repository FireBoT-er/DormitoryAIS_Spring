package com.cws._7s_cr.Repositories;

import com.cws._7s_cr.Models.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeesRepository extends JpaRepository<Employees, Long> {
    List<Employees> findAllByIsWorkingNowTrue();
}
