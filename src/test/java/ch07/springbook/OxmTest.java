package ch07.springbook;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch07.springbook.sql.reader.jaxb.SqlType;
import ch07.springbook.sql.reader.jaxb.Sqlmap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/OxmTest-context.xml")
public class OxmTest {

	@Autowired
	private Unmarshaller unmarshaller;

	@Test
	public void unmarshallSqlMap() throws XmlMappingException, IOException {

		Source xmlSource = new StreamSource(getClass().getResourceAsStream("/sql/sqlmap_test.xml"));
		Sqlmap sqlMap = (Sqlmap)unmarshaller.unmarshal(xmlSource);

		List<SqlType> sqlList = sqlMap.getSql();

		assertThat(sqlList.size(), is(3));

		assertThat(sqlList.get(0).getKey(), is("add"));
		assertThat(sqlList.get(0).getValue(), is("insert"));
		assertThat(sqlList.get(1).getKey(), is("get"));
		assertThat(sqlList.get(1).getValue(), is("select"));
		assertThat(sqlList.get(2).getKey(), is("delete"));
		assertThat(sqlList.get(2).getValue(), is("delete"));
	}
}
