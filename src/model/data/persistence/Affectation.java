package model.data.persistence;

import java.util.HashSet;

public class Affectation {

    private HashSet<Secouriste> idSec;
    private HashSet<DPS> idDps;
    private Competence competence;

    
    public HashSet<Secouriste> getIdSec(){
        return (HashSet<Secouriste>) idSec.clone();
    }

    public void setIdSec(HashSet<Secouriste> newIdSec){
        if(newIdSec == null || newIdSec.isEmpty() || newIdSec.contains(null)){
            throw new IllegalArgumentException("setIdSec : paramètre invalide");
        }
        this.idSec = newIdSec;
    }

    public Competence getCompetence(){
        return (Competence) this.competence.clone();
    }

    public void setCompetecnce(Competence newCompetence){
        if(newCompetence == null || newCompetence.getNom().trim().equals("")){
            throw new IllegalArgumentException("setCompetence : paramètre invalide");
        }
        this.competence = newCompetence;
    }

    public HashSet<DPS> getIdDps(){
        return (HashSet<DPS>) idDps.clone();
    }

    public void setIdDps(HashSet<DPS> newIdDps){
        if(newIdDps == null || newIdDps.isEmpty() || newIdDps.contains(null)){
            throw new IllegalArgumentException("setIdDps : paramètre invalide");
        }
        this.idDps = newIdDps;
    }
}
