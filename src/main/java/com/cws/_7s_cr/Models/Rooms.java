package com.cws._7s_cr.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Data
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле должно быть заполнено")
    @Min(101)
    @Max(999)
    private Integer roomNumber;

    @NotNull(message = "Поле должно быть заполнено")
    @Min(2)
    @Max(4)
    private Integer beds;

    public Rooms(Integer roomNumber, Integer beds) {
        this.roomNumber = roomNumber;
        this.beds = beds;
    }
}
