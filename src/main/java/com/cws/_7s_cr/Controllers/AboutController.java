package com.cws._7s_cr.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/About")
public class AboutController {
    @GetMapping({ "", "/Index" })
    public String index(Model model){
        model.addAttribute("title", "Об авторе");
        return "About/index";
    }
}
