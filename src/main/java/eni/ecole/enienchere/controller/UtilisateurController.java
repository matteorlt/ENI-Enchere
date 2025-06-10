package eni.ecole.enienchere.controller;

import eni.ecole.enienchere.bo.Utilisateur;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/Enchères.org")
@SessionAttributes({ "utilisateurConnecte" })
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

    @GetMapping("/mon-profil"){
        public String connexion(@ModelAttribute("utilisateurConnecte") Utilisateur utilisateurConnecte,
                @RequestParam(name = "pseudo", required = false) String pseudo) {
            Utilisateur aCharger = utilisateurService.charger(pseudo);
            if (aCharger != null) {
                utilisateurConnecte.setPseudo(aCharger.getPseudo());
                utilisateurConnecte.setNom(aCharger.getNom());
                utilisateurConnecte.setPrenom(aCharger.getPrenom());
                utilisateurConnecte.setEmail(aCharger.getEmail());
                utilisateurConnecte.setTelephone(aCharger.getTelephone());
                utilisateurConnecte.setCredit(aCharger.getCredit());
                utilisateurConnecte.setAdministrateur(aCharger.isAdmin());
                utilisateurConnecte.setAdresse(aCharger.getAdresse());

            } else {
                utilisateurConnecte.setPseudo(0);
                utilisateurConnecte.setNom(null);
                utilisateurConnecte.setPrenom(null);
                utilisateurConnecte.setEmail(null);
                utilisateurConnecte.setTelephone(null);
                utilisateurConnecte.setCredit(null);
                utilisateurConnecte.setAdmin(false);
                utilisateurConnecte.setAdresse(null);

            }
            System.out.println(utilisateurConnecte);

            return "redirect:/accueil";
        }

        @GetMapping("/deconnexion")
        public String finSession(SessionStatus status) {
            status.setComplete();

            return "redirect:/accueil";
        }
}
