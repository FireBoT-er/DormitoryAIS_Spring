package com.cws._7s_cr.Controllers;

import com.cws._7s_cr.Miscellaneous.InventoryToView;
import com.cws._7s_cr.Miscellaneous.StudentsToCEView;
import com.cws._7s_cr.Models.*;
import com.cws._7s_cr.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/IssuedInventories")
public class IssuedInventoriesController {
    private IssuedInventoriesService issuedInventoriesService;
    private InventoriesService inventoriesService;
    private StudentsService studentsService;
    private CheckInsOutsService checkInsOutsService;

    @Autowired
    public void setServices(IssuedInventoriesService issuedInventoriesService, InventoriesService inventoriesService, StudentsService studentsService, CheckInsOutsService checkInsOutsService){
        this.issuedInventoriesService = issuedInventoriesService;
        this.inventoriesService = inventoriesService;
        this.studentsService = studentsService;
        this.checkInsOutsService = checkInsOutsService;
    }

    private void fillModelWithAllIssuedInventories(Model model){
        model.addAttribute("title", "Выданный инвентарь");

        var issuedInventories = issuedInventoriesService.getAll();
        issuedInventories.sort(Comparator.comparing(IssuedInventory::getIssueDate).reversed());
        model.addAttribute("issuedInventories", issuedInventories);
    }

    private void fillModelWithIssuedInventory(Model model, String title, IssuedInventory issuedInventories){
        model.addAttribute("title", title);
        model.addAttribute("issuedInventory", issuedInventories);
    }

    @GetMapping({ "", "/Index" })
    public String index(Model model){
        fillModelWithAllIssuedInventories(model);
        return "IssuedInventories/index";
    }

    @GetMapping("/Details/{id}")
    public String details(Model model, @PathVariable("id") Long id){
        var issuedInventories = issuedInventoriesService.getOne(id);

        if (issuedInventories.isEmpty()){
            fillModelWithAllIssuedInventories(model);
            return "redirect:/IssuedInventories";
        }

        model.addAttribute("title", "Информация о выдаче инвентаря");
        model.addAttribute("issuedInventory", issuedInventories.get());
        return "IssuedInventories/details";
    }

    @GetMapping("/Create")
    public String create(Model model){
        List<InventoryToView> toView = new ArrayList<>();

        for (var inventory: inventoriesService.getAll()){
            Integer issCountSum = 0;
            for (var issuedInventory: issuedInventoriesService.findByInventoryItemAndTurnInDateGreaterThanEqualDateNow(inventory)){
                issCountSum += issuedInventory.getIssCount();
            }

            Integer difference = inventory.getInvCount()-issCountSum;
            if (difference>0){
                toView.add(new InventoryToView(inventory, difference, 0));
            }
        }

        model.addAttribute("title", "Выберите вид инвентаря");
        toView.sort(Comparator.comparing(inventoryToView -> inventoryToView.getInventory().getInvType()));
        model.addAttribute("inventoriesToView", toView);
        return "IssuedInventories/create";
    }

    @PostMapping("/Create")
    public String create(Model model, Long inventoryId){
        List<StudentsToCEView> toView = new ArrayList<>();

        for (var item: checkInsOutsService.findByCheckOutDateGreaterThanEqualDateNow()){
            toView.add(new StudentsToCEView(item.getStudent(), item.getRoom()));
        }

        model.addAttribute("title", "Выберите обладателя");
        model.addAttribute("inventoryId", inventoryId);
        toView.sort(Comparator.comparing(studentsToView -> studentsToView.getStudent().getSurname()));
        model.addAttribute("studentsToCEView", toView);
        return "IssuedInventories/chooseOwner";
    }

    @PostMapping("/Create/ChooseOwner")
    public String chooseOwner(Model model, Long inventoryId, Long studentId){
        Inventory inventory = inventoriesService.getOne(inventoryId).get();
        Integer issCountSum = 0;
        for (var issuedInventory: issuedInventoriesService.findByInventoryItemAndTurnInDateGreaterThanEqualDateNow(inventory)){
            issCountSum += issuedInventory.getIssCount();
        }
        Integer difference = inventory.getInvCount() - issCountSum;
        model.addAttribute("issCountMax", difference);

        CheckInsOuts checkInsOuts = checkInsOutsService.findByStudent(studentsService.getOne(studentId).get()).get();
        model.addAttribute("issueDateMin", checkInsOuts.getCheckInDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("turnInDateMax", checkInsOuts.getCheckOutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

        model.addAttribute("inventoryId", inventoryId);
        model.addAttribute("studentId", studentId);
        fillModelWithIssuedInventory(model, "Информация о выдаче", new IssuedInventory());
        return "IssuedInventories/chooseInfo";
    }

    @PostMapping("/Create/ChooseInfo")
    public String chooseInfo(Model model, @ModelAttribute("issuedInventory") IssuedInventory issuedInventories, Long inventoryId, Long studentId){
        if (issuedInventories.getIssCount() == null || issuedInventories.getIssueDate() == null || issuedInventories.getTurnInDate() == null){
            if (issuedInventories.getIssCount() == null){
                model.addAttribute("issCountError", "Поле должно быть заполнено");
            }
            else if (issuedInventories.getIssueDate() == null){
                model.addAttribute("issueDateError", "Поле должно быть заполнено");
            }
            else{
                model.addAttribute("turnInDateError", "Поле должно быть заполнено");
            }

            Inventory inventory = inventoriesService.getOne(inventoryId).get();
            Integer issCountSum = 0;
            for (var issuedInventory: issuedInventoriesService.findByInventoryItemAndTurnInDateGreaterThanEqualDateNow(inventory)){
                issCountSum += issuedInventory.getIssCount();
            }
            Integer difference = inventory.getInvCount() - issCountSum;
            model.addAttribute("issCountMax", difference);

            CheckInsOuts checkInsOuts = checkInsOutsService.findByStudent(studentsService.getOne(studentId).get()).get();
            model.addAttribute("issueDateMin", checkInsOuts.getCheckInDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            model.addAttribute("turnInDateMax", checkInsOuts.getCheckOutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

            model.addAttribute("inventoryId", inventoryId);
            model.addAttribute("studentId", studentId);
            fillModelWithIssuedInventory(model, "Информация о выдаче", issuedInventories);
            return "IssuedInventories/chooseInfo";
        }

        issuedInventories.setStudent(studentsService.getOne(studentId).get());
        issuedInventories.setInventoryItem(inventoriesService.getOne(inventoryId).get());
        issuedInventoriesService.createOrUpdate(issuedInventories);

        fillModelWithAllIssuedInventories(model);
        return "redirect:/IssuedInventories";
    }

    @GetMapping("/Edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        var issuedInventories = issuedInventoriesService.getOne(id);

        if (issuedInventories.isEmpty()){
            fillModelWithAllIssuedInventories(model);
            return "redirect:/IssuedInventories";
        }

        IssuedInventory issuedInventory = issuedInventories.get();

        Inventory inventory = inventoriesService.getOne(issuedInventory.getInventoryItem().getId()).get();
        Integer issCountSum = 0;
        for (var item : issuedInventoriesService.findByInventoryItemAndTurnInDateGreaterThanEqualDateNow(inventory)){
            issCountSum += item.getIssCount();
        }
        Integer issCountMax = inventory.getInvCount() - issCountSum + issuedInventory.getIssCount();
        model.addAttribute("issCountMax", issCountMax);

        CheckInsOuts checkInsOuts = checkInsOutsService.findByStudent(studentsService.getOne(issuedInventory.getStudent().getId()).get()).get();
        model.addAttribute("issueDateMin", checkInsOuts.getCheckInDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("turnInDateMax", checkInsOuts.getCheckOutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

        fillModelWithIssuedInventory(model, "Изменить информацию о выдаче", issuedInventory);
        return "IssuedInventories/edit";
    }

    @PostMapping("/Edit/{id}")
    public String edit(Model model, @ModelAttribute("issuedInventory") @Valid IssuedInventory issuedInventories, BindingResult result){
        if (result.hasErrors()){
            Inventory inventory = inventoriesService.getOne(issuedInventories.getInventoryItem().getId()).get();
            Integer issCountSum = 0;
            for (var item : issuedInventoriesService.findByInventoryItemAndTurnInDateGreaterThanEqualDateNow(inventory)){
                issCountSum += item.getIssCount();
            }

            Integer issCount = 0;
            if (issuedInventories.getIssCount() != null){
                issCount = issuedInventories.getIssCount();
            }
            Integer issCountMax = inventory.getInvCount() - issCountSum + issCount;
            model.addAttribute("issCountMax", issCountMax);

            CheckInsOuts checkInsOuts = checkInsOutsService.findByStudent(studentsService.getOne(issuedInventories.getStudent().getId()).get()).get();
            model.addAttribute("issueDateMin", checkInsOuts.getCheckInDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            model.addAttribute("turnInDateMax", checkInsOuts.getCheckOutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

            issuedInventories.setStudent(studentsService.getOne(issuedInventories.getStudent().getId()).get());
            issuedInventories.setInventoryItem(inventoriesService.getOne(issuedInventories.getInventoryItem().getId()).get());

            fillModelWithIssuedInventory(model, "Изменить информацию о выдаче", issuedInventories);
            return "IssuedInventories/edit";
        }

        issuedInventoriesService.createOrUpdate(issuedInventories);
        fillModelWithAllIssuedInventories(model);
        return "redirect:/IssuedInventories";
    }

    @GetMapping("/Delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        var issuedInventories = issuedInventoriesService.getOne(id);

        if (issuedInventories.isEmpty()){
            fillModelWithAllIssuedInventories(model);
            return "redirect:/IssuedInventories";
        }

        model.addAttribute("title", "Удаление выдачи инвентаря");
        model.addAttribute("issuedInventory", issuedInventories.get());
        return "IssuedInventories/delete";
    }

    @PostMapping("/Delete/{id}")
    public String deleteConfirmed(Model model, @PathVariable("id") Long id){
        issuedInventoriesService.delete(id);
        fillModelWithAllIssuedInventories(model);
        return "redirect:/IssuedInventories";
    }
}
