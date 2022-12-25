package com.cws._7s_cr.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Data
public class Visitors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(max = 30, message = "Максимум {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ\\-]+", message = "Только русский алфавит")
    private String surname;

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(max = 30, message = "Максимум {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ\\-]+", message = "Только русский алфавит")
    private String name;

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(max = 30, message = "Максимум {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ\\-]+", message = "Только русский алфавит")
    private String patronymic;

    @NotBlank(message = "Поле должно быть заполнено")
    @Pattern(regexp = "8\\d{10}", message = "Введите номер телефона, начинающийся с 8")
    private String phone;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateVs;

    @NotNull(message = "Поле должно быть заполнено")
    private LocalTime timeVs;

    @ManyToOne
    @JsonBackReference
    private Students student;

    public Visitors(String surname, String name, String patronymic, String phone, LocalDate dateVs, LocalTime timeVs, Students student) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phone = phone;
        this.dateVs = dateVs;
        this.timeVs = timeVs;
        this.student = student;
    }
}
