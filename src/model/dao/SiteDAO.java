package model.dao;

import java.util.List;

import model.data.persistence.Site;

public class SiteDAO extends DAO<Site> {
      public SiteDAO() {
    }

    //==========================
    //          METHODS
    //==========================


    public List <Site> findAll (){
        return null;
    }

    public Site findByID(Long id){
        return null;
    }

    public int update(Site element){
        return 0;
    }

    public int delete(Site element){
        return 0;
    }

    public int create(Site element){
        return 0;
    }
}