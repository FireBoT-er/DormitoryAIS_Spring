package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.Inventory;
import com.cws._7s_cr.Models.IssuedInventory;
import com.cws._7s_cr.Models.Students;
import com.cws._7s_cr.Repositories.IssuedInventoriesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IssuedInventoriesService {
    private final IssuedInventoriesRepository issuedInventoriesRepository;

    public IssuedInventoriesService(IssuedInventoriesRepository issuedInventoriesRepository) {
        this.issuedInventoriesRepository = issuedInventoriesRepository;
    }

    public List<IssuedInventory> getAll(){
        return issuedInventoriesRepository.findAll();
    }

    public Optional<IssuedInventory> getOne(Long id){
        return issuedInventoriesRepository.findById(id);
    }

    public List<IssuedInventory> findByInventoryItemAndTurnInDateGreaterThanEqualDateNow(Inventory inventoryItem){
        return issuedInventoriesRepository.findByInventoryItemAndTurnInDateGreaterThanEqual(inventoryItem, LocalDate.now());
    }

    public List<IssuedInventory> findByInventoryItem(Inventory inventoryItem){
        return issuedInventoriesRepository.findByInventoryItem(inventoryItem);
    }

    public List<IssuedInventory> findByStudent(Students student){
        return issuedInventoriesRepository.findByStudent(student);
    }

    public void createOrUpdate(IssuedInventory issuedInventory){
        issuedInventoriesRepository.save(issuedInventory);
    }

    public void delete(Long id){
        issuedInventoriesRepository.deleteById(id);
    }
}
