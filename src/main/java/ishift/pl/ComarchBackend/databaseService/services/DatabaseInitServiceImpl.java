package ishift.pl.ComarchBackend.databaseService.services;

import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesListSingleton;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseInitServiceImpl implements DatabaseInitService {

    private final DataBaseAccess dataBaseAccess;
    private final DataBasesListSingleton dataBasesListSingleton;

    @Autowired
    public DatabaseInitServiceImpl(DataBaseAccess dataBaseAccess) {
        this.dataBaseAccess = dataBaseAccess;
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(dataBaseAccess);
    }

    @Override
    public void createNewDatabaseAndAddToDataBasesListSingleton(String dbName) {

        try (Connection conn = DriverManager.getConnection(
                dataBaseAccess.getUrl(),
                dataBaseAccess.getUser(),
                dataBaseAccess.getPassword())) {
            Statement stmt = conn.createStatement();
            String sql = "CREATE DATABASE " + dbName;
            stmt.executeUpdate(sql);
            stmt.close();
            createTable(dbName, readTableQueryData());

            dataBasesListSingleton.getDatabasesList().add(dbName);

        } catch (SQLException throwables) {
            //todo
            throwables.printStackTrace();
        }

    }

    private void createTable(String databaseName, List<String> tableQuery) throws SQLException {
        String databaseUrl = "jdbc:mariadb://localhost:3306/" + databaseName;
        try (Connection conn = DriverManager.getConnection(databaseUrl,
                dataBaseAccess.getUser(),
                dataBaseAccess.getPassword())) {

            Statement stmt = conn.createStatement();

            tableQuery.forEach(query -> {
                try {
                    stmt.execute(query);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            stmt.close();

        } catch (SQLException throwables) {
            //todo
            throwables.printStackTrace();
        }
    }

    private List<String> readTableQueryData() {
        List<String> queryList = new ArrayList<>();

        try {
            InputStream resource = new ClassPathResource("schema.sql").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));

            String line;
            while ((line = reader.readLine()) != null)
                queryList.add(line);

        } catch (Exception e) {
            //todo
            return null;
        }

        return queryList;
    }
}
