package com.cws._7s_cr.Controllers;

import com.cws._7s_cr.Models.Cleanings;
import com.cws._7s_cr.Models.Employees;
import com.cws._7s_cr.Services.CleaningsService;
import com.cws._7s_cr.Services.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/Cleanings")
public class CleaningsController {
    private CleaningsService cleaningsService;
    private EmployeesService employeesService;

    @Autowired
    public void setServices(CleaningsService cleaningsService, EmployeesService employeesService){
        this.cleaningsService = cleaningsService;
        this.employeesService = employeesService;
    }

    private void fillModelWithAllCleanings(Model model){
        model.addAttribute("title", "Уборки");

        var cleanings = cleaningsService.getAll();
        cleanings.sort(Comparator.comparing(Cleanings::getDateC).thenComparing(Cleanings::getTimeC).reversed());
        for (var cleaning: cleanings){
            cleaning.getEmployees().sort(Comparator.comparing(Employees::getSurname));
        }
        model.addAttribute("cleanings", cleanings);
    }

    private void fillModelWithCleaning(Model model, String title, Cleanings cleaning){
        model.addAttribute("title", title);
        model.addAttribute("cleaning", cleaning);
    }

    @GetMapping({ "", "/Index" })
    public String index(Model model){
        fillModelWithAllCleanings(model);
        return "Cleanings/index";
    }

    @GetMapping("/Details/{id}")
    public String details(Model model, @PathVariable("id") Long id){
        var cleanings = cleaningsService.getOne(id);

        if (cleanings.isEmpty()){
            fillModelWithAllCleanings(model);
            return "redirect:/Cleanings";
        }

        Cleanings cleaning = cleanings.get();

        fillModelWithCleaning(model, "Информация об уборке", cleaning);
        return "Cleanings/details";
    }

    @GetMapping("/Create")
    public String create(Model model){
        fillModelWithCleaning(model, "Добавить уборку", new Cleanings());
        return "Cleanings/create";
    }

    @PostMapping("/Create")
    public String create(Model model, @ModelAttribute("cleaning") @Valid Cleanings cleanings, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithCleaning(model, "Добавить уборку", cleanings);
            return "Cleanings/create";
        }

        model.addAttribute("title", "Выберите исполнителей");
        model.addAttribute("id", cleanings.getId());
        model.addAttribute("date", cleanings.getDateC());
        model.addAttribute("time", cleanings.getTimeC());
        model.addAttribute("cleaned", cleanings.getCleaned());

        var employees = employeesService.findAllByIsWorkingNowTrue();
        employees.sort(Comparator.comparing(Employees::getSurname));
        model.addAttribute("employees", employees);
        return "Cleanings/choosePerformers";
    }

    @PostMapping("/Create/ChoosePerformers")
    public String choosePerformers(Model model, Long id, String date, LocalTime time, String cleaned, Long[] employees){
        if (employees == null) {
            model.addAttribute("submitButtonError", "Необходимо выбрать хотя бы одного сотрудника");

            model.addAttribute("title", "Выберите исполнителей");
            model.addAttribute("id", id);
            model.addAttribute("date", date);
            model.addAttribute("time", time);
            model.addAttribute("cleaned", cleaned);

            var employees_table = employeesService.findAllByIsWorkingNowTrue();
            employees_table.sort(Comparator.comparing(Employees::getSurname));
            model.addAttribute("employees", employees_table);
            return "Cleanings/choosePerformers";
        }

        Cleanings cleaning = new Cleanings();
        cleaning.setId(id);
        cleaning.setDateC(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE));
        cleaning.setTimeC(time);
        cleaning.setCleaned(cleaned);

        cleaningsService.createOrUpdate(cleaning);

        List<Employees> employeesList = new ArrayList<>();
        for (var empId: employees){
            var employee = employeesService.getOne(empId).get();

            var empCleanings = employee.getCleanings();
            empCleanings.add(cleaning);
            employee.setCleanings(empCleanings);

            employeesList.add(employee);
        }
        cleaning.setEmployees(employeesList);

        cleaningsService.createOrUpdate(cleaning);

        fillModelWithAllCleanings(model);
        return "redirect:/Cleanings";
    }

    @GetMapping("/Edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        var cleanings = cleaningsService.getOne(id);

        if (cleanings.isEmpty()){
            fillModelWithAllCleanings(model);
            return "redirect:/Cleanings";
        }

        Cleanings cleaning = cleanings.get();

        fillModelWithCleaning(model, "Изменить сведения об уборке", cleaning);
        return "Cleanings/edit";
    }

    @PostMapping("/Edit/{id}")
    public String edit(Model model, @ModelAttribute("cleaning") @Valid Cleanings cleanings, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithCleaning(model, "Изменить сведения об уборке", cleanings);
            return "Cleanings/edit";
        }

        var employees = employeesService.getAll();
        employees.sort(Comparator.comparing(Employees::getSurname));

        var performers = cleaningsService.getOne(cleanings.getId()).get().getEmployees();
        Collections.reverse(performers);
        for (var item: performers){
            if (employees.contains(item)){
                employees.remove(item);
                employees.add(0, item);
            }
        }

        model.addAttribute("title", "Выберите исполнителей");
        model.addAttribute("id", cleanings.getId());
        model.addAttribute("date", cleanings.getDateC());
        model.addAttribute("time", cleanings.getTimeC());
        model.addAttribute("cleaned", cleanings.getCleaned());
        model.addAttribute("employees", employees);
        model.addAttribute("cleaning", cleaningsService.getOne(cleanings.getId()).get());
        return "Cleanings/editPerformers";
    }

    @PostMapping("/Edit/{id}/EditPerformers")
    public String editPerformers(Model model, Long id, String date, LocalTime time, String cleaned, Long[] employees){
        if (employees == null) {
            model.addAttribute("submitButtonError", "Необходимо выбрать хотя бы одного сотрудника");

            var employees_table = employeesService.getAll();
            employees_table.sort(Comparator.comparing(Employees::getSurname));

            var performers = cleaningsService.getOne(id).get().getEmployees();
            Collections.reverse(performers);
            for (var item: performers){
                if (employees_table.contains(item)){
                    employees_table.remove(item);
                    employees_table.add(0, item);
                }
            }

            model.addAttribute("title", "Выберите исполнителей");
            model.addAttribute("id", id);
            model.addAttribute("date", date);
            model.addAttribute("time", time);
            model.addAttribute("cleaned", cleaned);
            model.addAttribute("employees", employees_table);
            model.addAttribute("cleaning", cleaningsService.getOne(id).get());
            return "Cleanings/editPerformers";
        }

        Cleanings cleaning = new Cleanings();
        cleaning.setId(id);
        cleaning.setDateC(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE));
        cleaning.setTimeC(time);
        cleaning.setCleaned(cleaned);

        var cleaningSaved = cleaningsService.getOne(id).get();
        for (var performer : cleaningSaved.getEmployees()){
            var empCleanings = performer.getCleanings();
            empCleanings.remove(cleaningSaved);

            performer.setCleanings(empCleanings);
            employeesService.createOrUpdate(performer);
        }

        cleaningsService.createOrUpdate(cleaning);

        List<Employees> employeesList = new ArrayList<>();
        for (var empId: employees){
            var employee = employeesService.getOne(empId).get();

            var empCleanings = employee.getCleanings();
            empCleanings.add(cleaning);
            employee.setCleanings(empCleanings);

            employeesList.add(employee);
        }
        cleaning.setEmployees(employeesList);

        cleaningsService.createOrUpdate(cleaning);

        fillModelWithAllCleanings(model);
        return "redirect:/Cleanings";
    }

    @GetMapping("/Delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        var cleanings = cleaningsService.getOne(id);

        if (cleanings.isEmpty()){
            fillModelWithAllCleanings(model);
            return "redirect:/Cleanings";
        }

        model.addAttribute("title", "Удаление уборки");
        var cleaning = cleanings.get();
        cleaning.getEmployees().sort(Comparator.comparing(Employees::getSurname));
        model.addAttribute("cleaning", cleaning);
        return "Cleanings/delete";
    }

    @PostMapping("/Delete/{id}")
    public String deleteConfirmed(Model model, @PathVariable("id") Long id){
        Cleanings cleaning = cleaningsService.getOne(id).get();

        var performers = new ArrayList<>(cleaning.getEmployees());
        cleaning.setEmployees(null);
        cleaningsService.createOrUpdate(cleaning);

        for (var performer : performers){
            var empCleanings = performer.getCleanings();
            empCleanings.remove(cleaning);

            performer.setCleanings(empCleanings);
            employeesService.createOrUpdate(performer);
        }

        cleaningsService.delete(id);
        fillModelWithAllCleanings(model);
        return "redirect:/Cleanings";
    }
}
