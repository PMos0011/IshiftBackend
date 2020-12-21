package ishift.pl.ComarchBackend.databaseService.configuration;

import com.zaxxer.hikari.HikariDataSource;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesListSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RoutingConfiguration {

    private final DataBasesListSingleton dataBasesListSingleton;
    private final DataBaseAccess dataBaseAccess;

    @Autowired
    public RoutingConfiguration(DataBaseAccess dataBaseAccess) throws SQLException {
        dataBasesListSingleton = DataBasesListSingleton.getInstance(dataBaseAccess);
        this.dataBaseAccess = dataBaseAccess;
    }

    @Bean
    public DataSource getDataSource() {

        Map<Object, Object> targetDataSources = new HashMap<>();
        HikariDataSource client0Datasource = clientADatasource("configuration", false);

        System.out.println(dataBasesListSingleton.getDatabasesList());

        dataBasesListSingleton.getDatabasesList().forEach(name -> {
            HikariDataSource clientADatasource = clientADatasource(name, true);
            targetDataSources.put(name,
                    clientADatasource);
        });

        ClientDataSourceRouter clientRoutingDatasource
                = new ClientDataSourceRouter();
        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(client0Datasource);
        return clientRoutingDatasource;
    }

    private HikariDataSource clientADatasource(String databaseName, boolean connectionsLimit) {

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mariadb://localhost:3306/" + databaseName);
        ds.setUsername(dataBaseAccess.getUser());
        ds.setPassword(dataBaseAccess.getPassword());
        if (connectionsLimit) {
            ds.setMaximumPoolSize(2);
            ds.setMinimumIdle(0);
            ds.setIdleTimeout(600000);
        }

        return ds;

    }
}
