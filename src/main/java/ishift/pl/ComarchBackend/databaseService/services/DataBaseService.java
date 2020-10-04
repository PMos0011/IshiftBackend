package ishift.pl.ComarchBackend.databaseService.services;


import java.sql.SQLException;
import java.util.List;

public interface DataBaseService {

    List<String> getAllDatabasesNameFromServer() throws SQLException;

}
