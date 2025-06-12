package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.EnchereService;
import eni.ecole.enienchere.bo.*;
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

        return "encheres";
    }}

        //Adresse adresse = new Adresse(1L, "12 rue des Lilas", "75001", "Paris", false);
        // Utilisateur utilisateur = new Utilisateur("Tom453", "Tom", "Dupont","06 43 43 53 53" ,"tom@gmail.com", "$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu", true, 15, adresse);
        //Enchere enchere = new Enchere(LocalDateTime.now(), 1, article, utilisateur);
        // ArticleAVendre articleAVendre = new ArticleAVendre(no_article:1; nom_article:PC, description:, date_debut_enchere:, date_fin_enchere:
        // statut:, prix_initial:, prix_vente:, vendeur:, categorie:, adresse_retrait:);

        //List<Enchere> all = List.of(
        //new Enchere(LocalDateTime.now(), 1, "Disque Dur", "coach_toto"),
        //new Enchere(LocalDateTime.now(), 3, "Baskets blanches (femme)", "coach_titi"),
        //new Enchere(LocalDateTime.now(), 8, "Combinaison Spider-Man", "venom"),
        //new Enchere(LocalDateTime.now(), 1, "Paire de chaussettes", "chausse_pieds")
        //);



