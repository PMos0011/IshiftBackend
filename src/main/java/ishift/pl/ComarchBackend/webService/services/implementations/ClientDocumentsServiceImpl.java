package ishift.pl.ComarchBackend.webService.services.implementations;


import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.repository.DeclarationDataRepository;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.services.ClientDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientDocumentsServiceImpl implements ClientDocumentsService {

    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final DeclarationDataRepository declarationDataRepository;

    @Autowired
    public ClientDocumentsServiceImpl(WebCompanyDataRepository webCompanyDataRepository, DeclarationDataRepository declarationDataRepository) {
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
        this.declarationDataRepository = declarationDataRepository;
    }

    @Override
    public List<DeclarationData> getDeclarations(String key) {

        String databaseName = dataBasesPairListSingleton.getDBNameFromKey(key);
        ClientDatabaseContextHolder.set(databaseName);
        System.out.println(dataBasesPairListSingleton.getDBNameFromKey(key));
        List<DeclarationData> declarationData = declarationDataRepository.findAll();
        ClientDatabaseContextHolder.clear();

        return declarationData;
    }
}
