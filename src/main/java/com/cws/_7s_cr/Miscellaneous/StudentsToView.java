package com.cws._7s_cr.Miscellaneous;

import com.cws._7s_cr.Models.Rooms;
import com.cws._7s_cr.Models.Students;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class StudentsToView {
    private Students student;
    private Rooms room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
