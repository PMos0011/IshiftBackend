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
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class InvoiceControllerServiceImpl implements InvoiceControllerService {

    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final InvoiceTypeRepository invoiceTypeRepository;
    private final WebInvoiceRepository webInvoiceRepository;
    private final InvoiceCommodityRepository invoiceCommodityRepository;
    private final PartyDataRepository partyDataRepository;
    private final SummaryDataRepository summaryDataRepository;
    private final InvoiceFromPanelRepository invoiceFromPanelRepository;
    private final InvoiceVatTableRepository invoiceVatTableRepository;
    private final VatTypeRepository vatTypeRepository;
    private final InvoiceFromPanelService invoiceFromPanelService;

    @Autowired
    public InvoiceControllerServiceImpl(InvoiceTypeRepository invoiceTypeRepository,
                                        WebInvoiceRepository webInvoiceRepository,
                                        InvoiceCommodityRepository invoiceCommodityRepository,
                                        PartyDataRepository partyDataRepository,
                                        SummaryDataRepository summaryDataRepository,
                                        InvoiceFromPanelRepository invoiceFromPanelRepository,
                                        InvoiceVatTableRepository invoiceVatTableRepository,
                                        VatTypeRepository vatTypeRepository,
                                        InvoiceFromPanelService invoiceFromPanelService) {
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(null);
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.webInvoiceRepository = webInvoiceRepository;
        this.partyDataRepository = partyDataRepository;
        this.invoiceCommodityRepository = invoiceCommodityRepository;
        this.summaryDataRepository = summaryDataRepository;
        this.invoiceFromPanelRepository = invoiceFromPanelRepository;
        this.invoiceVatTableRepository = invoiceVatTableRepository;
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

        Map<String, InvoiceVatTable> invoiceVatTableMap = new TreeMap<>();
        List<InvoiceCommodity> invoiceCommodities = new ArrayList<>();

        final InvoiceFromPanel savedInvoice = invoiceFromPanelRepository.save(invoiceDTO.getHeader());

        invoiceDTO.getCommodities().forEach(commodity -> invoiceCommodities.add(new InvoiceCommodity(
                commodity.getAmount(),
                commodity.getDiscount(),
                commodity.getMeasure(),
                commodity.getName(),
                commodity.getPrice(),
                commodity.getVat()
        )));
        invoiceCommodityRepository.saveAll(invoiceCommodities);

        invoiceCommodities.forEach(commodity -> {
            Optional<InvoiceVatTable> optionalInvoiceVatTable =
                    Optional.ofNullable(invoiceVatTableMap.get(commodity.getVat()));

            optionalInvoiceVatTable.ifPresentOrElse(vatTable -> {
                invoiceVatTableMap.get(commodity.getVat()).setBruttoAmount(
                        invoiceVatTableMap.get(commodity.getVat()).getBruttoAmount().add(commodity.getBruttoAmount()));
                invoiceVatTableMap.get(commodity.getVat()).setNettoAmount(
                        invoiceVatTableMap.get(commodity.getVat()).getNettoAmount().add(commodity.getNettoAmount()));
                invoiceVatTableMap.get(commodity.getVat()).setVatAmount(
                        invoiceVatTableMap.get(commodity.getVat()).getVatAmount().add(commodity.getVatAmount()));

            }, () -> invoiceVatTableMap.put(commodity.getVat(), new InvoiceVatTable(
                    commodity.getVat(),
                    commodity.getVatAmount(),
                    commodity.getNettoAmount(),
                    commodity.getBruttoAmount()
            )));
        });

        invoiceVatTableRepository.saveAll(invoiceVatTableMap.values());

        invoiceDTO.getBuyer().setInvoiceFromPanelId(savedInvoice.getId());
        //todo enums
        invoiceDTO.getBuyer().setPartyId(1);
        partyDataRepository.save(invoiceDTO.getBuyer());
        invoiceDTO.getSeller().setInvoiceFromPanelId(savedInvoice.getId());
        invoiceDTO.getSeller().setPartyId(0);
        partyDataRepository.save(invoiceDTO.getSeller());


        invoiceCommodities.forEach(commodity -> {
            invoiceDTO.getSummary().setVatAmount(invoiceDTO.getSummary().getVatAmount().add(commodity.getVatAmount()));
            invoiceDTO.getSummary().setBruttoAmount(invoiceDTO.getSummary().getBruttoAmount().add(commodity.getBruttoAmount()));
            invoiceDTO.getSummary().setNettoAmount(invoiceDTO.getSummary().getNettoAmount().add(commodity.getNettoAmount()));
        });

        invoiceDTO.getSummary().setInvoiceFromPanelId(savedInvoice.getId());
        summaryDataRepository.save(invoiceDTO.getSummary());


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
    public ResponseEntity<Resource> test(InvoiceDTO invoiceDTO) {

//        ClientDatabaseContextHolder.set("cdn_adam_ma_lej_shdnffbcg");
//        Optional<InvoiceFromPanel> invoiceFromPanel = invoiceFromPanelRepository.findById(1L);
//        ClientDatabaseContextHolder.clear();

        InvoiceFromPanel invoiceFromPanel = invoiceFromPanelService.generateInvoiceFromPanelFromInvoiceDTO(invoiceDTO);

        InvoicePDFGenerator invoicePDFGenerator = new InvoicePDFGenerator(invoiceFromPanel);
        try {
            byte[] bytes = invoicePDFGenerator.createInvoice();
            Resource r = new ByteArrayResource(bytes);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return null;
    }
}
