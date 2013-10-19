package searchCI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;


public abstract class ThreadSearch extends Thread {

	Map <Object,Set<Integer>> index;
	Object searchObject;
	Set<Integer> resultSet;
	
	@Override
	public final void run() {
		if(index != null) {
			resultSet = new HashSet<>();
			Iterator<Object> iterator = index.keySet().iterator();
			
			while(iterator.hasNext()) {
				Object iterString = iterator.next();
				
				if(contains(iterString,searchObject)){
					resultSet.addAll(index.get(iterString));
				}
			}
		}
	}
	
	public final Set<Integer> searchIndex(Object searchObject, Map<Object,Set<Integer>> index) {
		this.index = new TreeMap<>(index);
		this.searchObject = searchObject;
		run();
		return resultSet;
	}
	
	protected abstract boolean contains(Object t1, Object t2);
	
}
