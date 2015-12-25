package com.zzcm.tmp.init;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Administrator on 2015/12/25.
 */
@Configuration
@ComponentScan("com.zzcm.tmp")
@EnableWebMvc
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:db.properties")
@Import({MvcConfig.class})
public class WebAppConfig {

    private static final Logger LOG = LoggerFactory.getLogger(WebAppConfig.class);

    private static final String P_DB_DRIVER = "db.driver";
    private static final String P_DB_PASSWORD = "db.password";
    private static final String P_DB_URL = "db.url";
    private static final String P_DB_USERNAME = "db.username";

    private static final String P_DB_MAXACTIVE = "db.maxActive";
    private static final String P_DB_INITSIZE = "db.initialSize";
    private static final String P_DB_MAXWAIT = "db.maxWait";
    private static final String P_DB_MINIDLE = "db.minIdle";
    private static final String P_DB_TBERM = "db.timeBetweenEvictionRunsMillis";
    private static final String P_DB_MEITM = "db.minEvictableIdleTimeMillis";
    private static final String P_DB_VQ = "db.validationQuery";
    private static final String P_DB_TESTWHILEIDEL = "db.testWhileIdle";
    private static final String P_DB_TESTONBORROW = "db.testOnBorrow";
    private static final String P_DB_TESTONRETURN = "db.testOnReturn";
    private static final String P_DB_FILTERS = "db.filters";


    private static final String P_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String P_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String P_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
    private static final String P_HIBERNATE_MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
    private static final String P_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
    private static final String P_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String P_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String P_HIBERNATE_CACHE_P_CLASS = "hibernate.cache.provider_class";
    private static final String P_PERSISTENCE_VALI_MODE = "javax.persistence.validation.mode";
    private static final String P_HIBERNATE_EJB_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";

    @Resource
    private Environment env;


    @Bean
    public UrlBasedViewResolver setupViewResolver(){
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }

    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty(P_DB_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(P_DB_URL));
        dataSource.setUsername(env.getRequiredProperty(P_DB_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(P_DB_PASSWORD));

        dataSource.setMaxActive(env.getRequiredProperty(P_DB_MAXACTIVE, Integer.class));
        dataSource.setInitialSize(env.getRequiredProperty(P_DB_INITSIZE, Integer.class));
        dataSource.setMaxWait(env.getRequiredProperty(P_DB_MAXWAIT, Integer.class));
        dataSource.setMinIdle(env.getRequiredProperty(P_DB_MINIDLE, Integer.class));
        dataSource.setTimeBetweenEvictionRunsMillis(env.getRequiredProperty(P_DB_TBERM, Integer.class));
        dataSource.setMinEvictableIdleTimeMillis(env.getRequiredProperty(P_DB_MEITM, Long.class));
        dataSource.setValidationQuery(env.getRequiredProperty(P_DB_VQ));
        dataSource.setTestWhileIdle(env.getRequiredProperty(P_DB_TESTWHILEIDEL, Boolean.class));
        dataSource.setTestOnBorrow(env.getRequiredProperty(P_DB_TESTONBORROW, Boolean.class));
        dataSource.setTestOnReturn(env.getRequiredProperty(P_DB_TESTONRETURN, Boolean.class));
        try {
            dataSource.setFilters(env.getRequiredProperty(P_DB_FILTERS));
        } catch (SQLException e) {
            LOG.error("Create DataSource filters failed.",e);
        }
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPackagesToScan(env.getRequiredProperty(P_ENTITYMANAGER_PACKAGES_TO_SCAN));
        //b.setPersistenceUnitName("mysqldb");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(env.getRequiredProperty(P_HIBERNATE_SHOW_SQL,Boolean.class));
        adapter.setDatabasePlatform(env.getRequiredProperty(P_HIBERNATE_DIALECT));
        bean.setJpaVendorAdapter(adapter);
        bean.setJpaProperties(jpaProperties());
        return bean;
    }

    private Properties jpaProperties(){
        Properties prop = new Properties();
        prop.put(P_HIBERNATE_MAX_FETCH_DEPTH, env.getRequiredProperty(P_HIBERNATE_MAX_FETCH_DEPTH, Integer.class));
        prop.put(P_HIBERNATE_JDBC_FETCH_SIZE, env.getRequiredProperty(P_HIBERNATE_JDBC_FETCH_SIZE, Integer.class));
        prop.put(P_HIBERNATE_JDBC_BATCH_SIZE, env.getRequiredProperty(P_HIBERNATE_JDBC_BATCH_SIZE, Integer.class));
        prop.put(P_HIBERNATE_SHOW_SQL, env.getRequiredProperty(P_HIBERNATE_SHOW_SQL, Boolean.class));
        prop.put(P_HIBERNATE_FORMAT_SQL, env.getRequiredProperty(P_HIBERNATE_FORMAT_SQL, Boolean.class));
        prop.put(P_HIBERNATE_CACHE_P_CLASS, env.getRequiredProperty(P_HIBERNATE_CACHE_P_CLASS));
        prop.put(P_PERSISTENCE_VALI_MODE, env.getRequiredProperty(P_PERSISTENCE_VALI_MODE));
        prop.put(P_HIBERNATE_EJB_NAMING_STRATEGY, env.getRequiredProperty(P_HIBERNATE_EJB_NAMING_STRATEGY));

        return prop;
    }

    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        return new SchedulerFactoryBean();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(104857600);
        return resolver;
    }

    @Bean
    public LocalValidatorFactoryBean validator(){
        return new LocalValidatorFactoryBean();
    }
}
