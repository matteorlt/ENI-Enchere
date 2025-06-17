package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UtilisateurService extends UserDetailsService {

    Utilisateur consulterUtilisateurParPseudo(String pseudo);

    Utilisateur getUtilisateurByPseudo(String pseudo);

    void update(Utilisateur utilisateur);

    Adresse consulterAdresseParId(int no_adresse);

    Utilisateur authentifier(@NotBlank String pseudo, @NotBlank String motDePasse);

    String enregistrerUnUtilisateur(Utilisateur utilisateur);

    void enregistrerUneAdresse(Adresse adresse);
    
    void updateCredit(String pseudo, int nouveauCredit);
}
