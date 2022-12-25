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
public class Violations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(max = 500, message = "Максимум {max} символов")
    private String description;

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(max = 500, message = "Максимум {max} символов")
    private String punishment;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateVl;

    @NotNull(message = "Поле должно быть заполнено")
    private LocalTime timeVl;

    @ManyToMany
    private List<Students> students;

    public Violations(String description, String punishment, LocalDate dateVl, LocalTime timeVl, List<Students> students) {
        this.description = description;
        this.punishment = punishment;
        this.dateVl = dateVl;
        this.timeVl = timeVl;
        this.students = students;
    }
}
