package ishift.pl.ComarchBackend.databaseService.services;

import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseServiceImpl implements DataBaseService {

    private final DataBaseAccess dataBaseAccess;

    public DataBaseServiceImpl(DataBaseAccess dataBaseAccess) {
        this.dataBaseAccess = dataBaseAccess;
    }

    @Override
    public List<String> getAllDatabasesNameFromServer() throws SQLException {

        Connection connection = DriverManager.getConnection(
                dataBaseAccess.getUrl(),
                dataBaseAccess.getUser(),
                dataBaseAccess.getPassword());

        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet result = metadata.getCatalogs();

        List<String> dbNames = new ArrayList<>();

        while (result.next())
            if (result.getString(1).contains(dataBaseAccess.getPrefix()))
                dbNames.add(result.getString(1));

        return dbNames;

    }
}
