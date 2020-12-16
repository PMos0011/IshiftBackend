package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.dataModel.model.Invoice;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.DatesBetween;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.LastInvoicesDTO;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;
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

    @GetMapping("/invoice/types/{id}")
    public ResponseEntity<List<InvoiceType>> getAllInvoiceType(@PathVariable String id) {

        return invoiceControllerService.getAllInvoiceTypes(id);
    }

    @GetMapping("/invoice/{id}")
    ResponseEntity<LastInvoicesDTO> getLastInvoiceFromPanel(@PathVariable String id) {
        return invoiceControllerService.getLastInvoiceFromPanel(id);
    }

    @PutMapping("/invoice/{id}")
    ResponseEntity<List<InvoiceFromPanel>> getInvoicesFromPanel(@PathVariable String id, @RequestBody DatesBetween dates) {
        return invoiceControllerService.getInvoicesFromPanelBetweenIssueDates(id, dates);
    }

    @GetMapping("/invoice/vat/{id}")
    public ResponseEntity<List<VatType>> getVatTypes(@PathVariable String id) {

        return invoiceControllerService.getVatTypes(id);
    }

    @PutMapping("/invoice/save/{id}")
    public ResponseEntity<String> putInvoice(@PathVariable String id, @RequestBody InvoiceDTO data) {

        return invoiceControllerService.saveInvoice(id, data);
    }

    @PostMapping("/invoice/preview/{id}")
    public ResponseEntity<Resource> invoicePreview(@PathVariable String id, @RequestBody InvoiceDTO data) {

        return invoiceControllerService.invoicePreview(id, data);
    }

    @GetMapping("/invoice/preview/{dbId}/{id}")
    public ResponseEntity<Resource> getInvoicePDF(@PathVariable String dbId, @PathVariable Long id) {

        return invoiceControllerService.getInvoiceFromPanelByIdAndSendPDF(dbId, id);
    }


    @PutMapping("/invoice/save/preview/{id}")
    public ResponseEntity<Resource> saveAndSendInvoice(@PathVariable String id, @RequestBody InvoiceDTO data) {

        return invoiceControllerService.invoiceSaveAndSend(id, data);
    }

    @PutMapping("/invoice/imported/{id}")
    public ResponseEntity<List<Invoice>> getImportedInvoices(@PathVariable String id, @RequestBody DatesBetween dates) {
        System.out.println(dates.getBeginDate());

        return invoiceControllerService.getAllImportedInvoices(id, dates);
    }

    @GetMapping("/advancedInvoice/{id}")
    ResponseEntity<List<InvoiceFromPanel>> getAllNotUsedAdvancedInvoices(@PathVariable String id) {
        return invoiceControllerService.getAllNotUsedAdvancedInvoices(id);
    }


}
