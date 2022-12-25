package com.cws._7s_cr.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Employees {
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

//    @NotBlank(message = "Поле должно быть заполнено")
    private String photoFileName;

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(max = 50, message = "Максимум {max} символов")
    private String empPosition;

//    @NotNull(message = "Поле должно быть заполнено")
    private Boolean isWorkingNow;

    @ManyToMany
    private List<Cleanings> cleanings;

    public Employees(String surname, String name, String patronymic, String photoFileName, String empPosition, Boolean isWorkingNow) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.photoFileName = photoFileName;
        this.empPosition = empPosition;
        this.isWorkingNow = isWorkingNow;
    }
}
