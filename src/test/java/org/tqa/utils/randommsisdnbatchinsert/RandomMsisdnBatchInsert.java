package org.tqa.utils.randommsisdnbatchinsert;

import com.github.javafaker.Faker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@Component
class RandomMsisdnBatchInsert {

	@Autowired(required = false)
	DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	private Faker faker = new Faker();
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Transactional
	void generateFakeMsisdn(int outRows) throws SQLException {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Connection connection = dataSource.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate("TRUNCATE TABLE temptable");
		String sqlInsert = "INSERT INTO temptable (msisdn, created, updated, generation) VALUES(?,?,?,?) on conflict do nothing";
		String msisdn;
		List<String> msisdnArrayList = new ArrayList<>();
		for (int i = 0; i <= outRows; i++) {
			//RU msisdn format
			msisdn = faker.numerify("7##########");
			//msisdn = faker.regexify("7[0-9]{10}");
			msisdnArrayList.add(msisdn);
		}
		try {

			jdbcTemplate.batchUpdate(sqlInsert, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
					preparedStatement.setLong(1, Long.valueOf(msisdnArrayList.get(i)));
					preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
					preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
					preparedStatement.setInt(4, 10);
				}

				@Override
				public int getBatchSize() {
					log.info("ARRAY LIST SIZE = " + msisdnArrayList.size());
					return msisdnArrayList.size();
				}
			});
		} catch (Exception re) {
			re.printStackTrace();
		}
		stopWatch.stop();
		log.info("DONE in " + stopWatch.getTotalTimeSeconds() + " seconds");
	}
}