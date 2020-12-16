package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.LastInvoicesDTO;
import ishift.pl.ComarchBackend.webDataModel.model.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface InvoiceFromPanelService {

    InvoiceFromPanel generateInvoiceFromPanelFromInvoiceDTO(InvoiceDTO invoiceDTO);

    Set<InvoiceCommodity> generateInvoiceCommodityFromInvoiceDTO(InvoiceDTO invoiceDTO);

    Set<InvoiceVatTable> generateInvoiceVatTableFromInvoiceCommodities(Set<InvoiceCommodity> invoiceCommodities);

    Set<PartyData> generateInvoicePartiesFromInvoiceDTO(InvoiceDTO invoiceDTO);

    SummaryData generateSummaryDataFromInvoiceDTOAndInvoiceCommodities(InvoiceDTO invoiceDTO,Set<InvoiceCommodity> invoiceCommodities);

    InvoiceFromPanel saveInvoiceFromPanelFromInvoiceDTOWithRelationships(InvoiceDTO invoiceDTO);

    InvoiceFromPanel saveInvoiceFromPanelWithRelationships(InvoiceFromPanel invoiceFromPanel);

    List<InvoiceFromPanel> getInvoicesFromPanelBetweenIssueDate(Date beginDate, Date endDate);

    LastInvoicesDTO getLastInvoicesFromPanel();

    InvoiceFromPanel getInvoiceFromPanelById(Long id);

    List<InvoiceFromPanel> getAllNotUsedAdvancedInvoices();

}
