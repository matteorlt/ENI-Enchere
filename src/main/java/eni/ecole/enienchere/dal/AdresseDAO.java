package eni.ecole.enienchere.dal;

import eni.ecole.enienchere.bo.Adresse;
import eni.ecole.enienchere.bo.Utilisateur;

import java.util.List;

public interface AdresseDAO {

    long create(Adresse adresse);
    Utilisateur read(int no_adresse);
    List<Adresse> readAll();
    void update(Adresse adresse);
    void delete(int no_adresse);}
