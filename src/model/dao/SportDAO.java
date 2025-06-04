package model.dao;

import java.util.List;

import model.data.persistence.Sport;

public class SportDAO extends DAO<Sport> {
    public SportDAO() {
    }

    //==========================
    //          METHODS
    //==========================


    public List <Sport> findAll (){
        return null;
    }

    public Sport findByID(Long id){
        return null;
    }

    public int update(Sport element){
        return 0;
    }

    public int delete(Sport element){
        return 0;
    }

    public int create(Sport element){
        return 0;
    }
}