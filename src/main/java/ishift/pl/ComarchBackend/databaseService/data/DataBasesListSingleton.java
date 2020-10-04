package ishift.pl.ComarchBackend.databaseService.data;

import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
import ishift.pl.ComarchBackend.databaseService.services.DataBaseServiceImpl;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class DataBasesListSingleton {

    private static DataBasesListSingleton instance = null;
    private final List<String> databasesList;

    private DataBasesListSingleton(List<String> databasesList){
        this.databasesList=databasesList;
    };

    public static DataBasesListSingleton getInstance(DataBaseAccess dataBaseAccess){
        if(instance==null){
            try {
                instance = new DataBasesListSingleton(new DataBaseServiceImpl(dataBaseAccess).getAllDatabasesNameFromServer());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return instance;
    }

    public List<String> getDatabasesList() {
        return databasesList;
    }
}
