package dbAccess;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import oracle.jdbc.OracleCallableStatement;

import rqsData.Country;
import rqsData.MovieDocument;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleTypes;
import rqsData.Projection;

public class OracleDBAccess {
	
	private Connection connection;
	private Map typeMap;
	
	
	public OracleDBAccess() throws SQLException {
		DriverManager.registerDriver(new OracleDriver());
	}
	
	public synchronized void startConnection(String dbPath,String login,String password) throws SQLException{
		connection = DriverManager.getConnection("jdbc:oracle:thin:"+dbPath, login, password);
		Logger.getGlobal().info("Connected to jdbc:oracle:thin:"+dbPath);
		if(typeMap == null){
			typeMap = connection.getTypeMap();
			typeMap.put("MOVIE_T",MovieDocument.class);
			typeMap.put("COUNTRY_T", Country.class);
			connection.setTypeMap(typeMap);
		}
	}
	
	public synchronized void stopConnection() throws SQLException{
		Logger.getGlobal().info("Closed connection");
		if(connection!=null)
			connection.close();
	}
	
	public Connection getConnection(){
		return connection;
	}
	
	
	public synchronized void sendObject(String type,List<Object> object) throws SQLException{
		sendObject(type, object.toArray());
	}
	
	public synchronized void sendObject(String type,Collection<Object> object) throws SQLException{
		sendObject(type,object.toArray());
	}
	
	public synchronized void sendObject(String type,Object[] object) throws SQLException{
		
		Logger.getGlobal().info("Sending MovieDocument to DB");
		
		CallableStatement statement = connection.prepareCall("{call addMovies(?)}");
		statement.setArray(1, ((OracleConnection)connection).createARRAY(type, object));
		statement.execute();
		statement.close();
		connection.commit();
	}
        
        public synchronized Vector<MovieDocument> getMovies(int minPopularity, int maxPopularity, float minDurability, 
            float maxDurability, String title) {
            ResultSet result = null;
            Vector<MovieDocument> moviesList = new Vector<MovieDocument>();
            
            try {
                Logger.getGlobal().info("Loading MovieDocuments from DB");
                CallableStatement statement = connection.prepareCall("{? = call loadMovies(?, ?, ?, ?, ?)}");
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setInt(2, minPopularity);
                statement.setInt(3, maxPopularity);
                statement.setFloat(4, minDurability);
                statement.setFloat(5, maxDurability);
                statement.setString(6, title);
                statement.execute();

                result = ((OracleCallableStatement)statement).getCursor(1);
                while(result.next()) {
                    MovieDocument bufferMovie = new MovieDocument();
                    bufferMovie.id = result.getInt("id");
                    bufferMovie.title = result.getString("title");
                    bufferMovie.overview = result.getString("overview");
                    //bufferMovie.runtime = result.getInt("runtime");
                    //bufferMovie.vote_average = result.getDouble("vote_average");
                    //bufferMovie.vote_count = result.getInt("vote_count");
                    moviesList.add(bufferMovie);
                }
                
                statement.close();
                connection.commit();
                
                for(MovieDocument m: moviesList) {
                    CallableStatement statementAct = connection.prepareCall("{? = call loadActors(?)}");

                    statementAct.registerOutParameter(1, OracleTypes.CURSOR);
                    statementAct.setInt(2, m.id);
                    statementAct.execute();
                    ResultSet rsAct = ((OracleCallableStatement)statementAct).getCursor(1);
                    while(rsAct.next()) {
                        m.actors.add(rsAct.getString("name"));
                    }
                    statementAct.close();
                    connection.commit();
                }
                
                for(MovieDocument m: moviesList) {
                    CallableStatement statementCert = connection.prepareCall("{? = call loadCertifications(?)}");

                    statementCert.registerOutParameter(1, OracleTypes.CURSOR);
                    statementCert.setInt(2, m.id);
                    statementCert.execute();
                    ResultSet rsCert = ((OracleCallableStatement)statementCert).getCursor(1);
                    if(rsCert.next()) {
                        m.certification = rsCert.getString("name");
                    }
                    statementCert.close();
                    connection.commit();
                }
                
                for(MovieDocument m: moviesList) {
                    CallableStatement statementArch = connection.prepareCall("{? = call loadProjArchives(?)}");

                    statementArch.registerOutParameter(1, OracleTypes.CURSOR);
                    statementArch.setInt(2, m.id);
                    statementArch.execute();
                    ResultSet rsArch = ((OracleCallableStatement)statementArch).getCursor(1);
                    if(rsArch.next()) {
                        m.popularity = rsArch.getInt("Total_seats");
                        m.durability = rsArch.getFloat("durability");
                    }
                    statementArch.close();
                    connection.commit();
                }
                
                for(MovieDocument m: moviesList) {
                    CallableStatement statementImg = connection.prepareCall("{? = call loadImages(?)}");

                    statementImg.registerOutParameter(1, OracleTypes.CURSOR);
                    statementImg.setInt(2, m.id);
                    statementImg.execute();
                    ResultSet rsImg = ((OracleCallableStatement)statementImg).getCursor(1);
                    if(rsImg.next()) {
                        
                        
                        Blob blob = rsImg.getBlob("Image");
                        try {
                            int blobLen = (int) blob.length();
                            byte bytes[] = blob.getBytes(1, blobLen);
                            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                            m.poster = ImageIO.read(bais);
                            //bi.toString();
                        } catch (IOException ex) {
                            Logger.getLogger(OracleDBAccess.class.getName()).log(Level.SEVERE, null, ex);
                            ex.printStackTrace();
                        }
                        
                                
                    }
                    statementImg.close();
                    connection.commit();
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(OracleDBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            return moviesList;
            
        }
        
        public Vector<Projection> getProjections(int movieId) {
            
                ResultSet result = null;
                Vector<Projection> projections = new Vector<Projection>();
                
                Logger.getGlobal().info("Loading Projections from DB");
            try {
                CallableStatement statement = connection.prepareCall("{? = call loadProjections(?)}");
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setInt(2, movieId);
                statement.execute();

                result = ((OracleCallableStatement)statement).getCursor(1);
                while(result.next()) {
                    Projection proj = new Projection();
                    proj.day = result.getDate("Day");
                    proj.timeSlot = result.getInt("Time_Slot");
                    proj.remainingSeats = result.getInt("remaining_seats");
                    projections.add(proj);
                }

                statement.close();
                connection.commit();
                
            } catch (SQLException ex) {
                Logger.getLogger(OracleDBAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
            return projections;
        }
	
	@Override
	protected void finalize() throws Throwable {
		stopConnection();
	}
}
