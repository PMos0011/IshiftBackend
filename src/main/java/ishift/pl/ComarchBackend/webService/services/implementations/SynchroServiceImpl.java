package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.dataModel.repository.*;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesListSingleton;
import ishift.pl.ComarchBackend.databaseService.services.DatabaseInitService;
import ishift.pl.ComarchBackend.webDataModel.services.AccountingOfficeService;
import ishift.pl.ComarchBackend.webDataModel.services.SwapService;
import ishift.pl.ComarchBackend.webDataModel.model.AccountingOffice;
import ishift.pl.ComarchBackend.webDataModel.model.UserData;
import ishift.pl.ComarchBackend.webDataModel.model.WebCompanyData;
import ishift.pl.ComarchBackend.webDataModel.services.UserDataService;
import ishift.pl.ComarchBackend.webDataModel.services.WebCompanyDataService;
import ishift.pl.ComarchBackend.webService.controllers.SynchroController;
import ishift.pl.ComarchBackend.webService.services.SynchroService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SynchroServiceImpl implements SynchroService {

    private final DatabaseInitService databaseInitService;
    private final SwapService swapService;
    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final DeclarationDetailsRepository declarationDetailsRepository;
    private final UserDataService userDataService;
    private final AccountingOfficeService accountingOfficeService;
    private final WebCompanyDataService webCompanyDataService;

    @Autowired
    public SynchroServiceImpl(DatabaseInitService databaseInitService,
                              SwapService swapService,
                              DeclarationDataRepository declarationDataRepository,
                              DeclarationDetailsRepository declarationDetailsRepository,
                              DataBaseAccess dataBaseAccess,
                              UserDataService userDataService,
                              AccountingOfficeService accountingOfficeService,
                              WebCompanyDataService webCompanyDataService) {

        this.databaseInitService = databaseInitService;
        this.swapService = swapService;
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(dataBaseAccess);
        this.declarationDataRepository = declarationDataRepository;
        this.declarationDetailsRepository = declarationDetailsRepository;
        this.userDataService = userDataService;
        this.accountingOfficeService = accountingOfficeService;
        this.webCompanyDataService = webCompanyDataService;
    }

    @Override
    public ResponseEntity<String> setNewClientData(TransferObject transferObject) {

        AtomicReference<String> newClientPassword = new AtomicReference<>();

        dataBasesListSingleton.getDatabasesList().stream()
                .filter(name -> transferObject.getDbName().equals(name))
                .findAny()
                .ifPresentOrElse((name) -> {
                            //todo
                            //exception handling
                            throw new RuntimeException("baza istnieje");
                        },
                        () -> {
                            System.out.println("Start synchro: " + transferObject.getDbName());

                            databaseInitService.createNewDatabaseAndAddToDataBasesListSingleton(transferObject.getDbName());
                            swapService.saveCompanyData(transferObject);

                            //todo
                            //exception handling
                            UserData officeUser = userDataService.getUserDataByLoginOrElseThrowRuntimeException(transferObject.getLogin());

                            AccountingOffice accountingOffice = accountingOfficeService.getAccountingOfficeDataByNameIfNotExistCreateNewAndSave(
                                    transferObject.getAccountancyName(), officeUser.getDbId());

                           WebCompanyData webCompanyData = webCompanyDataService.createNewWebCompanyDataAndSaveAndAddToPairListSingleton(
                                   transferObject.getCompanyName(),
                                   transferObject.getDbName(),
                                   accountingOffice.getRandomId());

                            newClientPassword.set(RandomStringUtils.randomAlphanumeric(8));

                            userDataService.createAndSaveNewUser(transferObject.getRegon(),
                                    newClientPassword.get(),
                                    webCompanyData.getRandomId());

                            if (!SynchroController.restart)
                                SynchroController.restart = true;
                        });

        return new ResponseEntity<>(newClientPassword.get(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> handleNewDeclarationData(List<DeclarationData> declarationData, String id) {

        if (SynchroController.restart) {
            swapService.saveDeclarationData(declarationData, id);
        } else {
            ClientDatabaseContextHolder.set(id);
            declarationData.forEach(data -> {
                declarationDetailsRepository.saveAll(data.getDeclarationDetails());
                declarationDataRepository.save(data);
            });
            ClientDatabaseContextHolder.clear();
        }

        //TODO
        //error handling
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
