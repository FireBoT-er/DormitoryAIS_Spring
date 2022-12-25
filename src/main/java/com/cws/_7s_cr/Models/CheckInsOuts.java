package com.cws._7s_cr.Models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class CheckInsOuts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Students student;

    @OneToOne
    private Rooms room;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    public CheckInsOuts(Students student, Rooms room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.student = student;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}
