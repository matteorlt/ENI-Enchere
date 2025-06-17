package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UtilisateurService extends UserDetailsService {

    Utilisateur consulterUtilisateurParPseudo(String pseudo);

    void update(Utilisateur utilisateur);

    Adresse consulterAdresseParId(int no_adresse);

    void updateMdp(Utilisateur utilisateur, String mot_de_passe);

    void updateAdresse(Utilisateur utilisateur, String rue, String cp, String ville);

    void modifUtilisateur(Utilisateur utilisateur, String nom, String prenom, String email, String telephone);


    String enregistrerUnUtilisateur(Utilisateur utilisateur);

    void enregistrerUneAdresse(Adresse adresse);


}
