package com.zll.jpa.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing //BaseEntity,AuditingEntityListener
@EnableJpaRepositories(basePackages = "com.zll.jpa.dao", transactionManagerRef = "jpaTransactionManager")
public class JpaConfig {


    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.type}")
    private String type;

    /**
     * 数据源
     *
     * @return
     */
    @Bean
    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){

        Class clzType = null;
        try {
            clzType = Class.forName(type);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        DataSource dataSource = DataSourceBuilder.create()
                .password(password)
                .url(url).username(username).password(password)
                .driverClassName(driverClassName).type(clzType)
                .build();
        try {
            System.out.println(dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }


    /**
     * 实体管理工厂
     *
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.zll.jpa.entity");
        return entityManagerFactory;
    }


    /**
     * 事务管理器
     *
     * @param entityManagerFactory
     * @return
     */
    @Bean
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }
}
