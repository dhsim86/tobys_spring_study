package ch07.springbook;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedDbTest {

	private EmbeddedDatabase db;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() {
		db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:sql/embedded/schema.sql")
				.addScript("classpath:sql/embedded/data.sql")
				.build();

		jdbcTemplate = new JdbcTemplate(db);
	}

	@After
	public void tearDown() {
		db.shutdown();
	}

	@Test
	public void initData() {
		assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM SQLMAP", Integer.class), is(2));

		List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM SQLMAP ORDER BY KEY_");
		assertThat((String)list.get(0).get("KEY_"), is("KEY1"));
		assertThat((String)list.get(0).get("SQL_"), is("SQL1"));
		assertThat((String)list.get(1).get("KEY_"), is("KEY2"));
		assertThat((String)list.get(1).get("SQL_"), is("SQL2"));
	}

	@Test
	public void insert() {
		jdbcTemplate.update("INSERT INTO SQLMAP(KEY_, SQL_) VALUES(?, ?)", "KEY3", "SQL3");
		assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM SQLMAP", Integer.class), is(3));
	}
}
