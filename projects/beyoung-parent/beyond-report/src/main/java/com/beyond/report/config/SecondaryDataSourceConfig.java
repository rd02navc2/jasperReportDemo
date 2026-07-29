package com.beyond.report.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.beyond.permission.repository",
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    transactionManagerRef = "ERP_TM"
)
public class SecondaryDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.secondary")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource() {
        return secondaryDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder, 
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.beyond.permission.entity") // 指向 permission 的 Entity 套件
                .persistenceUnit("secondary")
                .build();
    }

    /**
     *  關鍵：將 JPA EntityManagerFactory 解包為原生的 Hibernate SessionFactory
     * Bean 名稱命名為 ERPSF，供舊有 Service 注入或呼叫
     */
    @Bean(name = "ERPSF")
    public SessionFactory erpSessionFactory(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("無法解包 EntityManagerFactory 為 SessionFactory");
        }
        return entityManagerFactory.unwrap(SessionFactory.class);
    }

    /**
     * 配置專屬於 ERP / PermissionDB 的 TransactionManager
     * 命名為 ERP_TM，完美匹配 @Transactional("ERP_TM")
     */
    @Bean(name = "ERP_TM")
    public PlatformTransactionManager erpTransactionManager(
            @Qualifier("ERPSF") SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}