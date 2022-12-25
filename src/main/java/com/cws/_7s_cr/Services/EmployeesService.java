package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.Employees;
import com.cws._7s_cr.Repositories.EmployeesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeesService {
    private final EmployeesRepository employeesRepository;

    public EmployeesService(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }

    public List<Employees> getAll(){
        return employeesRepository.findAll();
    }

    public List<Employees> findAllByIsWorkingNowTrue(){
        return employeesRepository.findAllByIsWorkingNowTrue();
    }

    public Optional<Employees> getOne(Long id){
        return employeesRepository.findById(id);
    }

    public void createOrUpdate(Employees employee){
        employeesRepository.save(employee);
    }

    public void delete(Long id){
        employeesRepository.deleteById(id);
    }
}
