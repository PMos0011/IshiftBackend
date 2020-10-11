package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.model.AccountingOffice;
import ishift.pl.ComarchBackend.webDataModel.services.AccountingOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AccountingOfficeController {

    private final AccountingOfficeService accountingOfficeService;

    @Autowired
    public AccountingOfficeController(AccountingOfficeService accountingOfficeService) {
        this.accountingOfficeService = accountingOfficeService;
    }

    @GetMapping("/accOffice/{id}")
    public ResponseEntity<AccountingOffice> getAllCompanyName(@PathVariable String id) {

        return new ResponseEntity<>(accountingOfficeService.getAccountingOfficeDataByIdOrThrowRuntimeException(id), HttpStatus.OK);
    }
}
