package model.data.persistence;

public class Dispos {
    private Secouriste secouriste;
    private Journee date;
    private int[] heureDebut;
    private int[] heureFin;

    public Dispos(Secouriste secouriste, Journee date, int[] heureDebut, int[] heureFin) throws IllegalArgumentException {
        // Checks if the parameters are valid
        if (secouriste == null || date == null || heureDebut == null || heureFin == null || heureDebut[0] < 0 || heureDebut[1] < 0 || heureFin[0] < 0 || heureFin[1] < 0 || heureDebut[0] > 23 || heureDebut[1] > 59 || heureFin[0] > 23 || heureFin[1] > 59 ) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }else if (heureDebut[0] > heureFin[0] || (heureDebut[0] == heureFin[0] && heureDebut[1] >= heureFin[1])) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin");
        }else{
            this.secouriste = new Secouriste(secouriste.getId(), secouriste.getNom(), secouriste.getPrenom(), secouriste.getDateNaissance(), secouriste.getEmail(), secouriste.getTel(), secouriste.getAdresse(), secouriste.getDisponibilites());
            this.date = new Journee(date.getJour(), date.getMois(), date.getAnnee());
            this.heureDebut = heureDebut.clone();
            this.heureFin = heureFin.clone();
        }
    }

    //================================
    //           GETTERS
    //================================

    public Secouriste getSecouriste() {
        return new Secouriste(
            this.secouriste.getId(), 
            this.secouriste.getNom(), 
            this.secouriste.getPrenom(), 
            this.secouriste.getDateNaissance(), 
            this.secouriste.getEmail(), 
            this.secouriste.getTel(),
            this.secouriste.getAdresse(),
            this.secouriste.getDisponibilites()
        );
    }

    public Journee getDate() {
        return new Journee(this.date.getJour(), this.date.getMois(), this.date.getAnnee());
    }

    public int[] getHeureDebut() {
        return heureDebut.clone();
    }

    public int[] getHeureFin() {
        return heureFin.clone();
    }

    @Override
    public String toString() {
        return date.toString() + " - " + heureDebut[0] + ":" + String.format("%02d", heureDebut[1]) + " - " + heureFin[0] + ":" + String.format("%02d", heureFin[1]);
    }
}
