package com.jorken.web;

import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.domain.Betreuer.ThemaView;
import com.jorken.service.betreuer.BetreuerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/jorken")
public class UserController {

    BetreuerService service;

    public UserController(BetreuerService service) {
        this.service = service;
    }

    @GetMapping
    public String start() {
        return "user/search";
    }

    @PostMapping
    public String search(@RequestParam String search, @RequestParam String filter, Model model) {
        if(filter.equals("betreuer")) {
            model.addAttribute("res_betreuer", service.searchBetreuer(search));
            return "user/result_betreuer";
        }
        if(filter.equals("themen")) {
            model.addAttribute("res_themen", service.searchThemen(search));
            return "user/result_themen";
        }
        return "user/nothing_found";
    }


    @GetMapping("/result_betreuer/{fachId}")
    public String profile(@PathVariable UUID fachId, Model model) {
        Betreuer b = service.betreuer(fachId);
        model.addAttribute("betreuer", b);
        List<ThemaView> themen = service.allThemen(fachId);
        model.addAttribute("themen", themen);
        return "user/profile";
    }

    @GetMapping("/result_themen/{fachId}")
    public String thema(@PathVariable UUID fachId, Model model) {
        ThemaView thema = service.themaById(fachId);
        model.addAttribute("thema", thema);

        Betreuer b = service.betreuerByThema(fachId);
        model.addAttribute("betreuer", b);

        return "user/thema";
    }
}
