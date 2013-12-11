/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbAccess;

import java.sql.SQLException;
import java.util.Vector;
import rqsData.MovieDocument;
import rqsData.Projection;

/**
 *
 * @author mike
 */
public class beanAccess {
    public static Vector<MovieDocument> getMovies(int minPopularity, int maxPopularity, float minDurability, 
            float maxDurability, String title) {
        Vector<MovieDocument> moviesDocs = new Vector<MovieDocument>();
        OracleDBAccess dbAccess = null;
        try {
            dbAccess = new OracleDBAccess();
            dbAccess.startConnection("@localhost:1521:XE", "BD_CC1", "dummy");
            moviesDocs = dbAccess.getMovies(minPopularity, maxPopularity, minDurability, maxDurability, title);
            
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        

        if(dbAccess!=null){
            try {
                dbAccess.stopConnection();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
        return moviesDocs;
    }
    
    public static Vector<Projection> getProjections(int movieId) {
        Vector<Projection> projections = new Vector<Projection>();
        OracleDBAccess dbAccess = null;
        try {
            dbAccess = new OracleDBAccess();
            dbAccess.startConnection("@localhost:1521:XE", "BD_CC1", "dummy");
            projections = dbAccess.getProjections(movieId);
            
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        

        if(dbAccess!=null){
            try {
                dbAccess.stopConnection();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
        return projections;
    }
}


