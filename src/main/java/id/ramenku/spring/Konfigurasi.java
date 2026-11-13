package id.ramenku.spring;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import id.ramenku.Koneksi;
import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Konfigurasi Spring untuk RamenKu. Di sinilah Spring diberi tahu cara membuat objek-objek utama:
 * sumber koneksi MySQL, SessionFactory milik Hibernate, dan pengelola transaksi. Semua DAO yang
 * bertanda @Repository di paket id.ramenku.data ditemukan dan dikelola otomatis.
 */
@Configuration
@ComponentScan(basePackages = {"id.ramenku.data", "id.ramenku.spring"})
@EnableTransactionManagement(proxyTargetClass = true)   // @Transactional = transaksi deklaratif
@EnableAspectJAutoProxy(proxyTargetClass = true)        // AOP: aspek PenerjemahGalat
public class Konfigurasi {

    /** Koneksi ke MySQL (kolam koneksi HikariCP). Pengaturannya dibaca dari koneksi.properties. */
    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        Properties p = Koneksi.pengaturan();
        HikariConfig c = new HikariConfig();
        c.setPoolName("ramenku");
        c.setDriverClassName("com.mysql.cj.jdbc.Driver");
        c.setJdbcUrl(p.getProperty("url"));
        c.setUsername(p.getProperty("user"));
        c.setPassword(p.getProperty("password"));
        c.setMaximumPoolSize(5);
        c.setInitializationFailTimeout(-1);             // aplikasi tetap jalan walau MySQL belum menyala
        return new HikariDataSource(c);
    }

    /** Hibernate sebagai penyedia JPA; Spring yang membuat dan menyimpan SessionFactory-nya. */
    @Bean
    @Primary                                            // dipilih bila ada lebih dari satu EntityManagerFactory
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        Properties p = Koneksi.pengaturan();
        LocalContainerEntityManagerFactoryBean f = new LocalContainerEntityManagerFactoryBean();
        f.setDataSource(dataSource);
        f.setPackagesToScan("id.ramenku");              // mencari class yang bertanda @Entity
        f.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties j = new Properties();
        // XAMPP memakai MariaDB; dialek dapat diganti lewat koneksi.properties (kunci dialect)
        j.setProperty("hibernate.dialect", p.getProperty("dialect", "org.hibernate.dialect.MariaDBDialect"));
        j.setProperty("hibernate.boot.allow_jdbc_metadata_access", "false");
        j.setProperty("hibernate.hbm2ddl.auto", "none");            // tabel dibuat dari database/ramenku_db.sql
        j.setProperty("hibernate.show_sql", System.getProperty("ramenku.sql", "false"));
        f.setJpaProperties(j);
        return f;
    }

    /** SessionFactory milik Hibernate yang dikelola Spring (dapat disuntikkan bila dibutuhkan). */
    @Bean
    public SessionFactory sessionFactory(EntityManagerFactory emf) {
        return emf.unwrap(SessionFactory.class);
    }

    /** Pengelola transaksi: commit bila berhasil, rollback bila terjadi galat. */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    /** Mengubah galat Hibernate pada @Repository menjadi exception Spring (DataAccessException). */
    @Bean
    public static PersistenceExceptionTranslationPostProcessor penerjemahPersistence() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
