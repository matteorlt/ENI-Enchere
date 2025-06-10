package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Utilisateur;

import java.util.List;

public interface UtilisateurDAO {

void create(Utilisateur utilisateur);
Utilisateur read(String pseudo);
List<Utilisateur> readAll();
Utilisateur update(String pseudo);
void delete(String pseudo);

}
