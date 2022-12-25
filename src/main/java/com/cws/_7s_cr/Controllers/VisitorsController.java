package com.cws._7s_cr.Controllers;

import com.cws._7s_cr.Miscellaneous.StudentsToCEView;
import com.cws._7s_cr.Models.*;
import com.cws._7s_cr.Services.CheckInsOutsService;
import com.cws._7s_cr.Services.StudentsService;
import com.cws._7s_cr.Services.VisitorsService;
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
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/Visitors")
public class VisitorsController {
    private VisitorsService visitorsService;
    private StudentsService studentsService;
    private CheckInsOutsService checkInsOutsService;

    @Autowired
    public void setServices(VisitorsService visitorsService, StudentsService studentsService, CheckInsOutsService checkInsOutsService){
        this.visitorsService = visitorsService;
        this.studentsService = studentsService;
        this.checkInsOutsService = checkInsOutsService;
    }

    private void fillModelWithAllVisitors(Model model){
        model.addAttribute("title", "Посетители");

        var visitors = visitorsService.getAll();
        visitors.sort(Comparator.comparing(Visitors::getDateVs).thenComparing(Visitors::getTimeVs).reversed());
        model.addAttribute("visitors", visitors);
    }

    private void fillModelWithVisitor(Model model, String title, Visitors visitors){
        model.addAttribute("title", title);
        model.addAttribute("visitor", visitors);
    }

    @GetMapping({ "", "/Index" })
    public String index(Model model){
        fillModelWithAllVisitors(model);
        return "Visitors/index";
    }

    @GetMapping("/Details/{id}")
    public String details(Model model, @PathVariable("id") Long id){
        var visitors = visitorsService.getOne(id);

        if (visitors.isEmpty()){
            fillModelWithAllVisitors(model);
            return "redirect:/Visitors";
        }

        Visitors visitor = visitors.get();

        fillModelWithVisitor(model, "Информация о посетителе", visitor);
        return "Visitors/details";
    }

    @GetMapping("/Create")
    public String create(Model model){
        fillModelWithVisitor(model, "Добавить посетителя", new Visitors());
        return "Visitors/create";
    }

    @PostMapping("/Create")
    public String create(Model model, @ModelAttribute("visitor") @Valid Visitors visitors, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithVisitor(model, "Добавить посетителя", visitors);
            return "Visitors/create";
        }

        List<StudentsToCEView> toView = new ArrayList<>();

        for (var item: checkInsOutsService.findByCheckOutDateGreaterThanEqualDateNow()){
            toView.add(new StudentsToCEView(item.getStudent(), item.getRoom()));
        }

        toView.sort(Comparator.comparing(studentsToView -> studentsToView.getStudent().getSurname()));

        model.addAttribute("title", "Выберите посещаемого студента");
        model.addAttribute("surname", visitors.getSurname());
        model.addAttribute("name", visitors.getName());
        model.addAttribute("patronymic", visitors.getPatronymic());
        model.addAttribute("phone", visitors.getPhone());
        model.addAttribute("dateVs", visitors.getDateVs());
        model.addAttribute("timeVs", visitors.getTimeVs());
        model.addAttribute("studentsToCEView", toView);
        return "Visitors/chooseVisited";
    }

    @PostMapping("/Create/ChooseVisited")
    public String chooseVisited(Model model, String surname, String name, String patronymic, String phone, String dateVs, LocalTime timeVs, Long studentId){
        Visitors visitors = new Visitors();
        visitors.setSurname(surname);
        visitors.setName(name);
        visitors.setPatronymic(patronymic);
        visitors.setPhone(phone);
        visitors.setDateVs(LocalDate.parse(dateVs, DateTimeFormatter.ISO_LOCAL_DATE));
        visitors.setTimeVs(timeVs);
        visitors.setStudent(studentsService.getOne(studentId).get());

        visitorsService.createOrUpdate(visitors);

        fillModelWithAllVisitors(model);
        return "redirect:/Visitors";
    }

    @GetMapping("/Edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        var visitors = visitorsService.getOne(id);

        if (visitors.isEmpty()){
            fillModelWithAllVisitors(model);
            return "redirect:/Visitors";
        }

        Visitors visitor = visitors.get();

        fillModelWithVisitor(model, "Изменить сведения о посетителе", visitor);
        return "Visitors/edit";
    }

    @PostMapping("/Edit/{id}")
    public String edit(Model model, @ModelAttribute("visitor") @Valid Visitors visitors, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithVisitor(model, "Изменить сведения о посетителе", visitors);
            return "Visitors/edit";
        }

        List<StudentsToCEView> toView = new ArrayList<>();

        List<Students> students = studentsService.getAll();
        students.sort(Comparator.comparing(Students::getSurname));

        Long studentId = visitorsService.getOne(visitors.getId()).get().getStudent().getId();
        Students student = studentsService.getOne(studentId).get();
        students.remove(student);
        students.add(0, student);

        for (var item: students){
            toView.add(new StudentsToCEView(item, checkInsOutsService.findByStudent(item).get().getRoom()));
        }

        model.addAttribute("title", "Выберите посещаемого студента");
        model.addAttribute("id", visitors.getId());
        model.addAttribute("surname", visitors.getSurname());
        model.addAttribute("name", visitors.getName());
        model.addAttribute("patronymic", visitors.getPatronymic());
        model.addAttribute("phone", visitors.getPhone());
        model.addAttribute("dateVs", visitors.getDateVs());
        model.addAttribute("timeVs", visitors.getTimeVs());
        model.addAttribute("studentId", studentId);
        model.addAttribute("studentsToCEView", toView);
        return "Visitors/editVisited";
    }

    @PostMapping("/Edit/{id}/EditVisited")
    public String editVisited(Model model, Long id, String surname, String name, String patronymic, String phone, String dateVs, LocalTime timeVs, Long studentId){
        Visitors visitors = new Visitors();
        visitors.setId(id);
        visitors.setSurname(surname);
        visitors.setName(name);
        visitors.setPatronymic(patronymic);
        visitors.setPhone(phone);
        visitors.setDateVs(LocalDate.parse(dateVs, DateTimeFormatter.ISO_LOCAL_DATE));
        visitors.setTimeVs(timeVs);
        visitors.setStudent(studentsService.getOne(studentId).get());

        visitorsService.createOrUpdate(visitors);

        fillModelWithAllVisitors(model);
        return "redirect:/Visitors";
    }

    @GetMapping("/Delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        var visitors = visitorsService.getOne(id);

        if (visitors.isEmpty()){
            fillModelWithAllVisitors(model);
            return "redirect:/Visitors";
        }

        model.addAttribute("title", "Удаление посетителя");
        model.addAttribute("visitor", visitors.get());
        return "Visitors/delete";
    }

    @PostMapping("/Delete/{id}")
    public String deleteConfirmed(Model model, @PathVariable("id") Long id){
        visitorsService.delete(id);
        fillModelWithAllVisitors(model);
        return "redirect:/Visitors";
    }
}
