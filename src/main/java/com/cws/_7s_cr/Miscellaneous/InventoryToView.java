package com.cws._7s_cr.Miscellaneous;

import com.cws._7s_cr.Models.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InventoryToView {
    private Inventory inventory;
    private Integer inStock;
    private Integer issued;
}
