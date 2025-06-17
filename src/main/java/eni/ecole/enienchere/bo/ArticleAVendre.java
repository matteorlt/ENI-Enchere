package eni.ecole.enienchere.bo;

import java.util.Date;

public class ArticleAVendre {

    private long no_article;
    private String nom_article;
    private String description;
    private Date date_debut_enchere;
    private Date date_fin_enchere;
    private int statut;
    private int prix_initial;
    private int prix_vente;
    private Utilisateur vendeur;
    private Categorie categorie;
    private Adresse adresse_retrait;
    private String photo;

    public ArticleAVendre(long no_article, Adresse adresse_retrait, Categorie categorie, Utilisateur vendeur, int prix_vente, int prix_initial, int statut, Date date_fin_enchere, Date date_debut_enchere, String description, String nom_article) {
        this.no_article = no_article;
        this.adresse_retrait = adresse_retrait;
        this.categorie = categorie;
        this.vendeur = vendeur;
        this.prix_vente = prix_vente;
        this.prix_initial = prix_initial;
        this.statut = statut;
        this.date_fin_enchere = date_fin_enchere;
        this.date_debut_enchere = date_debut_enchere;
        this.description = description;
        this.nom_article = nom_article;
    }

    public ArticleAVendre() {
    }

    public long getNo_article() {
        return no_article;
    }

    public Adresse getAdresse_retrait() {
        return adresse_retrait;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Utilisateur getVendeur() {
        return vendeur;
    }

    public int getPrix_vente() {
        return prix_vente;
    }

    public int getPrix_initial() {
        return prix_initial;
    }

    public int getStatut() {
        return statut;
    }

    public Date getDate_fin_enchere() {
        return date_fin_enchere;
    }

    public Date getDate_debut_enchere() {
        return date_debut_enchere;
    }

    public String getDescription() {
        return description;
    }

    public String getNom_article() {
        return nom_article;
    }

    public String getPhoto() {
        return photo;
    }

    public void setNo_article(long no_article) {
        this.no_article = no_article;
    }

    public void setNom_article(String nom_article) {
        this.nom_article = nom_article;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate_debut_enchere(Date date_debut_enchere) {
        this.date_debut_enchere = date_debut_enchere;
    }

    public void setDate_fin_enchere(Date date_fin_enchere) {
        this.date_fin_enchere = date_fin_enchere;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public void setPrix_initial(int prix_initial) {
        this.prix_initial = prix_initial;
    }

    public void setVendeur(Utilisateur vendeur) {
        this.vendeur = vendeur;
    }

    public void setPrix_vente(int prix_vente) {
        this.prix_vente = prix_vente;
    }

    public void setAdresse_retrait(Adresse adresse_retrait) {
        this.adresse_retrait = adresse_retrait;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "ArticleAVendre{" +
                "no_article=" + no_article +
                ", nom_article='" + nom_article + '\'' +
                ", description='" + description + '\'' +
                ", date_debut_enchere=" + date_debut_enchere +
                ", date_fin_enchere=" + date_fin_enchere +
                ", statut=" + statut +
                ", prix_initial=" + prix_initial +
                ", prix_vente=" + prix_vente +
                ", vendeur=" + vendeur +
                ", categorie=" + categorie +
                ", adresse_retrait=" + adresse_retrait +
                '}';
    }
}
