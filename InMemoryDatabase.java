/**
 * This interface defines methods to be implemented for an in memory database.
 */

/**
 * @author jatan
 *
 */
public interface InMemoryDatabase {
	void insert(String key, Object value);
	void remove(String key);
	Object get(String key);
	int numberEqualToValue(Object value);
	void begin();
	String rollback();
	void commit();
}
