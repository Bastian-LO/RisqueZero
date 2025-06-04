package model.data.persistence;

public class Dispos {
    private Journee date;
    private int[] heureDebut;
    private int[] heureFin;

    public Dispos(Journee date, int[] heureDebut, int[] heureFin) throws IllegalArgumentException {
        // Checks if the parameters are valid
        if (date == null || heureDebut == null || heureFin == null || heureDebut[0] < 0 || heureDebut[1] < 0 || heureFin[0] < 0 || heureFin[1] < 0 || heureDebut[0] > 23 || heureDebut[1] > 59 || heureFin[0] > 23 || heureFin[1] > 59 ) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }else if (heureDebut[0] > heureFin[0] || (heureDebut[0] == heureFin[0] && heureDebut[1] >= heureFin[1])) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin");
        }else{

            this.date = date;
            this.heureDebut = heureDebut;
            this.heureFin = heureFin;
        }
    }

    //================================
    //           GETTERS
    //================================
    public date getDate() {
        return date;
    }

    public int[] getHeureDebut() {
        return heureDebut;
    }

    public int[] getHeureFin() {
        return heureFin;
    }

    @Override
    public String toString() {
        return date.toString() + " - " + heureDebut[0] + ":" + String.format("%02d", heureDebut[1]) + " - " + heureFin[0] + ":" + String.format("%02d", heureFin[1]);
    }
}
