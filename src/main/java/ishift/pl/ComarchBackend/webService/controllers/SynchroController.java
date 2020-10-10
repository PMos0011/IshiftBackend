package ishift.pl.ComarchBackend.webService.controllers;


import ishift.pl.ComarchBackend.ComarchBackendApplication;
import ishift.pl.ComarchBackend.dataModel.model.BankAccount;
import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.webService.services.SynchroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SynchroController {

    private final SynchroService synchroService;
    public static boolean restart;

    @Autowired
    public SynchroController(SynchroService synchroService) {
        this.synchroService = synchroService;
        restart = false;
    }

    @PostMapping("/synchro/transferObject")
    public ResponseEntity<String> getTransferObject(@RequestBody TransferObject transferObject) {

        return synchroService.setNewClientData(transferObject) ;
    }

    @PostMapping("/synchro/documents/{id}")
    public ResponseEntity<String> getBankData(@PathVariable String id , @RequestBody List<DeclarationData> declarationData) {

        return synchroService.handleNewDeclarationData(declarationData, id);
    }

    @GetMapping("/synchro")
    @ResponseStatus(HttpStatus.OK)
    public void restart() {
        if (restart)
            ComarchBackendApplication.restart();
    }
}
