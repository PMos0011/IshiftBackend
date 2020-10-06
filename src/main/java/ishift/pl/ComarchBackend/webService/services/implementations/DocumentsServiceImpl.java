package ishift.pl.ComarchBackend.webService.services.implementations;


import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.DeclarationDetails;
import ishift.pl.ComarchBackend.dataModel.repository.DeclarationDataRepository;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.services.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentsServiceImpl implements DocumentsService {

    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final DeclarationDataRepository declarationDataRepository;

    @Autowired
    public DocumentsServiceImpl(WebCompanyDataRepository webCompanyDataRepository, DeclarationDataRepository declarationDataRepository) {
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
        this.declarationDataRepository = declarationDataRepository;
    }

    @Override
    public List<DeclarationData> getDeclarations(String key) {

        String databaseName = dataBasesPairListSingleton.getDBNameFromKey(key);
        ClientDatabaseContextHolder.set(databaseName);
        List<DeclarationData> declarationData = declarationDataRepository.findAll();
        ClientDatabaseContextHolder.clear();

        for (DeclarationData decl : declarationData) {
            List<DeclarationDetails> declarationDetails =
                    decl.getDeclarationDetails().stream()
                            .filter(this::detailFilter)
                            .filter(d->!d.getDescription().contains("Stawka "))
                            .collect(Collectors.toList());
            decl.setDeclarationDetails(declarationDetails);
        }

        return declarationData;
    }

    @Override
    public DeclarationData getDeclarationById(String key, Long id) {

        String databaseName = dataBasesPairListSingleton.getDBNameFromKey(key);
        ClientDatabaseContextHolder.set(databaseName);
        Optional<DeclarationData> declarationData = declarationDataRepository.findById(id);
        ClientDatabaseContextHolder.clear();

        return declarationData.get();
    }

    public boolean detailFilter(DeclarationDetails d) {

        try {
            String tmp = d.getValue().replace(",", "");
            double val = Double.parseDouble(tmp);

            return val > 0.0;

        } catch (Exception e) {
            return false;
        }
    }
}
