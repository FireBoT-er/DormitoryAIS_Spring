package com.cws._7s_cr.Controllers;

import com.cws._7s_cr.Models.Rooms;
import com.cws._7s_cr.Models.Students;
import com.cws._7s_cr.Services.CheckInsOutsService;
import com.cws._7s_cr.Services.RoomsService;
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
@RequestMapping("/Rooms")
public class RoomsController {
    private RoomsService roomsService;
    private CheckInsOutsService checkInsOutsService;

    @Autowired
    public void setServices(RoomsService roomsService, CheckInsOutsService checkInsOutsService){
        this.roomsService = roomsService;
        this.checkInsOutsService = checkInsOutsService;
    }

    private void fillModelWithAllRooms(Model model){
        model.addAttribute("title", "Комнаты");

        var rooms = roomsService.getAll();
        rooms.sort(Comparator.comparing(Rooms::getRoomNumber));
        model.addAttribute("rooms", rooms);
    }

    private void fillModelWithRoom(Model model, String title, Rooms rooms){
        model.addAttribute("title", title);
        model.addAttribute("room", rooms);
    }

    @GetMapping({ "", "/Index" })
    public String index(Model model){
        fillModelWithAllRooms(model);
        return "Rooms/index";
    }

    @GetMapping("/Details/{id}")
    public String details(Model model, @PathVariable("id") Long id){
        var rooms = roomsService.getOne(id);

        if (rooms.isEmpty()){
            fillModelWithAllRooms(model);
            return "redirect:/Rooms";
        }

        Rooms room = rooms.get();

        List<Students> residents = new ArrayList<>();
        for (var item: checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNow(room)){
            residents.add(item.getStudent());
        }
        model.addAttribute("residents", residents);

        fillModelWithRoom(model, "Информация о комнате", room);
        return "Rooms/details";
    }

    @GetMapping("/Create")
    public String create(Model model){
        fillModelWithRoom(model, "Добавить комнату", new Rooms());
        return "Rooms/create";
    }

    @PostMapping("/Create")
    public String create(Model model, @ModelAttribute("room") @Valid Rooms rooms, BindingResult result){
        if (result.hasErrors()) {
            fillModelWithRoom(model, "Добавить комнату", rooms);
            return "Rooms/create";
        }

        if (roomsService.findByRoomNumber(rooms.getRoomNumber()).isPresent()){
            fillModelWithRoom(model, "Добавить комнату", rooms);
            model.addAttribute("roomNumberError", "Комната с таким номером уже существует");
            return "Rooms/create";
        }

        if (rooms.getRoomNumber()%100 == 0){
            fillModelWithRoom(model, "Добавить комнату", rooms);
            model.addAttribute("roomNumberError", "Такой номер недопустим");
            return "Rooms/create";
        }

        roomsService.createOrUpdate(rooms);
        fillModelWithAllRooms(model);
        return "redirect:/Rooms";
    }

    @GetMapping("/Edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        var rooms = roomsService.getOne(id);

        if (rooms.isEmpty()){
            fillModelWithAllRooms(model);
            return "redirect:/Rooms";
        }

        Rooms room = rooms.get();

        fillModelWithRoom(model, "Изменить сведения о комнате", room);
        model.addAttribute("originalNumber", room.getRoomNumber());
        return "Rooms/edit";
    }

    @PostMapping("/Edit/{id}")
    public String edit(Model model, @ModelAttribute("room") @Valid Rooms rooms, BindingResult result, Integer originalNumber){
        if (result.hasErrors()) {
            fillModelWithRoom(model, "Добавить комнату", rooms);
            model.addAttribute("originalNumber", originalNumber);
            return "Rooms/edit";
        }

        if (rooms.getRoomNumber() != originalNumber){
            if (roomsService.findByRoomNumber(rooms.getRoomNumber()).isPresent()){
                fillModelWithRoom(model, "Изменить сведения о комнате", rooms);
                model.addAttribute("originalNumber", originalNumber);
                model.addAttribute("roomNumberError", "Комната с таким номером уже существует");
                return "Rooms/edit";
            }
        }

        if (rooms.getRoomNumber()%100 == 0){
            fillModelWithRoom(model, "Изменить сведения о комнате", rooms);
            model.addAttribute("originalNumber", originalNumber);
            model.addAttribute("roomNumberError", "Такой номер недопустим");
            return "Rooms/edit";
        }

        if (rooms.getBeds() < checkInsOutsService.findByRoomAndCheckOutDateGreaterThanEqualDateNow(rooms).size()){
            model.addAttribute("title", "Изменить сведения о комнате");
            model.addAttribute("room", rooms);
            model.addAttribute("originalNumber", originalNumber);

            model.addAttribute("bedsError", "Количество мест не может быть меньше количества заселённых студентов");
            return "Rooms/edit";
        }

        roomsService.createOrUpdate(rooms);
        fillModelWithAllRooms(model);
        return "redirect:/Rooms";
    }

    @GetMapping("/Delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        var rooms = roomsService.getOne(id);

        if (rooms.isEmpty()){
            fillModelWithAllRooms(model);
            return "redirect:/Rooms";
        }

        model.addAttribute("title", "Удаление комнаты");
        model.addAttribute("room", rooms.get());
        return "Rooms/delete";
    }

    @PostMapping("/Delete/{id}")
    public String deleteConfirmed(Model model, @PathVariable("id") Long id){
        if (checkInsOutsService.findByRoom(roomsService.getOne(id).get()).size()>0){
            model.addAttribute("title", "Удаление комнаты");
            model.addAttribute("bedsError", "Нельзя удалить комнату, к которой приписаны студенты");
            model.addAttribute("room", roomsService.getOne(id).get());
            return "Rooms/delete";
        }

        roomsService.delete(id);

        fillModelWithAllRooms(model);
        return "redirect:/Rooms";
    }
}
