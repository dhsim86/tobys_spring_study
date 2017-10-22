package ch07.springbook.sql.reader;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ch01.springbook.user.dao.UserDao;
import ch07.springbook.sql.reader.jaxb.SqlType;
import ch07.springbook.sql.reader.jaxb.Sqlmap;
import ch07.springbook.sql.registry.SqlRegistry;

public class JaxbXmlSqlReader implements SqlReader {

	private static final String DEFAULT_SQLMAP_FILE = "/sql/sqlmap.xml";

	private String sqlMapFile = DEFAULT_SQLMAP_FILE;

	public void setSqlMapFile(String sqlMapFile) {
		this.sqlMapFile = sqlMapFile;
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
