package eni.ecole.enienchere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour les pages du footer
 */
@Controller
public class FooterController {

    @GetMapping("/mentions-legales")
    public String mentionsLegales(Model model) {
        model.addAttribute("pageTitle", "Mentions Légales");
        return "view-mentions-legales";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Contact");
        return "view-contact";
    }

    @GetMapping("/a-propos")
    public String aPropos(Model model) {
        model.addAttribute("pageTitle", "À Propos");
        return "view-a-propos";
    }
} 