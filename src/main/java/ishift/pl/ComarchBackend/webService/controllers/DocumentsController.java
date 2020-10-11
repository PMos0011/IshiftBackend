package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.webService.services.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DocumentsController {

    private final DocumentsService documentsService;

    @Autowired
    public DocumentsController(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<List<DeclarationData>> getCompanyDocuments(@PathVariable String id) {

        return new ResponseEntity<List<DeclarationData>>(documentsService.getDeclarations(id), HttpStatus.OK);
    }

    @GetMapping("/documents/{dbId}/{id}")
    public ResponseEntity<DeclarationData> getCompanyDocuments(@PathVariable String dbId, @PathVariable Long id) {

        return new ResponseEntity<DeclarationData>(documentsService.getDeclarationById(dbId, id), HttpStatus.OK);
    }
}
