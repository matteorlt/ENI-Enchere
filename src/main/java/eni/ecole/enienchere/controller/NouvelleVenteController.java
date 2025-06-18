package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.CategorieService;
import eni.ecole.enienchere.bll.EnchereService;
import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Categorie;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class NouvelleVenteController {

    private final ArticleService articleService;
    private final EnchereService enchereService;
    private final UtilisateurService utilisateurService;
    private final CategorieService categorieService;

    @Autowired
    public NouvelleVenteController(ArticleService articleService, 
                                 EnchereService enchereService, 
                                 UtilisateurService utilisateurService,
                                 CategorieService categorieService) {
        this.articleService = articleService;
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;
        this.categorieService = categorieService;
    }

    @GetMapping("/vendre")
    public String afficherFormulaireVente(Model model, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }

            ArticleAVendre article = new ArticleAVendre();
            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(principal.getName());
            
            if (utilisateur == null) {
                model.addAttribute("error", "Utilisateur non trouvé");
                return "error";
            }

            model.addAttribute("article", article);
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("categories", categorieService.getAllCategories());
            model.addAttribute("adressesENI", utilisateurService.getAdressesENI());
            return "view-nouvelle-vente";
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue lors du chargement du formulaire : " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/vendre")
    public String traiterFormulaire(@RequestParam("nomArticle") String nomArticle,
                                  @RequestParam("categorie") Integer categorieId,
                                  @RequestParam("description") String description,
                                  @RequestParam("miseAPrix") Integer miseAPrix,
                                  @RequestParam("dateDebut") String dateDebutStr,
                                  @RequestParam("dateFin") String dateFinStr,
                                  @RequestParam("retrait") Long adresseRetraitId,
                                  @RequestParam("action") String action,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        
        if (principal == null) {
            return "redirect:/login";
        }

        if ("annuler".equals(action)) {
            return "redirect:/";
        }
        
        if ("annulerVente".equals(action)) {
            return "redirect:/vendre";
        }

        try {
            // Validation des données
            if (nomArticle == null || nomArticle.trim().isEmpty()) {
                throw new IllegalArgumentException("Le nom de l'article est requis");
            }
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("La description est requise");
            }
            if (miseAPrix <= 0) {
                throw new IllegalArgumentException("La mise à prix doit être supérieure à 0");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDebut = dateFormat.parse(dateDebutStr);
            Date dateFin = dateFormat.parse(dateFinStr);
            Date aujourdhui = new Date();

            // Autoriser la création d'enchères pour aujourd'hui ou dans le futur
            if (dateDebut.before(aujourdhui)) {
                // Autoriser les dates d'aujourd'hui en comparant uniquement les dates (sans l'heure)
                java.util.Calendar calAujourd = java.util.Calendar.getInstance();
                calAujourd.setTime(aujourdhui);
                calAujourd.set(java.util.Calendar.HOUR_OF_DAY, 0);
                calAujourd.set(java.util.Calendar.MINUTE, 0);
                calAujourd.set(java.util.Calendar.SECOND, 0);
                calAujourd.set(java.util.Calendar.MILLISECOND, 0);
                
                java.util.Calendar calDebut = java.util.Calendar.getInstance();
                calDebut.setTime(dateDebut);
                calDebut.set(java.util.Calendar.HOUR_OF_DAY, 0);
                calDebut.set(java.util.Calendar.MINUTE, 0);
                calDebut.set(java.util.Calendar.SECOND, 0);
                calDebut.set(java.util.Calendar.MILLISECOND, 0);
                
                if (calDebut.getTime().before(calAujourd.getTime())) {
                    throw new IllegalArgumentException("La date de début ne peut pas être dans le passé");
                }
            }
            if (dateFin.before(dateDebut)) {
                throw new IllegalArgumentException("La date de fin doit être après la date de début");
            }

            // Récupération et configuration du vendeur
            Utilisateur vendeur = utilisateurService.consulterUtilisateurParPseudo(principal.getName());
            if (vendeur == null) {
                throw new IllegalArgumentException("Utilisateur non trouvé");
            }

            // Configuration de la catégorie
            Categorie categorie = categorieService.getCategorieById(categorieId);
            if (categorie == null) {
                throw new IllegalArgumentException("Catégorie non trouvée");
            }

            // Configuration de l'adresse de retrait
            Adresse adresseRetrait = new Adresse();
            adresseRetrait.setNo_adresse(adresseRetraitId);

            // Création de l'article
            ArticleAVendre article = new ArticleAVendre();
            article.setNom_article(nomArticle);
            article.setDescription(description);
            article.setPrix_initial(miseAPrix);
            article.setDate_debut_enchere(dateDebut);
            article.setDate_fin_enchere(dateFin);
            article.setVendeur(vendeur);
            article.setCategorie(categorie);
            article.setAdresse_retrait(adresseRetrait);
            
            // Déterminer le statut selon la date de début
            Date maintenant = new Date();
            if (dateDebut.after(maintenant)) {
                // Si la date de début est dans le futur, l'enchère est en attente
                article.setStatut(0); // Statut en attente/bloqué
            } else {
                // Si la date de début est aujourd'hui ou dans le passé, l'enchère est active
                article.setStatut(1); // Statut actif
            }

            articleService.createArticle(article);
            
            // Rediriger vers la page d'ajout de photo avec l'ID de l'article créé
            redirectAttributes.addFlashAttribute("success", "Votre article a été mis en vente avec succès ! Vous pouvez maintenant ajouter une photo.");
            return "redirect:/ajouter-photo?articleId=" + article.getNo_article();
            
        } catch (ParseException e) {
            redirectAttributes.addFlashAttribute("error", "Format de date invalide : " + e.getMessage());
            return "redirect:/vendre";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vendre";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la création de l'article : " + e.getMessage());
            return "redirect:/vendre";
        }
    }
}









