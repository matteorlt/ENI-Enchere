package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Utilisateur;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface UtilisateurService {
    static void update(Utilisateur utilisateur) {
    }

    Utilisateur consulterUtilisateurParPseudo(String pseudo);

    void updateMdp(Utilisateur utilisateur, String mot_de_passe);
}
