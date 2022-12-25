package com.cws._7s_cr.Controllers;

import com.cws._7s_cr.Miscellaneous.FormatCheck;
import com.cws._7s_cr.Miscellaneous.StudentsToView;
import com.cws._7s_cr.Models.*;
import com.cws._7s_cr.Services.*;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/Students")
public class StudentsController {
    private StudentsService studentsService;
    private CheckInsOutsService checkInsOutsService;
    private ViolationsService violationsService;
    private VisitorsService visitorsService;
    private RoomsService roomsService;
    private IssuedInventoriesService issuedInventoriesService;

    @Autowired
    public void setServices(StudentsService studentsService, CheckInsOutsService checkInsOutsService,
                            ViolationsService violationsService, VisitorsService visitorsService,
                            RoomsService roomsService, IssuedInventoriesService issuedInventoriesService){
        this.studentsService = studentsService;
        this.checkInsOutsService = checkInsOutsService;
        this.violationsService = violationsService;
        this.visitorsService = visitorsService;
        this.roomsService = roomsService;
        this.issuedInventoriesService = issuedInventoriesService;
    }

    @Autowired
    private HttpSession httpSession;

    private void fillModelWithAllStudents(Model model){
        model.addAttribute("title", "Студенты");

        model.addAttribute("residentsOnly", httpSession.getAttribute("residentsOnly"));

        List<StudentsToView> toView = new ArrayList<>();

        List<Students> students = new ArrayList<>();
        if (httpSession.getAttribute("residentsOnly") == null){
            students = studentsService.getAll();
        }
        else{
            for (var item: checkInsOutsService.findByCheckOutDateGreaterThanEqualDateNow()){
                students.add(item.getStudent());
            }
        }

        for (var item: students){
            var checkInsOuts = checkInsOutsService.findByStudent(item);
            toView.add(new StudentsToView(item,
                    checkInsOuts.get().getRoom(),
                    checkInsOuts.get().getCheckInDate(),
                    checkInsOuts.get().getCheckOutDate()));
        }

        toView.sort(Comparator.comparing(studentsToView -> studentsToView.getStudent().getSurname()));
        model.addAttribute("studentsToView", toView);
    }

    private void fillModelWithStudent(Model model, String title, Students students){
        model.addAttribute("title", title);
        model.addAttribute("student", students);
    }

    @GetMapping({ "", "/Index" })
    public String index(Model model){
        fillModelWithAllStudents(model);
        return "Students/index";
    }

    @GetMapping("/Details/{id}")
    public String details(Model model, @PathVariable("id") Long id) throws IOException {
        var students = studentsService.getOne(id);

        if (students.isEmpty()){
            fillModelWithAllStudents(model);
            return "redirect:/Students";
        }

        Students student = students.get();
        student.getViolations().sort(Comparator.comparing(Violations::getDateVl).thenComparing(Violations::getTimeVl).reversed());
        student.getVisitors().sort(Comparator.comparing(Visitors::getDateVs).thenComparing(Visitors::getTimeVs).reversed());

        var roommatesCIO = checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNow(checkInsOutsService.findByStudent(student).get().getRoom());
        List<Students> roommates = new ArrayList<>();
        for (var item: roommatesCIO){
            roommates.add(item.getStudent());
        }
        roommates.remove(student);
        model.addAttribute("roommates", roommates);

        var issuedInventories = issuedInventoriesService.findByStudent(student);
        issuedInventories.sort(Comparator.comparing(IssuedInventory::getIssueDate).reversed());
        model.addAttribute("issuedInventories", issuedInventories);

        Path fileNameAndPath = Paths.get(System.getProperty("user.dir")+"/UserFiles/Photo/Students/", student.getPhotoFileName());
        model.addAttribute("photo", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(fileNameAndPath)));

        model.addAttribute("title", "Информация о студенте");

        var checkInsOuts = checkInsOutsService.findByStudent(student);
        model.addAttribute("studentToView", new StudentsToView(student, checkInsOuts.get().getRoom(), checkInsOuts.get().getCheckInDate(), checkInsOuts.get().getCheckOutDate()));
        model.addAttribute("isEvicted", checkInsOuts.get().getCheckOutDate().isBefore(LocalDate.now()));
        return "Students/details";
    }

    @GetMapping("/ShowPhoto/{id}")
    public String showPhoto(Model model, @PathVariable("id") Long studentID) throws IOException {
        var students = studentsService.getOne(studentID).get();

        model.addAttribute("title", "студента "+ students.getSurname() + " " + students.getName() + " " + students.getPatronymic());

        Path fileNameAndPath = Paths.get(System.getProperty("user.dir")+"/UserFiles/Photo/Students/", students.getPhotoFileName());
        model.addAttribute("photo", "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(fileNameAndPath)));

        return "Shared/showPhoto";
    }

    @GetMapping("/Create")
    public String create(Model model){
        fillModelWithStudent(model, "Данные студента", new Students());
        return "Students/create";
    }

    @PostMapping("/Create")
    public String create(Model model, @ModelAttribute("student") @Valid Students students, BindingResult result, @RequestParam MultipartFile upload) throws IOException {
        if (result.hasErrors()) {
            fillModelWithStudent(model, "Данные студента", students);
            return "Students/create";
        }

        byte[] photo = upload.getBytes();

        if (photo.length==0){
            fillModelWithStudent(model, "Данные студента", students);
            model.addAttribute("photoFileNameError", "Поле должно быть заполнено");
            return "Students/create";
        }

        if (!FormatCheck.isImage(photo)){
            fillModelWithStudent(model, "Данные студента", students);
            model.addAttribute("photoFileNameError", "Выбранный файл не является изображением");
            return "Students/create";
        }

        List<Rooms> rooms = new ArrayList<>();
        for (var item: roomsService.getAll()){
            if (item.getBeds() > checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNow(item).size()){
                if (checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNowAndStudent_Sex(item, students.getSex()).size()>0){
                    rooms.add(item);
                }
                else if (checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNow(item).size() == 0){
                    rooms.add(item);
                }
            }
        }
        rooms.sort(Comparator.comparing(Rooms::getRoomNumber));

        model.addAttribute("title", "Выберите комнату");
        model.addAttribute("surname", students.getSurname());
        model.addAttribute("name", students.getName());
        model.addAttribute("patronymic", students.getPatronymic());
        model.addAttribute("photo", Base64.getEncoder().encodeToString(photo));
        model.addAttribute("sex", students.getSex());
        model.addAttribute("birthday", students.getBirthday());
        model.addAttribute("recordBookNumber", students.getRecordBookNumber());
        model.addAttribute("rooms", rooms);
        return "Students/chooseRoom";
    }

    @PostMapping("/Create/ChooseRoom")
    public String chooseRoom(Model model, String surname, String name, String patronymic,
                             Boolean sex, String birthday, String recordBookNumber, Long roomId, String photo){
        model.addAttribute("title", "Информация о заселении");
        model.addAttribute("surname", surname);
        model.addAttribute("name", name);
        model.addAttribute("patronymic", patronymic);
        model.addAttribute("photo", photo);
        model.addAttribute("sex", sex);
        model.addAttribute("birthday", birthday);
        model.addAttribute("recordBookNumber", recordBookNumber);
        model.addAttribute("roomId", roomId);
        model.addAttribute("checkInsOuts", new CheckInsOuts());
        return "Students/chooseDates";
    }

    @PostMapping("/Create/ChooseDates")
    public String chooseDates(Model model, String surname, String name, String patronymic,
                              Boolean sex, String birthday, String recordBookNumber, Long roomId,
                              @ModelAttribute("checkInsOuts") CheckInsOuts checkInsOuts, String photo) throws IOException {
        if (checkInsOuts.getCheckInDate() == null) {
            model.addAttribute("checkInDateError", "Поле должно быть заполнено");

            model.addAttribute("title", "Информация о заселении");
            model.addAttribute("surname", surname);
            model.addAttribute("name", name);
            model.addAttribute("patronymic", patronymic);
            model.addAttribute("photo", photo);
            model.addAttribute("sex", sex);
            model.addAttribute("birthday", birthday);
            model.addAttribute("recordBookNumber", recordBookNumber);
            model.addAttribute("roomId", roomId);
            model.addAttribute("checkInsOuts", checkInsOuts);
            return "Students/chooseDates";
        }

        String path = System.getProperty("user.dir")+"/UserFiles/Photo/Students/";

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
        Files.write(fileNameAndPath, Base64.getDecoder().decode(photo));

        Students student = new Students();
        student.setSurname(surname);
        student.setName(name);
        student.setPatronymic(patronymic);
        student.setSex(sex);
        student.setBirthday(LocalDate.parse(birthday, DateTimeFormatter.ISO_LOCAL_DATE));
        student.setRecordBookNumber(recordBookNumber);
        student.setPhotoFileName(Long.toString(fileNumber));

        studentsService.createOrUpdate(student);

        checkInsOuts.setStudent(studentsService.getLast().get());
        checkInsOuts.setRoom(roomsService.getOne(roomId).get());
        checkInsOuts.setCheckOutDate(checkInsOuts.getCheckInDate().plusYears(4));

        checkInsOutsService.createOrUpdate(checkInsOuts);

        fillModelWithAllStudents(model);
        return "redirect:/Students";
    }

    @GetMapping("/Edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        var students = studentsService.getOne(id);

        if (students.isEmpty()){
            fillModelWithAllStudents(model);
            return "redirect:/Students";
        }

        Students student = students.get();

        fillModelWithStudent(model, "Изменить данные студента", student);
        return "Students/edit";
    }

    @PostMapping("/Edit/{id}")
    public String edit(Model model, @ModelAttribute("student") @Valid Students students, BindingResult result, @RequestParam MultipartFile upload) throws IOException {
        if (result.hasErrors()) {
            fillModelWithStudent(model, "Изменить данные студента", students);
            return "Students/edit";
        }

        byte[] photo = upload.getBytes();

        if (photo.length != 0){
            if (!FormatCheck.isImage(photo)){
                fillModelWithStudent(model, "Изменить данные студента", students);
                model.addAttribute("photoFileNameError", "Выбранный файл не является изображением");
                return "Students/edit";
            }
        }
        else{
            photo = new byte[] { 44 };
        }

        List<Rooms> rooms = new ArrayList<>();
        for (var item: roomsService.getAll()){
            if (item.getBeds() > checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNow(item).size()){
                if (checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNowAndStudent_Sex(item, students.getSex()).size()>0){
                    rooms.add(item);
                }
                else if (checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNow(item).size() == 0){
                    rooms.add(item);
                }
            }
        }
        rooms.sort(Comparator.comparing(Rooms::getRoomNumber));

        Long roomId = checkInsOutsService.findByStudent(students).get().getRoom().getId();
        Rooms currentRoom = roomsService.getOne(roomId).get();

        rooms.remove(currentRoom);
        rooms.add(0, currentRoom);

        model.addAttribute("title", "Выберите комнату");
        model.addAttribute("id", students.getId());
        model.addAttribute("surname", students.getSurname());
        model.addAttribute("name", students.getName());
        model.addAttribute("patronymic", students.getPatronymic());
        model.addAttribute("photo", Base64.getEncoder().encodeToString(photo));
        model.addAttribute("photoFileName", students.getPhotoFileName());
        model.addAttribute("sex", students.getSex());
        model.addAttribute("birthday", students.getBirthday());
        model.addAttribute("recordBookNumber", students.getRecordBookNumber());
        model.addAttribute("roomId", roomId);
        model.addAttribute("rooms", rooms);
        return "Students/editRoom";
    }

    @PostMapping("/Edit/{id}/EditRoom")
    public String editRoom(Model model, Long id, String surname, String name, String patronymic,
                             Boolean sex, String birthday, String recordBookNumber, Long roomId, String photo, String photoFileName){
        model.addAttribute("title", "Информация о проживании");
        model.addAttribute("id", id);
        model.addAttribute("surname", surname);
        model.addAttribute("name", name);
        model.addAttribute("patronymic", patronymic);
        model.addAttribute("photo", photo);
        model.addAttribute("photoFileName", photoFileName);
        model.addAttribute("sex", sex);
        model.addAttribute("birthday", birthday);
        model.addAttribute("recordBookNumber", recordBookNumber);
        model.addAttribute("roomId", roomId);
        model.addAttribute("checkInsOuts", checkInsOutsService.findByStudentId(id));
        return "Students/editDates";
    }

    @PostMapping("/Edit/{id}/EditDates")
    public String editDates(Model model, Long id, String surname, String name, String patronymic,
                              Boolean sex, String birthday, String recordBookNumber, Long roomId,
                              @ModelAttribute("checkInsOuts") CheckInsOuts checkInsOuts, String photo, String photoFileName) throws IOException {
        if (checkInsOuts.getCheckInDate() == null || checkInsOuts.getCheckOutDate() == null) {
            if (checkInsOuts.getCheckInDate() == null){
                model.addAttribute("checkInDateError", "Поле должно быть заполнено");
            }
            else{
                model.addAttribute("checkOutDateError", "Поле должно быть заполнено");
            }

            model.addAttribute("title", "Информация о проживании");
            model.addAttribute("id", id);
            model.addAttribute("surname",surname);
            model.addAttribute("name",name);
            model.addAttribute("patronymic", patronymic);
            model.addAttribute("photo", photo);
            model.addAttribute("photoFileName", photoFileName);
            model.addAttribute("sex", sex);
            model.addAttribute("birthday", birthday);
            model.addAttribute("recordBookNumber", recordBookNumber);
            model.addAttribute("roomId", roomId);
            model.addAttribute("checkInsOuts", checkInsOuts);
            return "Students/editDates";
        }

        if (checkInsOuts.getCheckInDate().isAfter(checkInsOuts.getCheckOutDate())){
            model.addAttribute("checkOutDateError", "Дата заселения не может быть позже даты выселения");

            model.addAttribute("title", "Информация о проживании");
            model.addAttribute("id", id);
            model.addAttribute("surname",surname);
            model.addAttribute("name",name);
            model.addAttribute("patronymic", patronymic);
            model.addAttribute("photo", photo);
            model.addAttribute("photoFileName", photoFileName);
            model.addAttribute("sex", sex);
            model.addAttribute("birthday", birthday);
            model.addAttribute("recordBookNumber", recordBookNumber);
            model.addAttribute("roomId", roomId);
            model.addAttribute("checkInsOuts", checkInsOuts);
            return "Students/editDates";
        }

        Students student = new Students();
        student.setId(id);
        student.setSurname(surname);
        student.setName(name);
        student.setPatronymic(patronymic);
        student.setSex(sex);
        student.setBirthday(LocalDate.parse(birthday, DateTimeFormatter.ISO_LOCAL_DATE));
        student.setRecordBookNumber(recordBookNumber);
        student.setPhotoFileName(photoFileName);

        byte[] photoArr = Base64.getDecoder().decode(photo);
        if (photoArr.length > 1){
            String path = System.getProperty("user.dir")+"/UserFiles/Photo/Students/";

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
            Files.write(fileNameAndPath, photoArr);

            student.setPhotoFileName(Long.toString(fileNumber));
        }

        studentsService.createOrUpdate(student);

        checkInsOuts.setStudent(studentsService.getOne(id).get());
        checkInsOuts.setRoom(roomsService.getOne(roomId).get());

        checkInsOutsService.createOrUpdate(checkInsOuts);

        fillModelWithAllStudents(model);
        return "redirect:/Students";
    }

    @GetMapping("/Delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        var students = studentsService.getOne(id);

        if (students.isEmpty()){
            fillModelWithAllStudents(model);
            return "redirect:/Students";
        }

        model.addAttribute("title", "Удаление студента");

        var checkInsOuts = checkInsOutsService.findByStudent(students.get());
        model.addAttribute("studentsToView", new StudentsToView(students.get(), checkInsOuts.get().getRoom(), checkInsOuts.get().getCheckInDate(), checkInsOuts.get().getCheckOutDate()));
        return "Students/delete";
    }

    @PostMapping("/Delete/{id}")
    public String deleteConfirmed(Model model, @PathVariable("id") Long id){
        Students student = studentsService.getOne(id).get();

        var violations = new ArrayList<>(student.getViolations());
        student.setViolations(null);
        studentsService.createOrUpdate(student);

        for (var violation : violations){
            var violators = violation.getStudents();
            violators.remove(student);

            if (violators.size() == 0){
                violation.setStudents(null);
                violationsService.delete(violation.getId());
            }
            else{
                violation.setStudents(violators);
                violationsService.createOrUpdate(violation);
            }
        }

        for (var item: issuedInventoriesService.findByStudent(student)){
            issuedInventoriesService.delete(item.getId());
        }

        checkInsOutsService.delete(checkInsOutsService.findByStudent(student).get().getId());

        for (var item: visitorsService.findByStudent(student)){
            visitorsService.delete(item.getId());
        }

        studentsService.delete(id);
        fillModelWithAllStudents(model);
        return "redirect:/Students";
    }

    @PostMapping("/ResidentsSwitch")
    public String residentsSwitch(Model model, Boolean residentsOnly){
        httpSession.setAttribute("residentsOnly", residentsOnly);
        fillModelWithAllStudents(model);
        return "redirect:/Students";
    }
}
