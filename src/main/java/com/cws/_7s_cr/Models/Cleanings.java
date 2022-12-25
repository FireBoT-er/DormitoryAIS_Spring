package com.cws._7s_cr.Models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Cleanings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateC;

    @NotNull(message = "Поле должно быть заполнено")
    private LocalTime timeC;

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(max = 500, message = "Максимум {max} символов")
    private String cleaned;

    @ManyToMany
    private List<Employees> employees;

    public Cleanings(LocalDate dateC, LocalTime timeC, String cleaned) {
        this.dateC = dateC;
        this.timeC = timeC;
        this.cleaned = cleaned;
    }
}
