package ch07.springbook;

import org.junit.After;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import ch07.springbook.sql.registry.EmbeddedDbSqlRegistry;
import ch07.springbook.sql.registry.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

	private EmbeddedDatabase db;

	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
										  .addScript("classpath:sql/embedded/schema.sql")
										  .build();

		EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
		embeddedDbSqlRegistry.setDataSource(db);

		return embeddedDbSqlRegistry;
	}

	@After
	public void tearDown() {
		db.shutdown();
	}
}
