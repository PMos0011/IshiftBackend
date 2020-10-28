package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceType;
import ishift.pl.ComarchBackend.webDataModel.model.VatType;
import ishift.pl.ComarchBackend.webService.services.InvoiceControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

    @GetMapping("/invoice/{id}")
    public ResponseEntity<List<InvoiceType>> getAllInvoiceType(@PathVariable String id){

        return invoiceControllerService.getAllInvoiceTypes(id);
    }

    @GetMapping("/invoice/vat/{id}")
    public ResponseEntity<List<VatType>> getVatTypes(@PathVariable String id){

        return invoiceControllerService.getVatTypes(id);
    }

    @PutMapping("/invoice/{id}")
    public ResponseEntity<String> putInvoice(@PathVariable String id, @RequestBody InvoiceDTO data){

        return invoiceControllerService.saveInvoice(id, data);
    }

    @PutMapping("/test/")
    public ResponseEntity<Resource> test(@RequestBody InvoiceDTO data){

        return invoiceControllerService.test(data);
    }
}
