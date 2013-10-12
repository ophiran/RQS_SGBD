package setupCI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationInfo {
	private Properties dbInfo;
	private String confFilePath = System.getProperty("user.dir") + System.getProperty("file.separator") 
			+ "RQSConf";
	
	private static ApplicationInfo refAppInfo;
	
	public String getIp() {
		return dbInfo.getProperty("dbIp");
	}
	
	public void setIp(String ip) {
		dbInfo.setProperty("dbIp", ip);
	}
	
	public String getPort() {
		return dbInfo.getProperty("dbPort");
	}
	
	public void setPort(String port) {
		dbInfo.setProperty("dbPort", port);
	}
	
	public String getDbName() {
		return dbInfo.getProperty("dbName");
	}
	
	public void setDbName(String dbName) {
		dbInfo.setProperty("dbName", dbName);
	}
	
	public String getCacheDirPath() {
		return dbInfo.getProperty("cacheDirPath");
	}
	
	public void setCacheDirPath(String cacheDirPath) {
		dbInfo.setProperty("cacheDirPath", cacheDirPath);
	}
	
	
	private ApplicationInfo() {
		
		dbInfo = new Properties(); 
						
		try {
			File dbInfoFile = new File(confFilePath);
			if (dbInfoFile.exists()) {
				dbInfo.load(new FileInputStream(dbInfoFile));
			} else {
				setIp("127.0.0.1");
				setPort("5984");
				setDbName("movies");
				setCacheDirPath(System.getProperty("user.dir"));
				dbInfo.store(new FileOutputStream(dbInfoFile), "data base informations");  
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static ApplicationInfo getInstance() {
		if(refAppInfo == null) {
			refAppInfo = new ApplicationInfo();
		}
		return refAppInfo;
	}
}
