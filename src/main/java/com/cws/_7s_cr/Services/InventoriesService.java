package com.cws._7s_cr.Services;

import com.cws._7s_cr.Models.Inventory;
import com.cws._7s_cr.Repositories.InventoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoriesService {
    private final InventoriesRepository inventoriesRepository;

    public InventoriesService(InventoriesRepository inventoriesRepository) {
        this.inventoriesRepository = inventoriesRepository;
    }

    public List<Inventory> getAll(){
        return inventoriesRepository.findAll();
    }

    public Optional<Inventory> getOne(Long id){
        return inventoriesRepository.findById(id);
    }

    public void createOrUpdate(Inventory inventory){
        inventoriesRepository.save(inventory);
    }

    public void delete(Long id){
        inventoriesRepository.deleteById(id);
    }
}
