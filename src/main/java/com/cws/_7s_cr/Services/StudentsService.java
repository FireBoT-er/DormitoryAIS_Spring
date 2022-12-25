package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.Students;
import com.cws._7s_cr.Repositories.StudentsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentsService {
    private final StudentsRepository studentsRepository;

    public StudentsService(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    public List<Students> getAll(){
        return studentsRepository.findAll();
    }

    public Optional<Students> getOne(Long id){
        return studentsRepository.findById(id);
    }

    public Optional<Students> getLast(){
        return studentsRepository.findFirstByOrderByIdDesc();
    }

    public void createOrUpdate(Students student){
        studentsRepository.save(student);
    }

    public void delete(Long id){
        studentsRepository.deleteById(id);
    }
}
