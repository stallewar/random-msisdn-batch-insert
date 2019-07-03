package org.tqa.utils.randommsisdnbatchinsert;

import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomMsisdnBatchInsertTests {

	@Autowired
	RandomMsisdnBatchInsert randomMsisdnBatchInsert;
	@Value("${outrows}")
	private Integer outrows;

	@Test
	public void generate() throws SQLException {
		randomMsisdnBatchInsert.generateFakeMsisdn(outrows);
	}

}