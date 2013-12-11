/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dbAccess.OracleDBAccess;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import rqsData.MovieDocument;

/**
 *
 * @author mike
 */
public class testSql {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MovieDocument[] movies = getMovies(0, 0, 0, 0, "");
    }
    public static MovieDocument[] getMovies(int minPopularity, int maxPopularity, float minDurability, 
            float maxDurability, String title) {
        Vector<MovieDocument> movieDoc = new Vector<MovieDocument>();
        OracleDBAccess dbAccess = null;
        MovieDocument[] moviesRs = null;
        Array a = null;
        try {
            dbAccess = new OracleDBAccess();
            dbAccess.startConnection("@localhost:1521:XE", "BD_CC1", "dummy");
            a = dbAccess.readObject("MOVIES_T", minPopularity, maxPopularity, minDurability, maxDurability, title);
            Object test =  a.getArray();
            moviesRs = (MovieDocument[]) test;
            System.out.println(moviesRs[0].toString());
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        

        if(dbAccess!=null){
            try {
                dbAccess.stopConnection();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return moviesRs;
    }
}
