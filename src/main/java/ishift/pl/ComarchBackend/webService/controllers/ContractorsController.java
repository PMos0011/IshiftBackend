package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.model.WebContactor;
import ishift.pl.ComarchBackend.webService.services.ContractorControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContractorsController {

    private final ContractorControllerService contractorControllerService;

    @Autowired
    public ContractorsController(ContractorControllerService contractorControllerService) {
        this.contractorControllerService = contractorControllerService;
    }

    @GetMapping("/contractors/{id}")
    public ResponseEntity<List<WebContactor>> getAllBankAccounts(@PathVariable String id) {

        return contractorControllerService.getAllWebContractors(id);
    }

    @PutMapping("/contractors/{id}")
    public ResponseEntity<List<WebContactor>> saveBankAccount(@PathVariable String id, @RequestBody WebContactor webContactor) {

        return contractorControllerService.saveWebContractor(webContactor,id);
    }

    @DeleteMapping("/contractors/{dbId}/{id}")
    public ResponseEntity<List<WebContactor>> deleteBankAccount(@PathVariable String dbId, @PathVariable Long id) {

        return contractorControllerService.deleteWebContractor(dbId, id);
    }
}
