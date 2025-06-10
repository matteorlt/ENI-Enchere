package eni.ecole.enienchere.bo;

public class Utilisateur {

    private String pseudo;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String mot_de_passe;
    private int credit;
    private boolean administrateur;
    private Adresse adresse;

    public Utilisateur(String pseudo, String nom, String prenom, String telephone, String email, String mot_de_passe, boolean administrateur, int credit, Adresse adresse) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.administrateur = administrateur;
        this.credit = credit;
        this.adresse = adresse;
    }

    public Utilisateur() {
    }

    public String getPseudo() {
        return pseudo;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public boolean isAdministrateur() {
        return administrateur;
    }

    public int getCredit() {
        return credit;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setAdministrateur(boolean administrateur) {
        this.administrateur = administrateur;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "pseudo='" + pseudo + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", mot_de_passe='" + mot_de_passe + '\'' +
                ", credit=" + credit +
                ", administrateur=" + administrateur +
                ", adresse=" + adresse +
                '}';
    }
}
