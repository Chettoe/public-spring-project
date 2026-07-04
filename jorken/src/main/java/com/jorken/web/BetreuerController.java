package com.jorken.web;

import com.jorken.annotations.BetreuerOnly;
import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.domain.Betreuer.ThemaView;
import com.jorken.service.betreuer.BetreuerService;
import com.jorken.service.exception.ObjektNotFoundException;
import com.jorken.web.form.EditBetreuerForm;
import com.jorken.web.form.ThemaForm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/jorken/betreuer")
@BetreuerOnly
public class BetreuerController {

    BetreuerService service;

    public BetreuerController(BetreuerService service) {
        this.service = service;
    }

    @GetMapping
    public String profile() {
        return "redirect:/jorken/betreuer/profile";
    }

    @GetMapping("/profile")
    public String loadProfile(Model model, @AuthenticationPrincipal OAuth2User token) {
        String gitHubName = token.getAttribute("login");

        Betreuer betreuer = service.betreuerByGitHubName(gitHubName);
        model.addAttribute("betreuer", betreuer);


        return "betreuer/profile";
    }

    @GetMapping("/edit_profile")
    public String editProfileGet(@AuthenticationPrincipal OAuth2User token, Model model) {
        String current = token.getAttribute("login");
        Betreuer betreuer = service.betreuerByGitHubName(current);
        model.addAttribute("betreuer", betreuer);

        String tags = StringConverter.setToString(betreuer.getTags(), "#");
        model.addAttribute("tags", tags);

        String links = StringConverter.setToString(betreuer.getLinks(), ",");
        model.addAttribute("links", links);

        return "betreuer/edit_profile";
    }

    @PostMapping("/edit_profile")
    public String editProfilePost(@AuthenticationPrincipal OAuth2User token,
                                  @Valid @ModelAttribute EditBetreuerForm form,
                                  BindingResult result) {

        if(result.hasErrors()) return "betreuer/edit_profile";

        String current = token.getAttribute("login");

        Betreuer betreuer = service.betreuerByGitHubName(current);
        service.addBeschreibung(betreuer.getId(), form.getBeschreibung());
        service.setEmail(betreuer.getId(), form.getEmail());
        service.setPhoneNumber(betreuer.getId(), form.getPhoneNumber());
        service.setLinks(betreuer.getId(), StringConverter.stringToSet(form.getLinks(), ","));
        service.setTags(betreuer.getId(), StringConverter.stringToSet(form.getTags(), "#"));

        return "redirect:profile";
    }

    @GetMapping("/create_thema")
    public String createThemaGet() {

        return "betreuer/create_thema";
    }

    @PostMapping("/create_thema")
    public String createThemaPost(@AuthenticationPrincipal OAuth2User token,
                                  @RequestParam("markdown") MultipartFile mk,
                                  @Valid @ModelAttribute ThemaForm form,
                                  BindingResult result
                                  ) throws IOException {

        if(result.hasErrors()) return "betreuer/create_thema";

        String current = token.getAttribute("login");
        String markdown = new String(mk.getBytes(), StandardCharsets.UTF_8);

        Set<String> reqSet = StringConverter.stringToSet(form.getRequirements(), "#");
        Set<String> linkSet = StringConverter.stringToSet(form.getLinks(), ",");


        Betreuer betreuer = service.betreuerByGitHubName(current);
        service.addThema(betreuer.getId(), form.getName(), markdown, reqSet, linkSet);

        return "redirect:profile";
    }

    @GetMapping("/edit_thema/{fachId}")
    public String editThemaGet(@PathVariable UUID fachId,
                               @AuthenticationPrincipal OAuth2User token,
                               Model model) {
        String current = token.getAttribute("login");
        Betreuer b = service.betreuerByGitHubName(current);

        ThemaView thema = service.themaWithUUID(b.getId(), fachId);

        String req = StringConverter.setToString(thema.requirements(), "#");
        String links = StringConverter.setToString(thema.links(), ",");

        model.addAttribute("thema", thema);
        model.addAttribute("req", req);
        model.addAttribute("links", links);

        return "betreuer/edit_thema";
    }

    @PostMapping("/edit_thema")
    public String editThemaPost(@AuthenticationPrincipal OAuth2User token,
                                @RequestParam UUID fachId,
                                @RequestParam("markdown") MultipartFile mk,
                                @Valid @ModelAttribute ThemaForm form,
                                BindingResult result
                                ) throws IOException{

        if(result.hasErrors()) return "betreuer/edit_thema";

        String current = token.getAttribute("login");
        Betreuer b = service.betreuerByGitHubName(current);
        String markdown = new String(mk.getBytes(), StandardCharsets.UTF_8);

        Set<String> reqSet = StringConverter.stringToSet(form.getRequirements(), "#");
        Set<String> linkSet = StringConverter.stringToSet(form.getLinks(), ",");

        ThemaView thema = new ThemaView(fachId, form.getName(), markdown, reqSet, linkSet);

        service.updateThema(b.getId(), thema);

        return "redirect:profile";
    }
}