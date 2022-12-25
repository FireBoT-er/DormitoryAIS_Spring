package com.cws._7s_cr.Models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class IssuedInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Students student;

    @OneToOne
    private Inventory inventoryItem;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate turnInDate;

    @NotNull(message = "Поле должно быть заполнено")
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer issCount;

    public IssuedInventory(Students student, Inventory inventoryItem, LocalDate issueDate, LocalDate turnInDate, Integer issCount) {
        this.student = student;
        this.inventoryItem = inventoryItem;
        this.issueDate = issueDate;
        this.turnInDate = turnInDate;
        this.issCount = issCount;
    }
}
