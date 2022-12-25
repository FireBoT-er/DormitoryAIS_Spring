package com.cws._7s_cr.Repositories;

import com.cws._7s_cr.Models.CheckInsOuts;
import com.cws._7s_cr.Models.Rooms;
import com.cws._7s_cr.Models.Students;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CheckInsOutsRepository extends JpaRepository<CheckInsOuts, Long> {
    List<CheckInsOuts> findByRoomAndCheckOutDateGreaterThanEqual(Rooms room, LocalDate checkOutDate);
    List<CheckInsOuts> findByRoom(Rooms room);
    Optional<CheckInsOuts> findByStudent(Students student);
    Optional<CheckInsOuts> findByStudent_id(Long id);
    List<CheckInsOuts> findByRoomAndCheckOutDateGreaterThanEqualAndStudent_Sex(Rooms room, LocalDate checkOutDate, Boolean sex);
    List<CheckInsOuts> findByCheckOutDateGreaterThanEqual(LocalDate checkOutDate);
}
