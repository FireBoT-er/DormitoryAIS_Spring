package com.cws._7s_cr.Repositories;

import com.cws._7s_cr.Models.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    Optional<Rooms> findByRoomNumber(Integer roomNumber);
}
