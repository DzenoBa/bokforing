package se.chalmers.bokforing;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Base class for use in tests that require the database. Populates the database
 * with tables from the schema.sql file.
 * 
 * The @DirtiesContext annotation drops and creates the database for each 
 * @Test method.
 * 
 * @author Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Before
    public void setupDatabase() throws SQLException {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        
        Connection connection = null;
        
        try {
            connection = DataSourceUtils.getConnection(dataSource);

            populator.populate(connection);
        } finally {
            if(connection != null) {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
    }
}
