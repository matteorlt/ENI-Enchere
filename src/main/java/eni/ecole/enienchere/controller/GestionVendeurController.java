package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.ArticleService;
import eni.ecole.enienchere.bll.PhotoService;
import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.ArticleAVendre;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class GestionVendeurController {

    private final ArticleService articleService;
    private final UtilisateurService utilisateurService;
    private final PhotoService photoService;

    @Autowired
    public GestionVendeurController(ArticleService articleService, UtilisateurService utilisateurService, PhotoService photoService) {
        this.articleService = articleService;
        this.utilisateurService = utilisateurService;
        this.photoService = photoService;
    }

    @GetMapping("/vendeur/gestion-articles")
    public String afficherGestionArticles(Model model, 
                                          @RequestParam(value = "recherche", required = false) String recherche,
                                          @RequestParam(value = "statut", required = false) String statut,
                                          Principal principal) {
        if (principal == null) {
            return "redirect:/connexion";
        }

        try {
            // Récupérer l'utilisateur connecté
            Utilisateur utilisateur = utilisateurService.getUtilisateurByPseudo(principal.getName());
            if (utilisateur == null) {
                return "redirect:/connexion";
            }

            // Récupérer UNIQUEMENT les articles de cet utilisateur
            List<ArticleAVendre> articles = articleService.getArticlesByVendeur(utilisateur.getPseudo());
            
            // Filtrer par nom d'article si spécifié
            if (recherche != null && !recherche.trim().isEmpty()) {
                articles = articles.stream()
                    .filter(a -> a.getNom_article().toLowerCase().contains(recherche.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Filtrer par statut temporel si spécifié
            if (statut != null && !statut.trim().isEmpty()) {
                java.util.Date maintenant = new java.util.Date();
                
                if ("debut".equals(statut)) {
                    // Filtrer les enchères qui n'ont pas encore commencé (date de début dans le futur)
                    articles = articles.stream()
                        .filter(a -> a.getDate_debut_enchere().after(maintenant))
                        .collect(java.util.stream.Collectors.toList());
                }
                else if ("actif".equals(statut)) {
                    // Filtrer les enchères en cours (date de début passée ET date de fin future)
                    articles = articles.stream()
                        .filter(a -> !a.getDate_debut_enchere().after(maintenant) && a.getDate_fin_enchere().after(maintenant))
                        .collect(java.util.stream.Collectors.toList());
                }
                else if ("termine".equals(statut)) {
                    // Filtrer les enchères terminées (date de fin passée)
                    articles = articles.stream()
                        .filter(a -> !a.getDate_fin_enchere().after(maintenant))
                        .collect(java.util.stream.Collectors.toList());
                }
            }
            
            model.addAttribute("articles", articles);
            model.addAttribute("recherche", recherche);
            model.addAttribute("statut", statut);
            model.addAttribute("vendeur", utilisateur);
            
            return "view-gestion-vendeur";
        } catch (Exception e) {
            model.addAttribute("erreur", "Erreur lors du chargement de vos articles : " + e.getMessage());
            return "view-gestion-vendeur";
        }
    }

    @PostMapping("/vendeur/gestion-articles/modifier/{articleId}")
    public String modifierArticle(@PathVariable Long articleId,
                                  @RequestParam(value = "nom", required = false) String nom,
                                  @RequestParam(value = "description", required = false) String description,
                                  @RequestParam(value = "prix", required = false) Integer prix,
                                  @RequestParam(value = "photo", required = false) MultipartFile photo,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) {
        if (principal == null) {
            return "redirect:/connexion";
        }

        try {
            // Vérifier que l'utilisateur est connecté
            Utilisateur utilisateur = utilisateurService.getUtilisateurByPseudo(principal.getName());
            if (utilisateur == null) {
                redirectAttributes.addFlashAttribute("erreur", "Utilisateur non trouvé");
                return "redirect:/vendeur/gestion-articles";
            }

            // Vérifier que l'article existe et appartient à l'utilisateur
            List<ArticleAVendre> articles = articleService.getArticleById(Math.toIntExact(articleId));
            if (articles.isEmpty()) {
                redirectAttributes.addFlashAttribute("erreur", "Article non trouvé");
                return "redirect:/vendeur/gestion-articles";
            }

            ArticleAVendre article = articles.get(0);
            
            // Vérifier que l'article appartient bien à l'utilisateur connecté
            if (!article.getVendeur().getPseudo().equals(utilisateur.getPseudo())) {
                redirectAttributes.addFlashAttribute("erreur", "Vous ne pouvez modifier que vos propres articles");
                return "redirect:/vendeur/gestion-articles";
            }

            boolean modificationEffectuee = false;

            // Modifier les informations de l'article
            if (nom != null && !nom.trim().isEmpty() && !nom.equals(article.getNom_article())) {
                article.setNom_article(nom.trim());
                modificationEffectuee = true;
            }

            if (description != null && !description.trim().isEmpty() && !description.equals(article.getDescription())) {
                article.setDescription(description.trim());
                modificationEffectuee = true;
            }

            if (prix != null && prix > 0 && !prix.equals(article.getPrix_initial())) {
                article.setPrix_initial(prix);
                modificationEffectuee = true;
            }

            // Sauvegarder les modifications de l'article
            if (modificationEffectuee) {
                articleService.updateArticle(article);
            }

            // Gérer la photo si fournie
            if (photo != null && !photo.isEmpty()) {
                try {
                                         String photoPath = photoService.savePhoto(photo, (long) Math.toIntExact(article.getNo_article()));
                    if (photoPath != null) {
                        article.setPhoto(photoPath);
                        articleService.updateArticle(article);
                        redirectAttributes.addFlashAttribute("succes", "Article et photo modifiés avec succès");
                    } else {
                        if (modificationEffectuee) {
                            redirectAttributes.addFlashAttribute("succes", "Article modifié avec succès, mais erreur lors de l'upload de la photo");
                        } else {
                            redirectAttributes.addFlashAttribute("erreur", "Erreur lors de l'upload de la photo");
                        }
                    }
                } catch (Exception e) {
                    if (modificationEffectuee) {
                        redirectAttributes.addFlashAttribute("succes", "Article modifié avec succès, mais erreur lors de l'upload de la photo : " + e.getMessage());
                    } else {
                        redirectAttributes.addFlashAttribute("erreur", "Erreur lors de l'upload de la photo : " + e.getMessage());
                    }
                }
            } else if (modificationEffectuee) {
                redirectAttributes.addFlashAttribute("succes", "Article modifié avec succès");
            } else {
                redirectAttributes.addFlashAttribute("info", "Aucune modification apportée");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", "Erreur lors de la modification : " + e.getMessage());
        }

        return "redirect:/vendeur/gestion-articles";
    }

    @PostMapping("/vendeur/gestion-articles/supprimer/{articleId}")
    public String supprimerArticle(@PathVariable Long articleId,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) {
        if (principal == null) {
            return "redirect:/connexion";
        }

        try {
            // Vérifier que l'utilisateur est connecté
            Utilisateur utilisateur = utilisateurService.getUtilisateurByPseudo(principal.getName());
            if (utilisateur == null) {
                redirectAttributes.addFlashAttribute("erreur", "Utilisateur non trouvé");
                return "redirect:/vendeur/gestion-articles";
            }

            // Vérifier que l'article existe et appartient à l'utilisateur
            List<ArticleAVendre> articles = articleService.getArticleById(Math.toIntExact(articleId));
            if (articles.isEmpty()) {
                redirectAttributes.addFlashAttribute("erreur", "Article non trouvé");
                return "redirect:/vendeur/gestion-articles";
            }

            ArticleAVendre article = articles.get(0);
            
            // Vérifier que l'article appartient bien à l'utilisateur connecté
            if (!article.getVendeur().getPseudo().equals(utilisateur.getPseudo())) {
                redirectAttributes.addFlashAttribute("erreur", "Vous ne pouvez supprimer que vos propres articles");
                return "redirect:/vendeur/gestion-articles";
            }

            // Vérifier que l'enchère n'a pas encore commencé
            java.util.Date maintenant = new java.util.Date();
            if (!article.getDate_debut_enchere().after(maintenant)) {
                redirectAttributes.addFlashAttribute("erreur", "Impossible de supprimer un article dont l'enchère a déjà commencé");
                return "redirect:/vendeur/gestion-articles";
            }

            // Supprimer l'article
            boolean supprime = articleService.deleteArticle(Math.toIntExact(articleId));
            
            if (supprime) {
                redirectAttributes.addFlashAttribute("succes", "Article supprimé avec succès");
            } else {
                redirectAttributes.addFlashAttribute("erreur", "Erreur lors de la suppression de l'article");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", "Erreur lors de la suppression : " + e.getMessage());
        }

        return "redirect:/vendeur/gestion-articles";
    }
} 