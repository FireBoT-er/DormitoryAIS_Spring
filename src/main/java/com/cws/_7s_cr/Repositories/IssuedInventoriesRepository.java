package com.cws._7s_cr.Repositories;

import com.cws._7s_cr.Models.Inventory;
import com.cws._7s_cr.Models.IssuedInventory;
import com.cws._7s_cr.Models.Students;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IssuedInventoriesRepository extends JpaRepository<IssuedInventory, Long> {
    List<IssuedInventory> findByInventoryItemAndTurnInDateGreaterThanEqual(Inventory inventoryItem, LocalDate turnInDate);
    List<IssuedInventory> findByInventoryItem(Inventory inventoryItem);
    List<IssuedInventory> findByStudent(Students student);
}
