package entities;

public class Cours {
    private int idC;
    private String titre;
    private String contenu;
    private String imageC;
    private int idTuteur;
    private String resume;

    // Constructeurs
    public Cours() {}

    public Cours(int idC, String titre, String contenu, String imageC, int idTuteur) {
        this.idC = idC;
        this.titre = titre;
        this.contenu = contenu;
        this.imageC = imageC;
        this.idTuteur = idTuteur;
    }

    public Cours(int idC, String titre, String contenu,String resume, String imageC, int idTuteur) {
        this.idC = idC;
        this.titre = titre;
        this.contenu = contenu;
        this.resume = resume;
        this.imageC = imageC;
        this.idTuteur = idTuteur;
    }

    public Cours(int idC, String titre, String contenu, String imageC) {
        this.idC = idC;
        this.titre = titre;
        this.contenu = contenu;
        this.imageC = imageC;
    }

    // Getters et Setters
    public int getIdC() { return idC; }
    public void setIdC(int idC) { this.idC = idC; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getImageC() { return imageC; }
    public void setImageC(String imageC) { this.imageC = imageC; }

    public int getIdTuteur() { return idTuteur; }
    public void setIdTuteur(int idTuteur) { this.idTuteur = idTuteur; }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "idC=" + idC +
                ", titre='" + titre + '\'' +
                ", contenu='" + contenu + '\'' +
                ", imageC='" + imageC + '\'' +
                ", idT=" + idTuteur +
                '}';
    }
}
