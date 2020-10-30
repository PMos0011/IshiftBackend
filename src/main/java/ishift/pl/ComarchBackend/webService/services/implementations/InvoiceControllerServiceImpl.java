package ishift.pl.ComarchBackend.webService.services.implementations;

import com.itextpdf.text.DocumentException;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.invoicePDFGenerator.InvoicePDFGenerator;
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

@Service
public class InvoiceControllerServiceImpl implements InvoiceControllerService {

    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final InvoiceTypeRepository invoiceTypeRepository;
    private final VatTypeRepository vatTypeRepository;
    private final InvoiceFromPanelService invoiceFromPanelService;

    @Autowired
    public InvoiceControllerServiceImpl(InvoiceTypeRepository invoiceTypeRepository,
                                         VatTypeRepository vatTypeRepository,
                                        InvoiceFromPanelService invoiceFromPanelService) {
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(null);
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.vatTypeRepository = vatTypeRepository;
        this.invoiceFromPanelService = invoiceFromPanelService;
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
    public ResponseEntity<Resource> invoicePreview(InvoiceDTO invoiceDTO) {

        return generateResourceResponseEntity(
                invoiceFromPanelService.generateInvoiceFromPanelFromInvoiceDTO(invoiceDTO));
    }

    @Override
    public ResponseEntity<Resource> invoiceSaveAndSend(String id, InvoiceDTO invoiceDTO) {

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        ResponseEntity<Resource> response = generateResourceResponseEntity(
                invoiceFromPanelService.saveInvoiceFromPanelFromInvoiceDTOWithRelationships(invoiceDTO));
        ClientDatabaseContextHolder.clear();
        return response;
    }

    private ResponseEntity<Resource> generateResourceResponseEntity(InvoiceFromPanel invoiceFromPanel){
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
}
