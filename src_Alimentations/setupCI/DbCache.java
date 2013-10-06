package setupCI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;

public abstract class DbCache {
	private Map<Object, Set<Integer>> index;
	private File dump;
	
	public DbCache(String cacheName) {
		ApplicationInfo dbInfo = ApplicationInfo.getInstance();
		dump = new File(dbInfo.getCacheDirPath() + System.getProperty("file.separator") + cacheName);
	}
	
	public void loadIndex() {
		if (cacheExists()) {
			SortedMap<Double, Set<Integer>> index = null;
			try {
				FileInputStream fis = new FileInputStream(dump);
				ObjectInputStream in = new ObjectInputStream(fis);
				index = (SortedMap<Double, Set<Integer>>) in.readObject();
				in.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		} else {
			
		}
	}
	
	
	public boolean cacheExists() {
		return dump.exists();
	}
}
