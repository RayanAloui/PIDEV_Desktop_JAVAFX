package esprit.tn.entities;

public class Rating {
    private String nomOrphelin;
    private String prenomOrphelin;
    private int note;

    public Rating(String nomOrphelin, String prenomOrphelin, int note) {
        this.nomOrphelin = nomOrphelin;
        this.prenomOrphelin = prenomOrphelin;
        this.note = note;
    }

    public String getNomOrphelin() {
        return nomOrphelin;
    }

    public void setNomOrphelin(String nomOrphelin) {
        this.nomOrphelin = nomOrphelin;
    }

    public String getPrenomOrphelin() {
        return prenomOrphelin;
    }

    public void setPrenomOrphelin(String prenomOrphelin) {
        this.prenomOrphelin = prenomOrphelin;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }
}

