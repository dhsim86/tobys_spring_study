package ch07.springbook.sql;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import ch07.springbook.sql.reader.SqlReader;
import ch07.springbook.sql.reader.jaxb.SqlType;
import ch07.springbook.sql.reader.jaxb.Sqlmap;
import ch07.springbook.sql.registry.HashMapSqlRegistry;
import ch07.springbook.sql.registry.SqlRegistry;

public class OxmSqlService implements SqlService {

	private final BaseSqlService baseSqlService = new BaseSqlService();
	private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

	private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

	private static class OxmSqlReader implements SqlReader {

		private Unmarshaller unmarshaller;
		private Resource sqlMap = new ClassPathResource("sql/sqlmap.xml");

		public void setUnmarshaller(Unmarshaller unmarshaller) {
			this.unmarshaller = unmarshaller;
		}

		public void setSqlMap(Resource sqlMap) {
			this.sqlMap = sqlMap;
		}

		@Override
		public void read(SqlRegistry sqlRegistry) {
			try {
				Source source = new StreamSource(sqlMap.getInputStream());
				Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(source);

				for (SqlType sql : sqlmap.getSql()) {
					sqlRegistry.registerSql(sql.getKey(), sql.getValue());
				}

			} catch (IOException e) {
				throw new IllegalArgumentException(sqlMap.getFilename() + " not found.", e);
			}
		}
	}

	@PostConstruct
	public void loadSql() {
		baseSqlService.setSqlReader(oxmSqlReader);
		baseSqlService.setSqlRegistry(sqlRegistry);

		baseSqlService.loadSql();
	}

	public void setUnmarshaller(Unmarshaller unmarshaller) {
		oxmSqlReader.setUnmarshaller(unmarshaller);
	}

	public void setSqlMap(Resource sqlMap) {
		oxmSqlReader.setSqlMap(sqlMap);
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		return baseSqlService.getSql(key);
	}
}
