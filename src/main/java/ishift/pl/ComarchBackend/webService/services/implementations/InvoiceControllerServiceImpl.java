package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.model.*;
import ishift.pl.ComarchBackend.webDataModel.repositiories.*;
import ishift.pl.ComarchBackend.webService.services.InvoiceControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    @Autowired
    public InvoiceControllerServiceImpl(InvoiceTypeRepository invoiceTypeRepository,
                                        WebInvoiceRepository webInvoiceRepository,
                                        InvoiceCommodityRepository invoiceCommodityRepository,
                                        PartyDataRepository partyDataRepository,
                                        SummaryDataRepository summaryDataRepository,
                                        InvoiceFromPanelRepository invoiceFromPanelRepository,
                                        InvoiceVatTableRepository invoiceVatTableRepository,
                                        VatTypeRepository vatTypeRepository) {
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(null);
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.webInvoiceRepository = webInvoiceRepository;
        this.partyDataRepository = partyDataRepository;
        this.invoiceCommodityRepository = invoiceCommodityRepository;
        this.summaryDataRepository = summaryDataRepository;
        this.invoiceFromPanelRepository = invoiceFromPanelRepository;
        this.invoiceVatTableRepository = invoiceVatTableRepository;
        this.vatTypeRepository=vatTypeRepository;
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
                commodity.getVat(),
                savedInvoice.getId()
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
                    commodity.getBruttoAmount(),
                    savedInvoice.getId()
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
}
