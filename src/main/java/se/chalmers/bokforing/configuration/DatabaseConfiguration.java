/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.configuration;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Class for configuring the Spring Data JPA functionality.
 * 
 * This class uses @Value tags to retrieve properties from the
 * application.properties file in the resources folder. This is for convenience,
 * so that you don't have to search through this class to find database settings.
 * 
 * The basePackages attribute for @EnableJpaRepositories tells Spring where to
 * look for classes tagged @Repository, the classes we use for contacting the
 * database.
 * 
 * The LocalContainerEntityManagerFactoryBean#setPackagesToScan method call below
 * sets where to look for classes tagged @Entity.
 * 
 * @author Jakob
 */
@Configuration
@EnableJpaRepositories(basePackages = "se.chalmers.bokforing.persistence")
@PropertySource(value = "classpath:application.properties")
@EnableTransactionManagement
public class DatabaseConfiguration {
    
    /** This is where Spring should search for entities. */
    private static final String PERSISTENCE_PACKAGE = "se.chalmers.bokforing.model";
    
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    
    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddlAuto;
    
    @Value("${jdbc.url}")
    private String jdbcURL;
    
    @Value("${jdbc.driver}")
    private String jdbcDriver;
        
    @Value("${jdbc.username}")
    private String jdbcUsername;
    
    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        
        ds.setDriverClassName(jdbcDriver);
        ds.setUrl(jdbcURL);
        ds.setUsername(jdbcUsername);
        ds.setPassword(jdbcPassword);
        
        return ds;
    }
    
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabase(Database.DERBY);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(DatabaseConfiguration.PERSISTENCE_PACKAGE); 
        factory.setJpaProperties(getJPAProperties());
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    private Properties getJPAProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        return properties;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}