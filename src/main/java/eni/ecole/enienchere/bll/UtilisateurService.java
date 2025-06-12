package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;
import jakarta.validation.constraints.NotBlank;

public interface UtilisateurService {

    Utilisateur consulterUtilisateurParPseudo(String pseudo);


    void update(Utilisateur utilisateur);

    Adresse consulterAdresseParId(int no_adresse);

    Utilisateur authentifier(@NotBlank String pseudo, @NotBlank String motDePasse);
}
