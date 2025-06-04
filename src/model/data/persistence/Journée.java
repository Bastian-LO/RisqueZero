public class Journée{
    private int jour;
    private int mois;
    private int annee;

    public Journée(int jour, int mois, int annee) throws IllegalArgumentException{
        checkDate(jour, mois, annee);

        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    public int getJour(){
        return this.jour;
    }

    public int getMois(){
        return this.mois;
    }

    public int getAnnee(){
        return this.annee;
    }

    public void setJour(int newJour) throws IllegalArgumentException{
        checkDate(newJour, this.mois, this.annee);

        this.jour = newJour;
    }

    public void setMois(int newMois) throws IllegalArgumentException{
        checkDate(this.jour, newMois, this.annee);

        this.mois = newMois;
    }

    public void setAnnee(int newAnnee) throws IllegalAccessException{
        checkDate(this.jour, this.mois, newAnnee);

        this.annee = newAnnee;
    }

    private void checkDate(int jour, int mois, int annee) throws IllegalArgumentException{
        if (jour < 1 || jour > 31 || mois < 1 || mois > 12 || annee < 0){
            throw new IllegalArgumentException("La date est impossible");
        }

        if (mois == 4 || mois == 6 || mois == 9 || mois == 11){
            if(jour > 30){
                throw new IllegalArgumentException("La date est impossible");
            }
        } else if (mois == 2){  
            // Traitement des années bissextiles
            if(annee % 4 != 0 || (annee % 100 == 0 && annee % 400 != 0)){
                if (jour > 28){
                    throw new IllegalArgumentException("La date est impossible");
                }
            } else {
                if (jour > 29){
                    throw new IllegalArgumentException("La date est impossible");
                }
            }
        } else {
            if (jour > 31){
                throw new IllegalArgumentException("La date est impossible");
            }
        }
    }
}