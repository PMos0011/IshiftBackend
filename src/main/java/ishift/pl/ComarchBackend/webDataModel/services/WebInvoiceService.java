package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.dataModel.model.Invoice;
import ishift.pl.ComarchBackend.webDataModel.model.WebInvoice;

import java.util.List;

public interface WebInvoiceService {

    List<WebInvoice> convertInvoiceListToWebInvoiceListAndSave(List<Invoice> invoiceList);
}
