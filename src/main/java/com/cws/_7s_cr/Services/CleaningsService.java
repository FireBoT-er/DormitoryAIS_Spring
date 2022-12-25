package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.Cleanings;
import com.cws._7s_cr.Repositories.CleaningsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CleaningsService {
    private final CleaningsRepository cleaningsRepository;

    public CleaningsService(CleaningsRepository cleaningsRepository) {
        this.cleaningsRepository = cleaningsRepository;
    }

    public List<Cleanings> getAll(){
        return cleaningsRepository.findAll();
    }

    public Optional<Cleanings> getOne(Long id){
        return cleaningsRepository.findById(id);
    }

    public void createOrUpdate(Cleanings cleaning){
        cleaningsRepository.save(cleaning);
    }

    public void delete(Long id){
        cleaningsRepository.deleteById(id);
    }
}
