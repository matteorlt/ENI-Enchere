package eni.ecole.enienchere.bo;

public class Categorie {

    private long no_categorie;
    private String libelle;

    public Categorie(String libelle, long no_categorie) {
        this.libelle = libelle;
        this.no_categorie = no_categorie;
    }

    public Categorie() {
    }

    public long getNo_categorie() {
        return no_categorie;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setNo_categorie(long no_categorie) {
        this.no_categorie = no_categorie;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "no_categorie=" + no_categorie +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
