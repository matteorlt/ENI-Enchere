package eni.ecole.enienchere.bll;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;

public interface UtilisateurService {
    static void update(Utilisateur utilisateur) {
    }

    Utilisateur consulterUtilisateurParPseudo(String pseudo);

    void update(Utilisateur utilisateur);

    Adresse consulterAdresseParId(int no_adresse);
}
