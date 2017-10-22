package ch07.springbook.sql;

import ch07.springbook.sql.reader.JaxbXmlSqlReader;
import ch07.springbook.sql.registry.HashMapSqlRegistry;

public class DefaultSqlService extends BaseSqlService {
	public DefaultSqlService() {
		setSqlReader(new JaxbXmlSqlReader());
		setSqlRegistry(new HashMapSqlRegistry());
	}
}
