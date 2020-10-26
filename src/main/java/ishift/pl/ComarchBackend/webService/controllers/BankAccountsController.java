package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.model.BankAccountData;
import ishift.pl.ComarchBackend.webService.services.BankAccountsControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountsController {

    private final BankAccountsControllerService bankAccountsControllerService;

    @Autowired
    public BankAccountsController(BankAccountsControllerService bankAccountsControllerService) {
        this.bankAccountsControllerService = bankAccountsControllerService;
    }

    @GetMapping("/bankAccounts/{id}")
    public ResponseEntity<List<BankAccountData>> getAllBankAccounts(@PathVariable String id) {

        return bankAccountsControllerService.getAllBankAccounts(id);
    }

    @PutMapping("/bankAccounts/{id}")
    public ResponseEntity<List<BankAccountData>> saveBankAccount(@PathVariable String id, @RequestBody BankAccountData bankAccountData) {

        return bankAccountsControllerService.saveBankAccount(bankAccountData, id);
    }

    @DeleteMapping("/bankAccounts/{dbId}/{id}")
    public ResponseEntity<List<BankAccountData>> deleteBankAccount(@PathVariable String dbId, @PathVariable Long id) {

        return bankAccountsControllerService.deleteBankAccount(dbId, id);
    }
}
