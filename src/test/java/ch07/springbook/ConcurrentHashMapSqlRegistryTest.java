package ch07.springbook;

import ch07.springbook.sql.registry.ConcurrentHashMapSqlRegistry;
import ch07.springbook.sql.registry.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		return new ConcurrentHashMapSqlRegistry();
	}
}

