package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.model.BankAccountData;
import ishift.pl.ComarchBackend.webService.services.BankAccountsControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankAccountsController {

    private final BankAccountsControllerService bankAccountsControllerService;

    public BankAccountsController(BankAccountsControllerService bankAccountsControllerService) {
        this.bankAccountsControllerService = bankAccountsControllerService;
    }

    @GetMapping("/bankAccounts/{id}")
    public ResponseEntity<List<BankAccountData>> getAllBankAccounts(@PathVariable String id) {

        return bankAccountsControllerService.getAllBankAccounts(id);
    }
}
