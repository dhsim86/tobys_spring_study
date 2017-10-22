package ch07.springbook.sql.reader;

import ch07.springbook.sql.registry.SqlRegistry;

public interface SqlReader {
	void read(SqlRegistry sqlRegistry);
}
