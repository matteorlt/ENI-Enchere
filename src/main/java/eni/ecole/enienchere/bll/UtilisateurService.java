package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import jakarta.validation.constraints.NotBlank;

public interface UtilisateurService {

    Utilisateur consulterUtilisateurParPseudo(String pseudo);

    void update(Utilisateur utilisateur);

    Adresse consulterAdresseParId(int no_adresse);

    void updateMdp(String pseudo, String mot_de_passe);

    void updateAdresse(Adresse adresse, String rue, String cp, String ville);

    void modifUtilisateur(Utilisateur utilisateur, String nom, String prenom, String email, String telephone);
}
