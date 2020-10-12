package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.model.BankAccountData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.BankAccountDataRepository;
import ishift.pl.ComarchBackend.webService.services.BankAccountsControllerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountsControllerServiceImpl implements BankAccountsControllerService {

    private final BankAccountDataRepository bankAccountDataRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;

    public BankAccountsControllerServiceImpl(BankAccountDataRepository bankAccountDataRepository) {
        this.bankAccountDataRepository = bankAccountDataRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(null);
    }

    @Override
    public ResponseEntity<List<BankAccountData>> getAllBankAccounts(String id) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        List<BankAccountData> bankAccountList = bankAccountDataRepository.findAll();
        ClientDatabaseContextHolder.clear();

        return new ResponseEntity<>(bankAccountList, HttpStatus.OK);
    }
}
