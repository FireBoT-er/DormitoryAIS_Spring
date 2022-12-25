package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.Students;
import com.cws._7s_cr.Models.Visitors;
import com.cws._7s_cr.Repositories.VisitorsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitorsService {
    private final VisitorsRepository visitorsRepository;

    public VisitorsService(VisitorsRepository visitorsRepository) {
        this.visitorsRepository = visitorsRepository;
    }

    public List<Visitors> getAll(){
        return visitorsRepository.findAll();
    }

    public Optional<Visitors> getOne(Long id){
        return visitorsRepository.findById(id);
    }

    public List<Visitors> findByStudent(Students student){
        return visitorsRepository.findByStudent(student);
    }

    public void createOrUpdate(Visitors visitor){
        visitorsRepository.save(visitor);
    }

    public void delete(Long id){
        visitorsRepository.deleteById(id);
    }
}
