package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bll.UtilisateurService;
import eni.ecole.enienchere.bo.Utilisateur;
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
        if (utilisateurConnecte != null && pseudo.equals(utilisateurConnecte.getPseudo())) {
            Utilisateur utilisateur = UtilisateurService.consulterUtilisateurParPseudo(pseudo);
            model.addAttribute("utilisateur", utilisateur);
            return "view-mon-profil";
        } else {
            System.out.println("Pseudo utilisateur ne correspond pas à ce profil");
        }
        return "redirect:/accueil";
    }


    @GetMapping("mon-profil/modifier")
    public String modifierProfilUtilisateur(@RequestParam(name = "pseudo", required = true) String pseudo, Model model, @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
        if (utilisateurConnecte != null && pseudo.equals(utilisateurConnecte.getPseudo())) {
            Utilisateur utilisateur = UtilisateurService.consulterUtilisateurParPseudo(pseudo);
            model.addAttribute("utilisateur", utilisateur);
            return "view-profil-modif";
        } else {
            System.out.println("Pseudo utilisateur ne correspond pas à ce profil");
        }
        return "redirect:/accueil";
    }

    @PostMapping("mon-profil/modifier")
    public String mettreAJourUtilisateur(@ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
        System.out.println(utilisateurConnecte);
        UtilisateurService.update(utilisateurConnecte);
        return "redirect:/mon-profil";
    }


    @GetMapping("/connexion")
    public String connexion() {
        return "view-connexion";
    }


    @GetMapping("/deconnexion")
    public String deconnexion(SessionStatus status) {
        status.setComplete();

        return "redirect:/accueil";
    }

    //----- A RETRAVAILLER -----
    @PostMapping("/supprimer")
    public String supprimerCompte(@RequestParam(name = "pseudo", required = true) String pseudo, Model model, @ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte) {
        if (utilisateurConnecte != null && pseudo.equals(utilisateurConnecte.getPseudo())) {

            utilisateurService.supprimerUtilisateur(utilisateurConnecte.getPseudo());
        } else {
            return "redirect:/accueil";
        }
        return "redirect:/supprimer?success";

    }


    @GetMapping("/creer-compte")
    public String formulaireCreationCompte(Model model) {
        Utilisateur utilisateur = new Utilisateur();
        model.addAttribute("utilisateur", utilisateur);
        return "view-creer-compte";
    }

    //----- A RETRAVAILLER -----
    @PostMapping("creer-compte/enregistrer")
    public String enregistrerCompte(@ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult result, Model model) {
        Utilisateur utilisateurExistant = UtilisateurService.getUtilisateurParMail(utilisateur.getEmail());

        if (utilisateurExistant != null && utilisateurExistant.getEmail() != null && !utilisateurExistant.getEmail().isEmpty()) {
            result.rejectValue("email", null, "Cet email est déjà utilisé");
        }

        if (result.hasErrors()) {
            model.addAttribute("utilisateur", utilisateur);
            return "/creer-compte";
        }

        utilisateurService.enregistrerUtilisateur(utilisateur);
        return "redirect:/creer-compte?success";
    }
}
