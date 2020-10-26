package ishift.pl.ComarchBackend.databaseService.data;


import ishift.pl.ComarchBackend.databaseService.services.DatabasePairKeyServiceImpl;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DataBasesPairListSingleton {

    private static DataBasesPairListSingleton instance = null;
    private final List<Pair<String,String>> databasesList;

    private DataBasesPairListSingleton(List<Pair<String,String>> databasesList){
        this.databasesList=databasesList;
    };

    public static DataBasesPairListSingleton getInstance(WebCompanyDataRepository webCompanyDataRepository){
        if(instance==null){
            instance = new DataBasesPairListSingleton(new DatabasePairKeyServiceImpl(webCompanyDataRepository).createDatabaseKeyPairList());
        }
        return instance;
    }

    public List<Pair<String,String>> getDatabasesList() {
        return databasesList;
    }

    public String getFirstDatabaseName(){
                return databasesList.get(1).getValue();
    }

    public String getDBNameFromKey(String key){

        return databasesList.stream()
                .filter(k->k.getKey().equals(key))
                .findAny()
                .orElseThrow(()->new RuntimeException("problem klucza"))
                .getValue();
    }
}
