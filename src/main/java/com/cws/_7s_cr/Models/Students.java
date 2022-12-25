package com.cws._7s_cr.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Students {
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

    @NotNull(message = "Поле должно быть заполнено")
    private Boolean sex;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank(message = "Поле должно быть заполнено")
//    @Size(max = 10, message = "Максимум {max} символов")
    @Pattern(regexp = "[а-яА-ЯёЁ\\s]{3}-[0-9]{3}-[0-9]{2}", message = "3 буквы - 3 цифры - 2 цифры\nТолько русский алфавит")
    private String recordBookNumber;

    @ManyToMany
    private List<Violations> violations;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<Visitors> visitors;

    public Students(String surname, String name, String patronymic, String photoFileName, Boolean sex, LocalDate birthday, String recordBookNumber) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.photoFileName = photoFileName;
        this.sex = sex;
        this.birthday = birthday;
        this.recordBookNumber = recordBookNumber;
    }
}
