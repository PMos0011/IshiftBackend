package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.dataModel.repository.CompanyDataRepository;
import ishift.pl.ComarchBackend.dataModel.repository.DeclarationDataRepository;
import ishift.pl.ComarchBackend.dataModel.repository.DeclarationDetailsRepository;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesListSingleton;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.databaseService.services.DatabaseInitService;
import ishift.pl.ComarchBackend.webDataModel.services.SwapDataService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SynchroServiceImpl implements SynchroService {

    private final DatabaseInitService databaseInitService;
    private final SwapDataService swapDataService;
    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final DeclarationDetailsRepository declarationDetailsRepository;
    private final WebCompanyDataRepository webCompanyDataRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final AccountingOfficeRepository accountingOfficeRepository;
    private final UserDataRepository userDataRepository;
    private final CompanyDataRepository companyDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SynchroServiceImpl(DatabaseInitService databaseInitService,
                              SwapDataService swapDataService,
                              DeclarationDataRepository declarationDataRepository,
                              DeclarationDetailsRepository declarationDetailsRepository,
                              WebCompanyDataRepository webCompanyDataRepository,
                              DataBaseAccess dataBaseAccess,
                              AccountingOfficeRepository accountingOfficeRepository,
                              UserDataRepository userDataRepository,
                              CompanyDataRepository companyDataRepository,
                              PasswordEncoder passwordEncoder) {

        this.databaseInitService = databaseInitService;
        this.swapDataService = swapDataService;
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(dataBaseAccess);
        this.declarationDataRepository = declarationDataRepository;
        this.declarationDetailsRepository = declarationDetailsRepository;
        this.webCompanyDataRepository = webCompanyDataRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
        this.accountingOfficeRepository = accountingOfficeRepository;
        this.userDataRepository = userDataRepository;
        this.companyDataRepository = companyDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String handleIncomingData(TransferObject transferObject) {

        AtomicReference<String> newClientPassword = new AtomicReference<>();

        Optional<String> existingDB = Optional.ofNullable(

                dataBasesListSingleton.getDatabasesList().stream()
                        .filter(name -> transferObject.getDbName().equals(name))
                        .findAny()
                        .orElseGet(() -> {

                            System.out.println("Start synchro: " + transferObject.getDbName());

                            databaseInitService.createNewDatabase(transferObject.getDbName());
                            swapDataService.saveCompanyData(transferObject);
                            dataBasesListSingleton.getDatabasesList().add(transferObject.getDbName());

                            //TODO orElse(null)
                            UserData officeUser = userDataRepository.findByUserName(transferObject.getLogin()).orElse(null);

                            Optional<AccountingOffice> accountingOfficeOptional =accountingOfficeRepository.findByName(transferObject.getAccountancyName());

                            AccountingOffice accountingOffice =
                                    accountingOfficeOptional.orElse(new AccountingOffice(transferObject.getAccountancyName(),officeUser.getDbId()));

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

        existingDB.ifPresent(name -> {
            System.out.println("start copping: " + name);

            ClientDatabaseContextHolder.set(name);
            companyDataRepository.saveAll(transferObject.getCompanyData());

            transferObject.getDeclarationData().forEach(declaration -> {
                declarationDetailsRepository.saveAll(declaration.getDeclarationDetails());
                declarationDataRepository.save(declaration);
            });

            ClientDatabaseContextHolder.clear();
        });

        return newClientPassword.get();
    }

}
