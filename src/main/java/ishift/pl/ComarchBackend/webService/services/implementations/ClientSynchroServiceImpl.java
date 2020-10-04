package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.dataModel.repository.DeclarationDataRepository;
import ishift.pl.ComarchBackend.dataModel.repository.DeclarationDetailsRepository;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesListSingleton;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.databaseService.services.DatabaseInitService;
import ishift.pl.ComarchBackend.databaseService.services.SwapDataService;
import ishift.pl.ComarchBackend.webDataModel.model.WebCompanyData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.controllers.ClientSynchroController;
import ishift.pl.ComarchBackend.webService.services.ClientSynchroService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientSynchroServiceImpl implements ClientSynchroService {

    private final DatabaseInitService databaseInitService;
    private final SwapDataService swapDataService;
    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final DeclarationDetailsRepository declarationDetailsRepository;
    private final WebCompanyDataRepository webCompanyDataRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;

    @Autowired
    public ClientSynchroServiceImpl(DatabaseInitService databaseInitService,
                                    SwapDataService swapDataService,
                                    DeclarationDataRepository declarationDataRepository,
                                    DeclarationDetailsRepository declarationDetailsRepository,
                                    WebCompanyDataRepository webCompanyDataRepository,
                                    DataBaseAccess dataBaseAccess) {
        this.databaseInitService = databaseInitService;
        this.swapDataService = swapDataService;
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(dataBaseAccess);
        this.declarationDataRepository = declarationDataRepository;
        this.declarationDetailsRepository = declarationDetailsRepository;
        this.webCompanyDataRepository = webCompanyDataRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
    }

    @Override
    public void handleIncomingData(TransferObject transferObject) {

        Optional<String> existingDB = Optional.ofNullable(

                dataBasesListSingleton.getDatabasesList().stream()
                        .filter(name -> transferObject.getDbName().equals(name))
                        .findAny()
                        .orElseGet(() -> {

                            System.out.println("Start synchro: "+ transferObject.getDbName());
                            databaseInitService.createNewDatabase(transferObject.getDbName());
                            swapDataService.SaveCompanyData(transferObject);
                            dataBasesListSingleton.getDatabasesList().add(transferObject.getDbName());

                            WebCompanyData webCompanyData = new WebCompanyData(transferObject.getCompanyName(),
                                    transferObject.getDbName());

                            webCompanyDataRepository.save(webCompanyData);

                            dataBasesPairListSingleton.getDatabasesList().add(
                                    new MutablePair<String, String>(webCompanyData.getRandomId(), webCompanyData.getDbName()));

                            if (!ClientSynchroController.restart)
                                ClientSynchroController.restart = true;

                            return null;
                        }));

        existingDB.ifPresent(name -> {
            System.out.println("start copping: " + name);
            transferObject.getDeclarationData().forEach(declaration -> {
                ClientDatabaseContextHolder.set(name);
                declarationDetailsRepository.saveAll(declaration.getDeclarationDetails());
                declarationDataRepository.save(declaration);
                ClientDatabaseContextHolder.clear();
            });
        });
    }
}
