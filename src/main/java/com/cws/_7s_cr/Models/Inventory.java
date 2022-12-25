package com.cws._7s_cr.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@NoArgsConstructor
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(max = 30, message = "Максимум {max} символов")
    private String invType;

    @NotNull(message = "Поле должно быть заполнено")
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer invCount;

    public Inventory(String invType, Integer invCount) {
        this.invType = invType;
        this.invCount = invCount;
    }
}
