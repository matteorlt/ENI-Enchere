package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.EnchereService;
import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Categorie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller

public class NouvelleVenteController {

    private ArticleService articleService;
    private EnchereService enchereService;
    private UtilisateurService utilisateurService;


    @RequestMapping("/ventes")


    @GetMapping("/nouvelle")
        public String afficherFormulaireVente(Model model, Principal principal) {
            model.addAttribute("article", new ArticleAVendre());
            model.addAttribute("utilisateur", utilisateurService.consulterUtilisateurParPseudo(principal.getName()));
            return "nouvelleVente";
        }

        // @PostMapping("/nouvelle")
        // public String traiterFormulaire
        // Switch case????



    }









