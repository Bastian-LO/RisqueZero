package model.dao;

import java.util.List;

import model.data.persistence.DPS;

public class DPSDAO extends DAO<DPS> {
      public DPSDAO() {
    }

    //==========================
    //          METHODS
    //==========================


    public List <DPS> findAll (){
        return null;
    }

    public DPS findByID(Long id){
        return null;
    }

    public int update(DPS element){
        return 0;
    }

    public int delete(DPS element){
        return 0;
    }

    public int create(DPS element){
        return 0;
    }
}