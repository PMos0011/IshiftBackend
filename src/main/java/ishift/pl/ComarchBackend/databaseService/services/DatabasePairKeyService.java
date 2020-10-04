package ishift.pl.ComarchBackend.databaseService.services;



import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface DatabasePairKeyService {

    List<Pair<String,String>> createDatabaseKeyPairList();
}
