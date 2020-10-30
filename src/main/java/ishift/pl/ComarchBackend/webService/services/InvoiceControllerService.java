package ishift.pl.ComarchBackend.webService.services;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceType;
import ishift.pl.ComarchBackend.webDataModel.model.VatType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InvoiceControllerService {

    ResponseEntity<List<InvoiceType>> getAllInvoiceTypes(String id);

    ResponseEntity <String> saveInvoice(String id, InvoiceDTO invoiceDTO);

    ResponseEntity<List<VatType>> getVatTypes(String id);

    ResponseEntity<Resource> invoicePreview(InvoiceDTO invoiceDTO);

    ResponseEntity<Resource> invoiceSaveAndSend(String id, InvoiceDTO invoiceDTO);
}
