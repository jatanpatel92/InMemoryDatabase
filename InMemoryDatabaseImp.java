import java.lang.ref.SoftReference;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class implements InmemoryDatabase Interface.
 */

/**
 * @author jatan
 *
 */
public class InMemoryDatabaseImp implements InMemoryDatabase {
	
	private ConcurrentMap<String, SoftReference<Object>> dataMap = new ConcurrentHashMap<>();
	private Stack<ConcurrentMap<String, SoftReference<Object>>> transactionStack;
	
	public InMemoryDatabaseImp() {
		transactionStack = new Stack<>();
		transactionStack.push(dataMap);
	}
	
	@Override
	public void insert(String key, Object value) {
		if(key == null || transactionStack.isEmpty()) return;
		if(value == null) 
			transactionStack.peek().remove(key);
		else
			transactionStack.peek().put(key, new SoftReference<Object>(value));
	}

	@Override
	public void remove(String key) {
		if(!transactionStack.isEmpty())
			transactionStack.peek().remove(key);
	}

	@Override
	public Object get(String key) {
		if(transactionStack.isEmpty()) return null;
		SoftReference<Object> data = transactionStack.peek().getOrDefault(key, null);
		return data!=null ? data.get():null;
	}
	
	@Override
	public int numberEqualToValue(Object value) {
		int count = 0;
		if(transactionStack.isEmpty()) return count;
		for(Entry<String, SoftReference<Object>> entry : transactionStack.peek().entrySet()) {
			if(entry.getValue()!=null && entry.getValue().get().equals(value)) {
				count++;
			}
		}
		return count;
	}
	
	public void begin() {
		ConcurrentMap<String, SoftReference<Object>> transactionDataMap = new ConcurrentHashMap<>(dataMap);
		transactionStack.push(transactionDataMap);
	}
	
	public String rollback() {
		if(transactionStack.isEmpty() || transactionStack.size()==1) return "No Transaction";
		ConcurrentMap<String, SoftReference<Object>> transactionDataMap = transactionStack.pop();
		return "Rolled back 1 transaction.";
	}
	
	public void commit() {
		dataMap = transactionStack.pop();
		if(!transactionStack.isEmpty()) transactionStack.clear();
		transactionStack.push(new ConcurrentHashMap<>(dataMap));
	}
}