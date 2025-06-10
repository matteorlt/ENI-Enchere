package eni.ecole.enienchere.bo;

public class Adresse {
    private long no_adresse;
    private String rue;
    private String code_postal;
    private String ville;
    private boolean adresse_eni;

    public Adresse(long no_adresse, String rue, String code_postal, String ville, boolean adresse_eni) {
        this.no_adresse = no_adresse;
        this.rue = rue;
        this.code_postal = code_postal;
        this.ville = ville;
        this.adresse_eni = adresse_eni;
    }

    public Adresse() {
    }

    public long getNo_adresse() {
        return no_adresse;
    }

    public String getRue() {
        return rue;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public String getVille() {
        return ville;
    }

    public boolean isAdresse_eni() {
        return adresse_eni;
    }

    public void setNo_adresse(long no_adresse) {
        this.no_adresse = no_adresse;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setAdresse_eni(boolean adresse_eni) {
        this.adresse_eni = adresse_eni;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "no_adresse=" + no_adresse +
                ", rue='" + rue + '\'' +
                ", code_postal='" + code_postal + '\'' +
                ", ville='" + ville + '\'' +
                ", adresse_eni=" + adresse_eni +
                '}';
    }
}
