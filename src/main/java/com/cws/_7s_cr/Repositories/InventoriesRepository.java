package com.cws._7s_cr.Repositories;

import com.cws._7s_cr.Models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoriesRepository extends JpaRepository<Inventory, Long> {
}
