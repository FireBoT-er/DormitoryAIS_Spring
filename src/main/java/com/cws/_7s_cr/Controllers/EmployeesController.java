package com.cws._7s_cr.Controllers;

import com.cws._7s_cr.Miscellaneous.FormatCheck;
import com.cws._7s_cr.Models.Cleanings;
import com.cws._7s_cr.Models.Employees;
import com.cws._7s_cr.Services.CleaningsService;
import com.cws._7s_cr.Services.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/Employees")
public class EmployeesController {
    private EmployeesService employeesService;
    private CleaningsService cleaningsService;

    @Autowired
    public void setServices(EmployeesService employeesService, CleaningsService cleaningsService){
        this.employeesService = employeesService;
        this.cleaningsService = cleaningsService;
    }

    @Autowired
    private HttpSession httpSession;

    private void fillModelWithAllEmployees(Model model){
        model.addAttribute("title", "Сотрудники");

        model.addAttribute("isWorkingNowOnly", httpSession.getAttribute("isWorkingNowOnly"));

        List<Employees> employees;
        if (httpSession.getAttribute("isWorkingNowOnly") == null){
            employees = employeesService.getAll();
        }
        else{
            employees = employeesService.findAllByIsWorkingNowTrue();
        }
        employees.sort(Comparator.comparing(Employees::getSurname));
        model.addAttribute("employees", employees);
    }

    private void fillModelWithEmployee(Model model, String title, Employees employees){
        model.addAttribute("title", title);
        model.addAttribute("employee", employees);
    }

    @GetMapping({ "", "/Index" })
    public String index(Model model){
        fillModelWithAllEmployees(model);
        return "Employees/index";
    }

    @GetMapping("/Details/{id}")
    public String details(Model model, @PathVariable("id") Long id) throws IOException {
        var employees = employeesService.getOne(id);

        if (employees.isEmpty()){
            fillModelWithAllEmployees(model);
            return "redirect:/Employees";
        }

        Employees employee = employees.get();

        Path fileNameAndPath = Paths.get(System.getProperty("user.dir")+"/UserFiles/Photo/Employees/", employee.getPhotoFileName());
        model.addAttribute("photo", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(fileNameAndPath)));

        employee.getCleanings().sort(Comparator.comparing(Cleanings::getDateC).thenComparing(Cleanings::getTimeC).reversed());
        fillModelWithEmployee(model, "Информация о сотруднике", employee);
        return "Employees/details";
    }

    @GetMapping("/ShowPhoto/{id}")
    public String showPhoto(Model model, @PathVariable("id") Long employeeID) throws IOException {
        var employees = employeesService.getOne(employeeID).get();

        model.addAttribute("title", "сотрудника "+ employees.getSurname() + " " + employees.getName() + " " + employees.getPatronymic());

        Path fileNameAndPath = Paths.get(System.getProperty("user.dir")+"/UserFiles/Photo/Employees/", employees.getPhotoFileName());
        model.addAttribute("photo", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(fileNameAndPath)));

        return "Shared/showPhoto";
    }

    @GetMapping("/Create")
    public String create(Model model){
        fillModelWithEmployee(model, "Добавить сотрудника", new Employees());
        return "Employees/create";
    }

    @PostMapping("/Create")
    public String create(Model model, @ModelAttribute("employee") @Valid Employees employees, BindingResult result, @RequestParam MultipartFile upload) throws IOException {
        if (result.hasErrors()) {
            fillModelWithEmployee(model, "Добавить сотрудника", employees);
            return "Employees/create";
        }

        byte[] photo = upload.getBytes();

        if (photo.length==0){
            fillModelWithEmployee(model, "Добавить сотрудника", employees);
            model.addAttribute("photoFileNameError", "Поле должно быть заполнено");
            return "Employees/create";
        }

        if (!FormatCheck.isImage(photo)){
            fillModelWithEmployee(model, "Добавить сотрудника", employees);
            model.addAttribute("photoFileNameError", "Выбранный файл не является изображением");
            return "Employees/create";
        }

        String path = System.getProperty("user.dir")+"/UserFiles/Photo/Employees/";

        long fileNumber = 1;
        var files = new File(path).list();
        if (files.length != 0){
            List<Long> filesN = new ArrayList<>();
            for (String s: files){
                filesN.add(Long.parseLong(s));
            }
            Collections.sort(filesN);

            fileNumber = filesN.get(filesN.size()-1)+1;
        }

        Path fileNameAndPath = Paths.get(path, Long.toString(fileNumber));
        Files.write(fileNameAndPath, photo);

        employees.setPhotoFileName(Long.toString(fileNumber));
        employees.setIsWorkingNow(true);

        employeesService.createOrUpdate(employees);
        fillModelWithAllEmployees(model);
        return "redirect:/Employees";
    }

    @GetMapping("/Edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        var employees = employeesService.getOne(id);

        if (employees.isEmpty()){
            fillModelWithAllEmployees(model);
            return "redirect:/Employees";
        }

        Employees employee = employees.get();

        fillModelWithEmployee(model, "Изменить сведения о сотруднике", employee);
        return "Employees/edit";
    }

    @PostMapping("/Edit/{id}")
    public String edit(Model model, @ModelAttribute("employee") @Valid Employees employees, BindingResult result, @RequestParam MultipartFile upload) throws IOException {
        if (result.hasErrors()) {
            fillModelWithEmployee(model, "Изменить сведения о сотруднике", employees);
            return "Employees/edit";
        }

        byte[] photo = upload.getBytes();

        if (photo.length!=0){
            if (!FormatCheck.isImage(photo)){
                fillModelWithEmployee(model, "Изменить сведения о сотруднике", employees);
                model.addAttribute("photoFileNameError", "Выбранный файл не является изображением");
                return "Employees/edit";
            }

            String path = System.getProperty("user.dir")+"/UserFiles/Photo/Employees/";

            long fileNumber = 1;
            var files = new File(path).list();
            if (files.length != 0){
                List<Long> filesN = new ArrayList<>();
                for (String s: files){
                    filesN.add(Long.parseLong(s));
                }
                Collections.sort(filesN);

                fileNumber = filesN.get(filesN.size()-1)+1;
            }

            Path fileNameAndPath = Paths.get(path, Long.toString(fileNumber));
            Files.write(fileNameAndPath, upload.getBytes());

            employees.setPhotoFileName(Long.toString(fileNumber));
        }

        if (employees.getIsWorkingNow() == null){
            employees.setIsWorkingNow(false);
        }

        employeesService.createOrUpdate(employees);
        fillModelWithAllEmployees(model);
        return "redirect:/Employees";
    }

    @GetMapping("/Delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        var employees = employeesService.getOne(id);

        if (employees.isEmpty()){
            fillModelWithAllEmployees(model);
            return "redirect:/Employees";
        }

        model.addAttribute("title", "Удаление сотрудника");
        model.addAttribute("employee", employees.get());
        return "Employees/delete";
    }

    @PostMapping("/Delete/{id}")
    public String deleteConfirmed(Model model, @PathVariable("id") Long id){
        Employees employee = employeesService.getOne(id).get();

        var cleanings = new ArrayList<>(employee.getCleanings());
        employee.setCleanings(null);
        employeesService.createOrUpdate(employee);

        for (var cleaning: cleanings){
            var performers = cleaning.getEmployees();
            performers.remove(employee);

            if (performers.size() == 0){
                cleaning.setEmployees(null);
                cleaningsService.delete(cleaning.getId());
            }
            else{
                cleaning.setEmployees(performers);
                cleaningsService.createOrUpdate(cleaning);
            }
        }

        employeesService.delete(id);
        fillModelWithAllEmployees(model);
        return "redirect:/Employees";
    }

    @PostMapping("/EmployeesSwitch")
    public String employeesSwitch(Model model, Boolean isWorkingNowOnly){
        httpSession.setAttribute("isWorkingNowOnly", isWorkingNowOnly);
        fillModelWithAllEmployees(model);
        return "redirect:/Employees";
    }
}
