package com.cws._7s_cr.Controllers;

import com.cws._7s_cr.Miscellaneous.InventoryToView;
import com.cws._7s_cr.Models.Inventory;
import com.cws._7s_cr.Models.IssuedInventory;
import com.cws._7s_cr.Services.InventoriesService;
import com.cws._7s_cr.Services.IssuedInventoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/Inventories")
public class InventoriesController {
    private InventoriesService inventoriesService;
    private IssuedInventoriesService issuedInventoriesService;

    @Autowired
    public void setServices(InventoriesService inventoriesService, IssuedInventoriesService issuedInventoriesService){
        this.inventoriesService = inventoriesService;
        this.issuedInventoriesService = issuedInventoriesService;
    }

    private void fillModelWithAllInventories(Model model){
        model.addAttribute("title", "Инвентарь");

        List<InventoryToView> toView = new ArrayList<>();

        for (var item: inventoriesService.getAll()){
            int issued = getIssued(item);
            toView.add(new InventoryToView(item, item.getInvCount()-issued, issued));
        }

        toView.sort(Comparator.comparing(inventoryToView -> inventoryToView.getInventory().getInvType()));
        model.addAttribute("inventoriesToView", toView);
    }

    private Integer getIssued(Inventory item){
        var issuedInventories = issuedInventoriesService.findByInventoryItemAndTurnInDateGreaterThanEqualDateNow(item);
        int issued = 0;
        if (issuedInventories.size()>0){
            for (var issuedInventory: issuedInventories){
                issued += issuedInventory.getIssCount();
            }
        }

        return issued;
    }

    private void fillModelWithInventoryItem(Model model, String title, Inventory inventoryItem){
        model.addAttribute("title", title);
        model.addAttribute("inventoryItem", inventoryItem);
    }

    @GetMapping({ "", "/Index" })
    public String index(Model model){
        fillModelWithAllInventories(model);
        return "Inventories/index";
    }

    @GetMapping("/Details/{id}")
    public String details(Model model, @PathVariable("id") Long id){
        var inventoryItem = inventoriesService.getOne(id);

        if (inventoryItem.isEmpty()){
            fillModelWithAllInventories(model);
            return "redirect:/Inventories";
        }

        model.addAttribute("title", "Информация о виде инвентаря");

        var issuedInventories = issuedInventoriesService.findByInventoryItemAndTurnInDateGreaterThanEqualDateNow(inventoryItem.get());
        issuedInventories.sort(Comparator.comparing(IssuedInventory::getIssueDate).reversed());
        model.addAttribute("issuedInventories", issuedInventories);

        int issued = getIssued(inventoryItem.get());
        model.addAttribute("inventoryToView", new InventoryToView(inventoryItem.get(), inventoryItem.get().getInvCount()-issued, issued));
        return "Inventories/details";
    }

    @GetMapping("/Create")
    public String create(Model model){
        fillModelWithInventoryItem(model, "Добавить вид инвентаря", new Inventory());
        return "Inventories/create";
    }

    @PostMapping("/Create")
    public String create(Model model, @ModelAttribute("inventoryItem") @Valid Inventory inventoryItem, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithInventoryItem(model, "Добавить вид инвентаря", inventoryItem);
            return "Inventories/create";
        }

        inventoriesService.createOrUpdate(inventoryItem);
        fillModelWithAllInventories(model);
        return "redirect:/Inventories";
    }

    @GetMapping("/Edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        var inventoryItemOptional = inventoriesService.getOne(id);

        if (inventoryItemOptional.isEmpty()){
            fillModelWithAllInventories(model);
            return "redirect:/Inventories";
        }

        Inventory inventoryItem = inventoryItemOptional.get();

        fillModelWithInventoryItem(model, "Изменить сведения о виде инвентаря", inventoryItem);
        model.addAttribute("issuedSum", getIssued(inventoryItem));
        return "Inventories/edit";
    }

    @PostMapping("/Edit/{id}")
    public String edit(Model model, @ModelAttribute("rooms") @Valid Inventory inventoryItem, Integer originalNumber, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithInventoryItem(model, "Изменить сведения о виде инвентаря", inventoryItem);
            model.addAttribute("issuedSum", getIssued(inventoryItem));
            return "Inventories/edit";
        }

        inventoriesService.createOrUpdate(inventoryItem);
        fillModelWithAllInventories(model);
        return "redirect:/Inventories";
    }

    @GetMapping("/Delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        var inventoryItem = inventoriesService.getOne(id);

        if (inventoryItem.isEmpty()){
            fillModelWithAllInventories(model);
            return "redirect:/Inventories";
        }

        model.addAttribute("title", "Удаление вида инвентаря");

        int issued = getIssued(inventoryItem.get());
        model.addAttribute("inventoryToView", new InventoryToView(inventoryItem.get(), inventoryItem.get().getInvCount()-issued, issued));
        return "Inventories/delete";
    }

    @PostMapping("/Delete/{id}")
    public String deleteConfirmed(Model model, @PathVariable("id") Long id){
        for (var item: issuedInventoriesService.findByInventoryItem(inventoriesService.getOne(id).get())){
            issuedInventoriesService.delete(item.getId());
        }

        inventoriesService.delete(id);

        fillModelWithAllInventories(model);
        return "redirect:/Inventories";
    }
}
