package ishift.pl.ComarchBackend.databaseService.configuration;

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
    public DataSource getDataSource()  {

        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource client0Datasource = clientADatasource("configuration");

        System.out.println(dataBasesListSingleton.getDatabasesList());

        dataBasesListSingleton.getDatabasesList().forEach(name -> {
            DataSource clientADatasource = clientADatasource(name);
            targetDataSources.put(name,
                    clientADatasource);
        });

        ClientDataSourceRouter clientRoutingDatasource
                = new ClientDataSourceRouter();
        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(client0Datasource);
        return clientRoutingDatasource;
    }

    private DataSource clientADatasource(String databaseName) {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/" + databaseName + "?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false");
        dataSourceBuilder.username(dataBaseAccess.getUser());
        dataSourceBuilder.password(dataBaseAccess.getPassword());
        return dataSourceBuilder.build();
    }
}
