package ch07.springbook.sql;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ch01.springbook.user.dao.UserDao;
import ch07.springbook.sql.reader.SqlReader;
import ch07.springbook.sql.reader.jaxb.SqlType;
import ch07.springbook.sql.reader.jaxb.Sqlmap;
import ch07.springbook.sql.registry.SqlNotFoundException;
import ch07.springbook.sql.registry.SqlRegistry;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;

	private Map<String, String> sqlMap = new HashMap<>();
	private String sqlMapFile;

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	public void setSqlMapFile(String sqlMapFile) {
		this.sqlMapFile = sqlMapFile;
	}

	@PostConstruct
	public void loadSql() {
		sqlReader.read(sqlRegistry);
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
	}

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);

		if (sql == null) {
			throw new SqlRetrievalFailureException("Can not find appropriate sql statement, key: " + key);
		}

		return sql;
	}

	@Override
	public void read(SqlRegistry sqlRegistry) {
		String contextPath = Sqlmap.class.getPackage().getName();

		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream inputStream = UserDao.class.getResourceAsStream(sqlMapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(inputStream);

			for (SqlType sql : sqlmap.getSql()) {
				sqlRegistry.registerSql(sql.getKey(), sql.getValue());
			}

		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}
