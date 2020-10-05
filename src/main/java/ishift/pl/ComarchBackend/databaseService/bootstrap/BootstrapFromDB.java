package ishift.pl.ComarchBackend.databaseService.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.repository.DeclarationDataRepository;
import ishift.pl.ComarchBackend.dataModel.repository.DeclarationDetailsRepository;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.configuration.DataBaseAccess;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesListSingleton;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.model.Swap;
import ishift.pl.ComarchBackend.webDataModel.repositiories.SwapRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class BootstrapFromDB implements CommandLineRunner {

    private final SwapRepository swapRepository;
    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final DeclarationDetailsRepository declarationDetailsRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;

    @Autowired
    public BootstrapFromDB(SwapRepository swapRepository, DeclarationDataRepository declarationDataRepository,
                           DeclarationDetailsRepository declarationDetailsRepository,
                           WebCompanyDataRepository webCompanyDataRepository,
                           DataBaseAccess dataBaseAccess) {
        this.swapRepository = swapRepository;
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(dataBaseAccess);
        this.declarationDataRepository = declarationDataRepository;
        this.declarationDetailsRepository = declarationDetailsRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println(dataBasesPairListSingleton.getDatabasesList());
        System.out.println("from database-service");

        saveDataToDatabase();

    }

    void saveDataToDatabase() {

        List<Swap> swapList = swapRepository.findAll();

        if (swapList.size() > 0) {
            swapList.forEach(swap -> {

                Optional<String> existingDB = dataBasesListSingleton.getDatabasesList().stream()
                        .filter(existingName -> swap.getDatabaseName().equals(existingName))
                        .findAny();

                existingDB.map(name -> {
                    ClientDatabaseContextHolder.set(name);
                    System.out.println(name);
                    try {
                        List<DeclarationData> decl = new ObjectMapper().readValue(swap.getCustomerData(), new TypeReference<List<DeclarationData>>() {
                        });
                        decl.forEach(d -> {
                            declarationDetailsRepository.saveAll(d.getDeclarationDetails());
                            declarationDataRepository.save(d);
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ClientDatabaseContextHolder.clear();
                    swapRepository.delete(swap);
                    return name;

                }).orElseGet(()-> {
                    swapRepository.delete(swap);
                    //TODO
                    System.out.println("error getting data from swap");
                    return null;
                });
            });
        }
    }

}