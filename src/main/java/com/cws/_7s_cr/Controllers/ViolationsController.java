package com.cws._7s_cr.Controllers;

import com.cws._7s_cr.Miscellaneous.StudentsToCEView;
import com.cws._7s_cr.Models.Students;
import com.cws._7s_cr.Models.Violations;
import com.cws._7s_cr.Services.CheckInsOutsService;
import com.cws._7s_cr.Services.StudentsService;
import com.cws._7s_cr.Services.ViolationsService;
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
@RequestMapping("/Violations")
public class ViolationsController {
    private ViolationsService violationsService;
    private StudentsService studentsService;
    private CheckInsOutsService checkInsOutsService;

    @Autowired
    public void setServices(ViolationsService violationsService, StudentsService studentsService, CheckInsOutsService checkInsOutsService){
        this.violationsService = violationsService;
        this.studentsService = studentsService;
        this.checkInsOutsService = checkInsOutsService;
    }

    private void fillModelWithAllViolations(Model model){
        model.addAttribute("title", "Нарушения");

        var violations = violationsService.getAll();
        violations.sort(Comparator.comparing(Violations::getDateVl).thenComparing(Violations::getTimeVl).reversed());
        for (var violation: violations){
            violation.getStudents().sort(Comparator.comparing(Students::getSurname));
        }
        model.addAttribute("violations", violations);
    }

    private void fillModelWithViolation(Model model, String title, Violations violations){
        model.addAttribute("title", title);
        model.addAttribute("violation", violations);
    }

    @GetMapping({ "", "/Index" })
    public String index(Model model){
        fillModelWithAllViolations(model);
        return "Violations/index";
    }

    @GetMapping("/Details/{id}")
    public String details(Model model, @PathVariable("id") Long id){
        var violations = violationsService.getOne(id);

        if (violations.isEmpty()){
            fillModelWithAllViolations(model);
            return "redirect:/Violations";
        }

        Violations violation = violations.get();

        fillModelWithViolation(model, "Информация о нарушении", violation);
        return "Violations/details";
    }

    @GetMapping("/Create")
    public String create(Model model){
        fillModelWithViolation(model, "Сведения о нарушении", new Violations());
        return "Violations/create";
    }

    @PostMapping("/Create")
    public String create(Model model, @ModelAttribute("violation") @Valid Violations violations, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithViolation(model, "Сведения о нарушении", violations);
            return "Violations/create";
        }

        List<StudentsToCEView> toView = new ArrayList<>();

        for (var item: checkInsOutsService.findByCheckOutDateGreaterThanEqualDateNow()){
            toView.add(new StudentsToCEView(item.getStudent(), item.getRoom()));
        }

        toView.sort(Comparator.comparing(studentsToView -> studentsToView.getStudent().getSurname()));

        model.addAttribute("title", "Выберите нарушителей");
        model.addAttribute("description", violations.getDescription());
        model.addAttribute("punishment", violations.getPunishment());
        model.addAttribute("dateVl", violations.getDateVl());
        model.addAttribute("timeVl", violations.getTimeVl());
        model.addAttribute("studentsToCEView", toView);
        return "Violations/chooseViolators";
    }

    @PostMapping("/Create/ChooseViolators")
    public String chooseViolators(Model model, String description, String punishment, String dateVl, LocalTime timeVl, Long[] violators){
        if (violators == null) {
            model.addAttribute("submitButtonError", "Необходимо выбрать хотя бы одного студента");

            List<StudentsToCEView> toView = new ArrayList<>();

            for (var item: checkInsOutsService.findByCheckOutDateGreaterThanEqualDateNow()){
                toView.add(new StudentsToCEView(item.getStudent(), item.getRoom()));
            }

            toView.sort(Comparator.comparing(studentsToView -> studentsToView.getStudent().getSurname()));

            model.addAttribute("title", "Выберите нарушителей");
            model.addAttribute("description", description);
            model.addAttribute("punishment", punishment);
            model.addAttribute("dateVl", dateVl);
            model.addAttribute("timeVl", timeVl);
            model.addAttribute("studentsToCEView", toView);
            return "Violations/chooseViolators";
        }

        Violations violation = new Violations();
        violation.setDescription(description);
        violation.setPunishment(punishment);
        violation.setDateVl(LocalDate.parse(dateVl, DateTimeFormatter.ISO_LOCAL_DATE));
        violation.setTimeVl(timeVl);

        violationsService.createOrUpdate(violation);

        List<Students> studentsList = new ArrayList<>();
        for (var stId: violators){
            var student = studentsService.getOne(stId).get();

            var stViolations = student.getViolations();
            stViolations.add(violation);
            student.setViolations(stViolations);

            studentsList.add(student);
        }
        violation.setStudents(studentsList);

        violationsService.createOrUpdate(violation);

        fillModelWithAllViolations(model);
        return "redirect:/Violations";
    }

    @GetMapping("/Edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        var violations = violationsService.getOne(id);

        if (violations.isEmpty()){
            fillModelWithAllViolations(model);
            return "redirect:/Violations";
        }

        Violations violation = violations.get();

        fillModelWithViolation(model, "Изменить сведения о нарушении", violation);
        return "Violations/edit";
    }

    @PostMapping("/Edit/{id}")
    public String edit(Model model, @ModelAttribute("violation") @Valid Violations violations, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithViolation(model, "Изменить сведения о нарушении", violations);
            return "Violations/edit";
        }

        List<StudentsToCEView> toView = new ArrayList<>();

        List<Students> students = studentsService.getAll();

        var violators = violationsService.getOne(violations.getId()).get().getStudents();
        violators.sort(Comparator.comparing(Students::getSurname));
        Collections.reverse(violators);
        for (var item: violators){
            if (students.contains(item)){
                students.remove(item);
                students.add(0, item);
            }
        }

        for (var item: students){
            toView.add(new StudentsToCEView(item, checkInsOutsService.findByStudent(item).get().getRoom()));
        }

        model.addAttribute("title", "Выберите нарушителей");
        model.addAttribute("id", violations.getId());
        model.addAttribute("description", violations.getDescription());
        model.addAttribute("punishment", violations.getPunishment());
        model.addAttribute("dateVl", violations.getDateVl());
        model.addAttribute("timeVl", violations.getTimeVl());
        model.addAttribute("studentsToCEView", toView);
        model.addAttribute("violation", violationsService.getOne(violations.getId()).get());
        return "Violations/editViolators";
    }

    @PostMapping("/Edit/{id}/EditViolators")
    public String editViolators(Model model, Long id, String description, String punishment, String dateVl, LocalTime timeVl, Long[] violators){
        if (violators == null) {
            model.addAttribute("submitButtonError", "Необходимо выбрать хотя бы одного студента");

            List<StudentsToCEView> toView = new ArrayList<>();

            List<Students> students = studentsService.getAll();

            var violators_view = violationsService.getOne(id).get().getStudents();
            violators_view.sort(Comparator.comparing(Students::getSurname));
            Collections.reverse(violators_view);
            for (var item: violators_view){
                if (students.contains(item)){
                    students.remove(item);
                    students.add(0, item);
                }
            }

            for (var item: students){
                toView.add(new StudentsToCEView(item, checkInsOutsService.findByStudent(item).get().getRoom()));
            }

            model.addAttribute("title", "Выберите нарушителей");
            model.addAttribute("id", id);
            model.addAttribute("description", description);
            model.addAttribute("punishment", punishment);
            model.addAttribute("dateVl", dateVl);
            model.addAttribute("timeVl", timeVl);
            model.addAttribute("studentsToCEView", toView);
            model.addAttribute("violation", violationsService.getOne(id).get());
            return "Violations/editViolators";
        }

        Violations violation = new Violations();
        violation.setId(id);
        violation.setDescription(description);
        violation.setPunishment(punishment);
        violation.setDateVl(LocalDate.parse(dateVl, DateTimeFormatter.ISO_LOCAL_DATE));
        violation.setTimeVl(timeVl);

        var violationSaved = violationsService.getOne(id).get();
        for (var student : violationSaved.getStudents()){
            var stViolations = student.getViolations();
            stViolations.remove(violationSaved);

            student.setViolations(stViolations);
            studentsService.createOrUpdate(student);
        }

        violationsService.createOrUpdate(violation);

        List<Students> violatorsList = new ArrayList<>();
        for (var stId : violators){
            var violator = studentsService.getOne(stId).get();

            var vlViolations = violator.getViolations();
            vlViolations.add(violation);
            violator.setViolations(vlViolations);

            violatorsList.add(violator);
        }
        violation.setStudents(violatorsList);

        violationsService.createOrUpdate(violation);

        fillModelWithAllViolations(model);
        return "redirect:/Violations";
    }

    @GetMapping("/Delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        var violations = violationsService.getOne(id);

        if (violations.isEmpty()){
            fillModelWithAllViolations(model);
            return "redirect:/Violations";
        }

        model.addAttribute("title", "Удаление нарушения");
        var violation = violations.get();
        violation.getStudents().sort(Comparator.comparing(Students::getSurname));
        model.addAttribute("violation", violation);
        return "Violations/delete";
    }

    @PostMapping("/Delete/{id}")
    public String deleteConfirmed(Model model, @PathVariable("id") Long id){
        Violations violation = violationsService.getOne(id).get();

        var violators = new ArrayList<>(violation.getStudents());
        violation.setStudents(null);
        violationsService.createOrUpdate(violation);

        for (var violator : violators){
            var vlViolations = violator.getViolations();
            vlViolations.remove(violation);

            violator.setViolations(vlViolations);
            studentsService.createOrUpdate(violator);
        }

        violationsService.delete(id);
        fillModelWithAllViolations(model);
        return "redirect:/Violations";
    }
}
