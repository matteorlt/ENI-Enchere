package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bo.Enchere;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EnchereController {

    @Controller

    @GetMapping("/encheres")

    public String afficherEncheres(Model model, @RequestParam(name = "nom", required = true) String nom,
     @RequestParam(name = "categorie", required = true) String categorie);

      {

        List<String> categories = List.of("Ameublement", "Informatique", "Sport&Loisirs", "Vêtements");
        List<Enchere> encheres = filtrerEncheres(nom, categorie); //TO DO

        model.addAttribute("categories", categories);
        model.addAttribute("encheres", encheres);
        model.addAttribute("nom", nom);
        model.addAttribute("categorie", categorie);
        model.addAttribute("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return "encheres";
    }

    private List<Enchere> filtrerEncheres(String nom, String categorie) {
        // TO DO simu

        List<Enchere> all = List.of(
                new Enchere("Disque Dur", 1, "01/06/202X", "coach_toto"),
                new Enchere("Baskets blanches (femme)", 3, "01/10/202X", "coach_titi"),
                new Enchere("Combinaison Spider-Man", 8, "12/06/202X", "venom"),
                new Enchere("Paire de chaussettes", 1, "01/09/202X", "chausse_pieds")
        );


    }
}

