package com.blog.monitorservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库配置类
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.blog.monitorservice.collector",
        entityManagerFactoryRef = "monitorEntityManagerFactory",
        transactionManagerRef = "monitorTransactionManager"
)
public class DatabaseConfig {

    @Autowired
    private Environment env;

    /**
     * 配置数据源
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    /**
     * 配置实体管理器工厂
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean monitorEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.blog.monitorservice.collector.model");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql", "false"));
        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql", "false"));
        
        // 配置自定义类型
        properties.put("hibernate.type_contributors", "com.blog.monitorservice.collector.model.JsonBinaryType");
        
        em.setJpaPropertyMap(properties);
        
        return em;
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public PlatformTransactionManager monitorTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(monitorEntityManagerFactory().getObject());
        return transactionManager;
    }
}