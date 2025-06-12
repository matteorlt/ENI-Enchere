package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;


@Controller
@RequestMapping("/utilisateur")
@SessionAttributes({"utilisateurConnecte"})
public class UtilisateurController {

    private UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @ModelAttribute("utilisateurConnecte")
    public Utilisateur utilisateurConnecte() {
        System.out.println("Add Attribut Session");
        return new Utilisateur();
    }


    @GetMapping("/profil")
    public String afficherUnUtilisateur(@RequestParam(name = "pseudo", required = true) String pseudo, Model model, @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
        if (utilisateurConnecte != null && utilisateurConnecte.getPseudo() != null) {
            if (pseudo != null) {
                Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
                // Ajout de l'instance dans le modèle
                model.addAttribute("utilisateur", utilisateur);
                return "view-profil";
            } else {
                System.out.println("Utilisateur inconnu");
            }
        } else {
            System.out.println("Identifiant inconnu");
        }
        return "redirect:/accueil";
    }


    @GetMapping("/mon-profil")
    public String afficherProfilUtilisateur(@RequestParam(name = "pseudo", required = true) String pseudo, Model model, @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
//        if (utilisateurConnecte != null && pseudo.equals(utilisateurConnecte.getPseudo())) {
        Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
        int no_adresse =(int) utilisateur.getAdresse().getNo_adresse();
        Adresse adresse=utilisateurService.consulterAdresseParId(no_adresse);
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("adresse", adresse);
        return "view-mon-profil";
//        } else {
//            System.out.println("Pseudo utilisateur ne correspond pas à ce profil");
//        }
//        return "redirect:/accueil";
    }


    @GetMapping("mon-profil/modifier")
    public String modifierProfilUtilisateur(@RequestParam(name = "pseudo", required = true) String pseudo, Model model, @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
        if (utilisateurConnecte != null && pseudo.equals(utilisateurConnecte.getPseudo())) {
            Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
            model.addAttribute("utilisateur", utilisateur);
            return "view-profil-modif";
        } else {
            System.out.println("Pseudo utilisateur ne correspond pas à ce profil");
        }
        return "redirect:/accueil";
    }


    @PostMapping("mon-profil/modifier")
    public String mettreAJourFormateur(@ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult bindingResult) {
        System.out.println(utilisateur);
        utilisateurService.update(utilisateur);
        return "redirect:/mon-profil";
    }


//    @GetMapping("/connexion")
//    public String connexion(@ModelAttribute("membreEnSession") Membre membreEnSession,
//                            @RequestParam(name = "email", required = false, defaultValue = "jtrillard@campus-eni.fr") String email) {
//        Membre aCharger = service.charger(email);
//        if (aCharger != null) {
//            membreEnSession.setId(aCharger.getId());
//            membreEnSession.setNom(aCharger.getNom());
//            membreEnSession.setPrenom(aCharger.getPrenom());
//            membreEnSession.setPseudo(aCharger.getPseudo());
//            membreEnSession.setAdmin(aCharger.isAdmin());
//
//        } else {
//            membreEnSession.setId(0);
//            membreEnSession.setNom(null);
//            membreEnSession.setPrenom(null);
//            membreEnSession.setPseudo(null);
//            membreEnSession.setAdmin(false);
//
//        }
//        System.out.println(membreEnSession);
//        // Evidemment on évite de mettre un mot de passe en session
//        // (surtout non chiffré)
//
//        return "redirect:/films";
//    }



    @GetMapping("/deconnexion")
    public String deconnexion(SessionStatus status) {
        status.setComplete();

        return "redirect:/accueil";
    }



//        @PostMapping("/supprimer")
//        @ResponseBody
//        public ResponseEntity<?> deleteAccount(Authentication authentication) {
//            try {
//                String UserPseudo = authentication.getPseudo();
//                Long userId = getUserId(authentication); // Méthode à implémenter
//
//                utilisateurService.deleteUserAccount(userId, UserPseudo);
//
//                // Déconnexion de l'utilisateur
//                SecurityContextHolder.clearContext();
//
//                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Compte supprimé avec succès\"}");
//            } catch (Exception e) {
//                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
//            }
//        }
}