package ishift.pl.ComarchBackend.webService.services;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InvoiceControllerService {

    ResponseEntity<List<InvoiceType>> getAllInvoiceTypes();

    ResponseEntity <String> saveInvoice(String id, InvoiceDTO invoiceDTO);
}
