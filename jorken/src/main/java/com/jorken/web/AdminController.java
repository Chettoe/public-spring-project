package com.jorken.web;

import com.jorken.annotations.AdminOnly;
import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.service.betreuer.BetreuerService;
import com.jorken.web.form.RegistrationBetreuerForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AdminOnly
@Controller
@RequestMapping("/jorken/admin")
public class AdminController {

    BetreuerService service;

    public AdminController(BetreuerService service) {
        this.service = service;
    }

    @GetMapping
    public String admin(Model model) {

        Collection<Betreuer> all = service.allBetreuer();
        model.addAttribute("betreuer", all);

        return "admin/admin";
    }

    @GetMapping("/add")
    public String addBetreuerPageAdmin(){
        return "admin/addBetreuerForm";
    }

    @PostMapping("/add")
    public String postBetreuer(@Valid @ModelAttribute RegistrationBetreuerForm form, BindingResult result) {

        if(result.hasErrors()) return "admin/addBetreuerForm";

        service.addBetreuer(
                form.getFirstName(),
                form.getLastName(),
                form.getEmail(),
                form.getPhoneNumber(),
                form.getGitHubName()
        );

        return "redirect:/jorken/admin";
    }
}
