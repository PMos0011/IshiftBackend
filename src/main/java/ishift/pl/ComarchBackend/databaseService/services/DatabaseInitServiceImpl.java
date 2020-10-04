package ishift.pl.ComarchBackend.databaseService.services;

import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
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

    @Autowired
    public DatabaseInitServiceImpl(DataBaseAccess dataBaseAccess) {
        this.dataBaseAccess = dataBaseAccess;
    }

    @Override
    public void createNewDatabase(String dbName) {
        try (Connection conn = DriverManager.getConnection(
                dataBaseAccess.getUrl(),
                dataBaseAccess.getUser(),
                dataBaseAccess.getPassword())) {
            Statement stmt = conn.createStatement();
            String sql = "CREATE DATABASE " + dbName;
            stmt.executeUpdate(sql);
            stmt.close();
            createTable(dbName, readTableQueryData());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void createTable(String databaseName, List<String> tableQuery) throws SQLException {
        String databaseUrl = "jdbc:mysql://localhost:3306/" + databaseName + "?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=TRUE";
        Connection conn = DriverManager.getConnection(databaseUrl,
                dataBaseAccess.getUser(),
                dataBaseAccess.getPassword());
        Statement stmt = conn.createStatement();

        tableQuery.forEach(query -> {
            try {
                stmt.execute(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        stmt.close();
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
            return null;
        }

        return queryList;
    }
}
