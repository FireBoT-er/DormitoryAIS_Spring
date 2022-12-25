package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.Rooms;
import com.cws._7s_cr.Repositories.RoomsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomsService {
    private final RoomsRepository roomsRepository;

    public RoomsService(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    public List<Rooms> getAll(){
        return roomsRepository.findAll();
    }

    public Optional<Rooms> getOne(Long id){
        return roomsRepository.findById(id);
    }

    public Optional<Rooms> findByRoomNumber(Integer roomNumber){
        return roomsRepository.findByRoomNumber(roomNumber);
    }

    public void createOrUpdate(Rooms room){
        roomsRepository.save(room);
    }

    public void delete(Long id){
        roomsRepository.deleteById(id);
    }
}
