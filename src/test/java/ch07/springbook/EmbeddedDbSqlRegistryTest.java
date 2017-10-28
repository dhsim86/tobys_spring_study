package ch07.springbook;

import static junit.framework.TestCase.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import ch07.springbook.sql.registry.EmbeddedDbSqlRegistry;
import ch07.springbook.sql.registry.SqlUpdateFailureException;
import ch07.springbook.sql.registry.UpdatableSqlRegistry;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

	private EmbeddedDatabase db;

	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
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

	@Test
	public void transactionalUpdate() {
		checkFindResult("SQL1", "SQL2", "SQL3");

		Map<String, String> sqlMap = new HashMap<>();
		sqlMap.put("KEY1", "Modified1");
		sqlMap.put("KEY123!@#", "Modified");

		try {
			sqlRegistry.updateSql(sqlMap);
			fail();
		} catch (SqlUpdateFailureException e) {

		}

		checkFindResult("SQL1", "SQL2", "SQL3");
	}
}
