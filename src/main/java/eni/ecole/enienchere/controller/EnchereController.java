package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.EnchereService;
import eni.ecole.enienchere.bo.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
public class EnchereController {

    private EnchereService enchereService;
    private ArticleService articleService;

    @GetMapping("/encheres")

    public String afficherEncheres(Model model, @RequestParam(name = "nom", required = true) String nom,
   @RequestParam(name = "categorie", required = true) int idcategorie) {


    List<String> categories = List.of("Ameublement", "Informatique", "Sport&Loisirs", "Vêtements");
    List<ArticleAVendre> encheres = articleService.getArticlesByCategorie(idcategorie);

        //TO DO
        model.addAttribute("encheres", encheres);
        model.addAttribute("nom", nom);
        model.addAttribute("categorie", idcategorie);
        model.addAttribute("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return "view-enchere";
    }

    @GetMapping("/enchere")
    public String ListEnchereController() {
        return "view-enchere";
    }

}



