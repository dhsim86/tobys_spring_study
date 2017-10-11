package ch07.springbook.sql;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ch01.springbook.user.dao.UserDao;

public class XmlSqlService implements SqlService {
	private Map<String, String> sqlMap = new HashMap<>();

	public XmlSqlService() {
		String contextPath = Sqlmap.class.getPackage().getName();

		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream inputStream = UserDao.class.getResourceAsStream("/sql/sqlmap.xml");
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(inputStream);

			for (SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}

		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);

		if (sql == null) {
			throw new SqlRetrievalFailureException("Can not find appropriate sql statement, key: " + key);
		}

		return sql;
	}
}
