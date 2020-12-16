package ishift.pl.ComarchBackend.webService.services;

import ishift.pl.ComarchBackend.dataModel.model.Invoice;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.DatesBetween;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.LastInvoicesDTO;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceType;
import ishift.pl.ComarchBackend.webDataModel.model.VatType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InvoiceControllerService {

    ResponseEntity<List<InvoiceType>> getAllInvoiceTypes(String id);

    ResponseEntity <String> saveInvoice(String id, InvoiceDTO invoiceDTO);

    ResponseEntity<List<VatType>> getVatTypes(String id);

    ResponseEntity<Resource> invoicePreview(String id, InvoiceDTO invoiceDTO);

    ResponseEntity<Resource> invoiceSaveAndSend(String id, InvoiceDTO invoiceDTO);

    ResponseEntity<List<Invoice>> getAllImportedInvoices (String id, DatesBetween dates);

    ResponseEntity<List<InvoiceFromPanel>> getInvoicesFromPanelBetweenIssueDates(String id, DatesBetween dates);

    ResponseEntity<LastInvoicesDTO> getLastInvoiceFromPanel(String id);

    ResponseEntity<Resource> getInvoiceFromPanelByIdAndSendPDF(String dbId, Long id);

    ResponseEntity<List<InvoiceFromPanel>> getAllNotUsedAdvancedInvoices(String id);
}
