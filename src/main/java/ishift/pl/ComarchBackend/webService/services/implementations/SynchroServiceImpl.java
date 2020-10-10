package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.dataModel.repository.*;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesListSingleton;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.databaseService.services.DatabaseInitService;
import ishift.pl.ComarchBackend.webDataModel.services.SwapService;
import ishift.pl.ComarchBackend.webDataModel.model.AccountingOffice;
import ishift.pl.ComarchBackend.webDataModel.model.UserData;
import ishift.pl.ComarchBackend.webDataModel.model.WebCompanyData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.AccountingOfficeRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.UserDataRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.controllers.SynchroController;
import ishift.pl.ComarchBackend.webService.services.SynchroService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SynchroServiceImpl implements SynchroService {

    private final DatabaseInitService databaseInitService;
    private final SwapService swapService;
    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final DeclarationDetailsRepository declarationDetailsRepository;
    private final WebCompanyDataRepository webCompanyDataRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final AccountingOfficeRepository accountingOfficeRepository;
    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SynchroServiceImpl(DatabaseInitService databaseInitService,
                              SwapService swapService,
                              DeclarationDataRepository declarationDataRepository,
                              DeclarationDetailsRepository declarationDetailsRepository,
                              WebCompanyDataRepository webCompanyDataRepository,
                              DataBaseAccess dataBaseAccess,
                              AccountingOfficeRepository accountingOfficeRepository,
                              UserDataRepository userDataRepository,
                              PasswordEncoder passwordEncoder) {

        this.databaseInitService = databaseInitService;
        this.swapService = swapService;
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(dataBaseAccess);
        this.declarationDataRepository = declarationDataRepository;
        this.declarationDetailsRepository = declarationDetailsRepository;
        this.webCompanyDataRepository = webCompanyDataRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
        this.accountingOfficeRepository = accountingOfficeRepository;
        this.userDataRepository = userDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<String> setNewClientData(TransferObject transferObject) {

        AtomicReference<String> newClientPassword = new AtomicReference<>();

        Optional<String> existingDB = Optional.ofNullable(

                dataBasesListSingleton.getDatabasesList().stream()
                        .filter(name -> transferObject.getDbName().equals(name))
                        .findAny()
                        .orElseGet(() -> {

                            System.out.println("Start synchro: " + transferObject.getDbName());

                            databaseInitService.createNewDatabase(transferObject.getDbName());
                            swapService.saveCompanyData(transferObject);
                            dataBasesListSingleton.getDatabasesList().add(transferObject.getDbName());

                            //TODO orElse(null)
                            UserData officeUser = userDataRepository.findByUserName(transferObject.getLogin()).orElse(null);

                            Optional<AccountingOffice> accountingOfficeOptional = accountingOfficeRepository.findByName(transferObject.getAccountancyName());

                            AccountingOffice accountingOffice =
                                    accountingOfficeOptional.orElse(new AccountingOffice(transferObject.getAccountancyName(), officeUser.getDbId()));

                            WebCompanyData webCompanyData = new WebCompanyData(transferObject.getCompanyName(),
                                    transferObject.getDbName(), accountingOffice.getRandomId());

                            accountingOfficeRepository.save(accountingOffice);
                            webCompanyDataRepository.save(webCompanyData);

                            dataBasesPairListSingleton.getDatabasesList().add(
                                    new MutablePair<>(webCompanyData.getRandomId(), webCompanyData.getDbName()));

                            newClientPassword.set(RandomStringUtils.randomAlphanumeric(8));

                            UserData userData = new UserData(transferObject.getRegon(),
                                    passwordEncoder.encode(newClientPassword.get()),
                                    "ROLE_USER",
                                    webCompanyData.getRandomId());

                            userDataRepository.save(userData);

                            if (!SynchroController.restart)
                                SynchroController.restart = true;

                            return null;
                        }));

        existingDB.ifPresent((name) -> {
            throw new RuntimeException("baza istnieje");
        });

        //TODO
        //error response
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
