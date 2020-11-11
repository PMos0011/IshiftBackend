package ishift.pl.ComarchBackend.webService.services.implementations;

import com.itextpdf.text.DocumentException;
import ishift.pl.ComarchBackend.dataModel.model.Invoice;
import ishift.pl.ComarchBackend.dataModel.repository.InvoiceRepository;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.invoicePDFGenerator.InvoicePDFGenerator;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.DatesBetween;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.model.*;
import ishift.pl.ComarchBackend.webDataModel.repositiories.*;
import ishift.pl.ComarchBackend.webDataModel.services.InvoiceFromPanelService;
import ishift.pl.ComarchBackend.webService.services.InvoiceControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceControllerServiceImpl implements InvoiceControllerService {

    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final InvoiceTypeRepository invoiceTypeRepository;
    private final VatTypeRepository vatTypeRepository;
    private final InvoiceFromPanelService invoiceFromPanelService;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceControllerServiceImpl(InvoiceTypeRepository invoiceTypeRepository,
                                        VatTypeRepository vatTypeRepository,
                                        InvoiceFromPanelService invoiceFromPanelService,
                                        InvoiceRepository invoiceRepository) {
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(null);
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.vatTypeRepository = vatTypeRepository;
        this.invoiceFromPanelService = invoiceFromPanelService;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public ResponseEntity<List<InvoiceType>> getAllInvoiceTypes(String id) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        List<InvoiceType> invoiceTypeList = invoiceTypeRepository.findAll();
        ClientDatabaseContextHolder.clear();

        return new ResponseEntity<>(invoiceTypeList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> saveInvoice(String id, InvoiceDTO invoiceDTO) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        invoiceFromPanelService.saveInvoiceFromPanelFromInvoiceDTOWithRelationships(invoiceDTO);
        ClientDatabaseContextHolder.clear();

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<VatType>> getVatTypes(String id) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        List<VatType> vatTypes = vatTypeRepository.findAll();
        ClientDatabaseContextHolder.clear();

        return new ResponseEntity<>(vatTypes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> invoicePreview(String id, InvoiceDTO invoiceDTO) {
        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        InvoiceFromPanel invoiceFromPanel = invoiceFromPanelService.generateInvoiceFromPanelFromInvoiceDTO(invoiceDTO);
        ClientDatabaseContextHolder.clear();
        return generateResourceResponseEntity(invoiceFromPanel);
    }

    @Override
    public ResponseEntity<Resource> invoiceSaveAndSend(String id, InvoiceDTO invoiceDTO) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        ResponseEntity<Resource> response = generateResourceResponseEntity(
                invoiceFromPanelService.saveInvoiceFromPanelFromInvoiceDTOWithRelationships(invoiceDTO));
        ClientDatabaseContextHolder.clear();
        return response;
    }

    @Override
    public ResponseEntity<List<Invoice>> getAllImportedInvoices(String id, DatesBetween dates) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        List<Invoice> invoices = invoiceRepository.findAllByIssueDateBetween(dates.getBeginDate(), dates.getEndDate());
        ClientDatabaseContextHolder.clear();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<InvoiceFromPanel>> getInvoicesFromPanelBetweenIssueDates(String id, DatesBetween dates) {
        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        List<InvoiceFromPanel> invoices = invoiceFromPanelService.getInvoicesFromPanelBetweenIssueDate(dates.getBeginDate(), dates.getEndDate());
        ClientDatabaseContextHolder.clear();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvoiceFromPanel> getLastInvoiceFromPanel(String id) {
        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        InvoiceFromPanel invoice = invoiceFromPanelService.getLastInvoiceFromPanel();
        ClientDatabaseContextHolder.clear();
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Resource> getInvoiceFromPanelByIdAndSendPDF(String dbId, Long id) {
        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(dbId));
        InvoiceFromPanel invoice = invoiceFromPanelService.getInvoiceFromPanelById(id);
        ClientDatabaseContextHolder.clear();
        return generateResourceResponseEntity(invoice);
    }

    private ResponseEntity<Resource> generateResourceResponseEntity(InvoiceFromPanel invoiceFromPanel) {
        invoiceFromPanel.setInvoiceCommodities(
                sortAlphabeticInvoiceCommoditySet(invoiceFromPanel.getInvoiceCommodities()));
        invoiceFromPanel.setInvoiceVatTables(sortAlphabeticInvoiceVatTableSet(
                invoiceFromPanel.getInvoiceVatTables()));

        InvoicePDFGenerator invoicePDFGenerator = new InvoicePDFGenerator(invoiceFromPanel);
        try {
            byte[] bytes = invoicePDFGenerator.createInvoice();
            Resource r = new ByteArrayResource(bytes);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        //todo error handling
        return null;
    }

    private Set<InvoiceCommodity> sortAlphabeticInvoiceCommoditySet(Set<InvoiceCommodity> invoiceCommodities){
        return invoiceCommodities.stream()
                .sorted(Comparator.comparing(InvoiceCommodity::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<InvoiceVatTable> sortAlphabeticInvoiceVatTableSet(Set<InvoiceVatTable> invoiceVatTables){
        return invoiceVatTables.stream()
                .sorted(Comparator.comparing(InvoiceVatTable::getVat))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
