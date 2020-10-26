package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceType;
import ishift.pl.ComarchBackend.webService.services.InvoiceControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvoiceController {

    private final InvoiceControllerService invoiceControllerService;

    @Autowired
    public InvoiceController(InvoiceControllerService invoiceControllerService) {
        this.invoiceControllerService = invoiceControllerService;
    }

    @GetMapping("/invoice")
    public ResponseEntity<List<InvoiceType>> getAllInvoiceType(){

        return invoiceControllerService.getAllInvoiceTypes();
    }

    @PutMapping("/invoice/{id}")
    public ResponseEntity<String> putInvoice(@PathVariable String id, @RequestBody InvoiceDTO data){

        return invoiceControllerService.saveInvoice(id, data);
    }
}
