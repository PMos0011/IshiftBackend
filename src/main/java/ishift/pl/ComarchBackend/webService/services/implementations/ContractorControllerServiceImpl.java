package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.model.WebContactor;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebContractorRepository;
import ishift.pl.ComarchBackend.webService.services.ContractorControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractorControllerServiceImpl implements ContractorControllerService {

    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final WebContractorRepository webContractorRepository;

    @Autowired
    public ContractorControllerServiceImpl(WebContractorRepository webContractorRepository) {
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(null);
        this.webContractorRepository = webContractorRepository;
    }

    @Override
    public ResponseEntity<List<WebContactor>> getAllWebContractors(String id) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        List<WebContactor> webContactorList = webContractorRepository.findAll();
        ClientDatabaseContextHolder.clear();

        return new ResponseEntity<>(webContactorList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WebContactor>> saveWebContractor(WebContactor webContactor, String dbId) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(dbId));
        webContractorRepository.save(webContactor);
        ClientDatabaseContextHolder.clear();

        return getAllWebContractors(dbId);
    }

    @Override
    public ResponseEntity<List<WebContactor>> deleteWebContractor(String dbId, Long id) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(dbId));
        webContractorRepository.deleteById(id);
        ClientDatabaseContextHolder.clear();

        return getAllWebContractors(dbId);
    }
}
