package eni.ecole.enienchere.bo;

import java.time.LocalDateTime;


public class Enchere {

    private LocalDateTime date_enchere;
    private int montant_enchere;
    private ArticleAVendre articleAVendre;
    private Utilisateur acquereur;

    public Enchere(LocalDateTime date_enchere, int montant_enchere, ArticleAVendre articleAVendre, Utilisateur acquereur) {
        this.date_enchere = date_enchere;
        this.montant_enchere = montant_enchere;
        this.articleAVendre = articleAVendre;
        this.acquereur = acquereur;
    }

    public Enchere() {
    }

    public LocalDateTime getDate_enchere() {
        return date_enchere;
    }

    public int getMontant_enchere() {
        return montant_enchere;
    }

    public ArticleAVendre getArticleAVendre() {
        return articleAVendre;
    }

    public Utilisateur getAcquereur() {
        return acquereur;
    }

    public void setDate_enchere(LocalDateTime date_enchere) {
        this.date_enchere = date_enchere;
    }

    public void setMontant_enchere(int montant_enchere) {
        this.montant_enchere = montant_enchere;
    }

    public void setArticleAVendre(ArticleAVendre articleAVendre) {
        this.articleAVendre = articleAVendre;
    }

    public void setAcquereur(Utilisateur acquereur) {
        this.acquereur = acquereur;
    }

    @Override
    public String toString() {
        return "Enchere{" +
                "date_enchere=" + date_enchere +
                ", montant_enchere=" + montant_enchere +
                ", articleAVendre=" + articleAVendre +
                ", acquereur=" + acquereur +
                '}';
    }
}
