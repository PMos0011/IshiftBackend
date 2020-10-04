package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.webService.services.ClientDocumentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("https://panel.ishift.pl")
public class ClientDocumentsController {

    private final ClientDocumentsService clientDocumentsService;

    public ClientDocumentsController(ClientDocumentsService clientDocumentsService) {
        this.clientDocumentsService = clientDocumentsService;
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<List<DeclarationData>> getCompanyDocuments(@PathVariable String id) {
        System.out.println(id);
        return new ResponseEntity<List<DeclarationData>>(clientDocumentsService.getDeclarations(id), HttpStatus.OK);
    }
}
