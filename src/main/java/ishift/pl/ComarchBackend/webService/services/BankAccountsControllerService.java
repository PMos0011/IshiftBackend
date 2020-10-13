package ishift.pl.ComarchBackend.webService.services;

import ishift.pl.ComarchBackend.webDataModel.model.BankAccountData;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BankAccountsControllerService {

    ResponseEntity<List<BankAccountData>> getAllBankAccounts(String id);

    ResponseEntity<List<BankAccountData>> saveBankAccount(BankAccountData bankAccountData, String dbId);

    ResponseEntity<List<BankAccountData>> deleteBankAccount(String dbId, Long id);
}
