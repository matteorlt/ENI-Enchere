package eni.ecole.enienchere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller

public class NouvelleVenteController {

    @RequestMapping("/ventes")


    @GetMapping("/nouvelle")
        public String afficherFormulaireVente(Model model, Principal principal) {
            model.addAttribute("article", new Article());
            model.addAttribute("utilisateur", utilisateurService.findByPseudo(principal.getName()));
            model.addAttribute("categories", categorieService.getAll());
            model.addAttribute("adresses", adresseService.getAllAdressesENI());
            return "nouvelleVente";
        }

        // @PostMapping("/nouvelle")
        // public String traiterFormulaire
        // Switch case????



    }








}
