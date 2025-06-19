package model.data.service;
import model.dao.*;
public class DAOMngt {
    private static final AffectationDAO affectationDAO = new AffectationDAO();
    private static final CompetenceDAO competenceDAO = new CompetenceDAO();
    private static final DisposDAO disposDAO= new DisposDAO();
    private static final DPSDAO dpsDAO = new DPSDAO();
    private static final SecouristeDAO secouristeDAO = new SecouristeDAO();
    private static final SiteDAO siteDAO = new SiteDAO();
    private static final SportDAO sportDAO = new SportDAO();
    private static final UserDAO userDAO = new UserDAO();
    
    /**
     * Returns the AffectationDAO singleton
     * @return the AffectationDAO singleton
     */
    public static AffectationDAO getAffectationDAO() {
        return affectationDAO;
    }
    
    /**
     * Returns the CompetenceDAO singleton
     * @return the CompetenceDAO singleton
     */
    public static CompetenceDAO getCompetenceDAO() {
        return competenceDAO;
    }
    
    /**
     * Returns the DisposDAO singleton
     * @return the DisposDAO singleton
     */
    public static DisposDAO getDisposDAO() {
        return disposDAO;
    }
    
    /**
     * Returns the DPSDAO singleton
     * @return the DPSDAO singleton
     */
    public static DPSDAO getDPSDAO() {
        return dpsDAO;
    }

    /**
     * Returns the SecouristeDAO singleton
     * @return the SecouristeDAO singleton
     */
    public static SecouristeDAO getSecouristeDAO() {
        return secouristeDAO;
    }
    
    /**
     * Returns the SiteDAO singleton
     * @return the SiteDAO singleton
     */
    public static SiteDAO getSiteDAO() {
        return siteDAO;
    }
    
    /**
     * Returns the SportDAO singleton
     * @return the SportDAO singleton
     */
    public static SportDAO getSportDAO() {
        return sportDAO;
    }
    
    /**
     * Returns the UserDAO singleton
     * @return the UserDAO singleton
     */
    public static UserDAO getUserDAO() {
        return userDAO;
    }
}