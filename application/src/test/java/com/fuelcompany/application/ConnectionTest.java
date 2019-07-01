package com.fuelcompany.application;

import org.h2.jdbc.JdbcSQLException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTest {
    private static Logger logger = LoggerFactory.getLogger(ConnectionTest.class);

    @Test
    @Rollback
    @Transactional
    public void testDatabaseMem() throws SQLException {
        logger.info("Test connection to H2 database");
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:./h2mem");
        Statement s = connection.createStatement();

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("select * from PERSON");
        } catch (JdbcSQLException ignored) {
        }
        assertNull(ps);

        s.execute("CREATE TABLE PERSON (ID INT PRIMARY KEY, FIRSTNAME VARCHAR(64), LASTNAME VARCHAR(64))");
        ps = connection.prepareStatement("select * from PERSON");
        ResultSet r = ps.executeQuery();
        assertFalse(r.next());

        s.execute("INSERT INTO PERSON(ID,FIRSTNAME, LASTNAME) VALUES (1,'123132','123123')");
        ps = connection.prepareStatement("select * from PERSON");
        r = ps.executeQuery();
        assertTrue(r.next());

        r.close();
        ps.close();
        s.close();
        connection.close();
    }
}
