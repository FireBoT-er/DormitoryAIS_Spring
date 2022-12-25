package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.Violations;
import com.cws._7s_cr.Repositories.ViolationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViolationsService {
    private final ViolationsRepository violationsRepository;

    public ViolationsService(ViolationsRepository violationsRepository) {
        this.violationsRepository = violationsRepository;
    }

    public List<Violations> getAll(){
        return violationsRepository.findAll();
    }

    public Optional<Violations> getOne(Long id){
        return violationsRepository.findById(id);
    }

    public void createOrUpdate(Violations violation){
        violationsRepository.save(violation);
    }

    public void delete(Long id){
        violationsRepository.deleteById(id);
    }
}
