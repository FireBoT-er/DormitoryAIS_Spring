package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.CheckInsOuts;
import com.cws._7s_cr.Models.Rooms;
import com.cws._7s_cr.Models.Students;
import com.cws._7s_cr.Repositories.CheckInsOutsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CheckInsOutsService {
    private final CheckInsOutsRepository checkInsOutsRepository;

    public CheckInsOutsService(CheckInsOutsRepository checkInsOutsRepository) {
        this.checkInsOutsRepository = checkInsOutsRepository;
    }

    public List<CheckInsOuts> getAll(){
        return checkInsOutsRepository.findAll();
    }

    public Optional<CheckInsOuts> getOne(Long id){
        return checkInsOutsRepository.findById(id);
    }

    public List<CheckInsOuts> findByRoomAndCheckOutDateGreaterThanEqualDateNow(Rooms room){
        return checkInsOutsRepository.findByRoomAndCheckOutDateGreaterThanEqual(room, LocalDate.now());
    }

    public List<CheckInsOuts> findByRoom(Rooms room){
        return checkInsOutsRepository.findByRoom(room);
    }

    public Optional<CheckInsOuts> findByStudent(Students student){
        return checkInsOutsRepository.findByStudent(student);
    }

    public Optional<CheckInsOuts> findByStudentId(Long id){
        return checkInsOutsRepository.findByStudent_id(id);
    }

    public List<CheckInsOuts> findByRoomAndCheckOutDateGreaterThanEqualDateNowAndStudent_Sex(Rooms room, Boolean sex){
        return checkInsOutsRepository.findByRoomAndCheckOutDateGreaterThanEqualAndStudent_Sex(room, LocalDate.now(), sex);
    }

    public List<CheckInsOuts> findByCheckOutDateGreaterThanEqualDateNow(){
        return checkInsOutsRepository.findByCheckOutDateGreaterThanEqual(LocalDate.now());
    }

    public void createOrUpdate(CheckInsOuts checkInOut){
        checkInsOutsRepository.save(checkInOut);
    }

    public void delete(Long id){
        checkInsOutsRepository.deleteById(id);
    }
}
