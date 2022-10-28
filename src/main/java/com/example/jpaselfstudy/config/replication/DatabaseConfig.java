package com.example.jpaselfstudy.config.replication;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableConfigurationProperties({MasterProperties.class, SlaveProperties.class})
public class DatabaseConfig {

    private final MasterProperties masterProperties;
    private final SlaveProperties slaveProperties;
    private JpaProperties jpaProperties;

    public DatabaseConfig(MasterProperties masterProperties, SlaveProperties slaveProperties,
                          JpaProperties jpaProperties) {
        this.masterProperties = masterProperties;
        this.slaveProperties = slaveProperties;
        this.jpaProperties = jpaProperties;
    }

    @Bean
    public DataSource writeDataSource() {
        return DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .url(masterProperties.getUrl())
                .driverClassName(masterProperties.getDriverClassName())
                .username(masterProperties.getUsername())
                .password(masterProperties.getPassword())
                .build();

    }

    @Bean
    public DataSource readDataSource() {
        return DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .url(slaveProperties.getUrl())
                .driverClassName(slaveProperties.getDriverClassName())
                .username(slaveProperties.getUsername())
                .password(slaveProperties.getPassword())
                .build();
    }

    @Bean
    public DataSource routingDataSource(@Qualifier("writeDataSource") DataSource writeDataSource,
                                        @Qualifier("readDataSource") DataSource readDataSource) {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<Object, Object>();
        dataSourceMap.put("write", writeDataSource);
        dataSourceMap.put("read", readDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(writeDataSource);

        return routingDataSource;
    }

    @Bean
    public DataSource routingLazyDataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        final LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy = new LazyConnectionDataSourceProxy(
                routingDataSource);
        return lazyConnectionDataSourceProxy;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
        final LocalContainerEntityManagerFactoryBean build = entityManagerFactoryBuilder.dataSource(
                        routingLazyDataSource(routingDataSource(writeDataSource(), readDataSource())))
                .packages("com.example.jpaselfstudy")
                .build();
        return build;
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(vendorAdapter, jpaProperties.getProperties(), null);
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier("routingLazyDataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
