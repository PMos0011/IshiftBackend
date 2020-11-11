package ishift.pl.ComarchBackend.webDataModel.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.model.*;
import ishift.pl.ComarchBackend.webDataModel.repositiories.*;
import ishift.pl.ComarchBackend.webDataModel.services.InvoiceFromPanelService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class InvoiceFromPanelServiceImpl implements InvoiceFromPanelService {

    private final InvoiceFromPanelRepository invoiceFromPanelRepository;
    private final InvoiceCommodityRepository invoiceCommodityRepository;
    private final PartyDataRepository partyDataRepository;
    private final InvoiceVatTableRepository invoiceVatTableRepository;
    private final SummaryDataRepository summaryDataRepository;

    public InvoiceFromPanelServiceImpl(InvoiceFromPanelRepository invoiceFromPanelRepository,
                                       InvoiceCommodityRepository invoiceCommodityRepository,
                                       PartyDataRepository partyDataRepository,
                                       InvoiceVatTableRepository invoiceVatTableRepository,
                                       SummaryDataRepository summaryDataRepository) {
        this.invoiceFromPanelRepository = invoiceFromPanelRepository;
        this.invoiceCommodityRepository = invoiceCommodityRepository;
        this.partyDataRepository = partyDataRepository;
        this.invoiceVatTableRepository = invoiceVatTableRepository;
        this.summaryDataRepository = summaryDataRepository;
    }

    @Override
    public InvoiceFromPanel generateInvoiceFromPanelFromInvoiceDTO(InvoiceDTO invoiceDTO) {

        InvoiceFromPanel invoiceFromPanel = invoiceDTO.getHeader();

        Optional <Long> invoiceToCorrectId = Optional.ofNullable(invoiceDTO.getHeader().getId());
        invoiceToCorrectId.ifPresent((id)->invoiceFromPanel.setInvoiceToCorrect(invoiceFromPanelRepository.findById(id).orElse(null)));

        invoiceFromPanel.setInvoiceCommodities(generateInvoiceCommodityFromInvoiceDTO(invoiceDTO));
        invoiceFromPanel.setInvoiceVatTables(generateInvoiceVatTableFromInvoiceCommodities(invoiceFromPanel.getInvoiceCommodities()));
        invoiceFromPanel.setPartiesData(generateInvoicePartiesFromInvoiceDTO(invoiceDTO));
        invoiceFromPanel.setSummaryData(generateSummaryDataFromInvoiceDTOAndInvoiceCommodities(invoiceDTO, invoiceFromPanel.getInvoiceCommodities()));
        return invoiceFromPanel;
    }

    @Override
    public Set<InvoiceCommodity> generateInvoiceCommodityFromInvoiceDTO(InvoiceDTO invoiceDTO) {

        return invoiceDTO.getCommodities().stream()
                .map(commodity ->
                        new InvoiceCommodity(
                                commodity.getId(),
                                commodity.getAmount(),
                                commodity.getDiscount(),
                                commodity.getMeasure(),
                                commodity.getName(),
                                commodity.getPrice(),
                                commodity.getVat())
                ).sorted(Comparator.comparing(InvoiceCommodity::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }

    @Override
    public Set<InvoiceVatTable> generateInvoiceVatTableFromInvoiceCommodities(Set<InvoiceCommodity> invoiceCommodities) {
        Map<String, InvoiceVatTable> invoiceVatTableMap = new TreeMap<>();

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


        return new HashSet<>(invoiceVatTableMap.values());
    }

    @Override
    public Set<PartyData> generateInvoicePartiesFromInvoiceDTO(InvoiceDTO invoiceDTO) {
        //todo enums
        PartyData buyer = invoiceDTO.getBuyer();
        buyer.setPartyId(1);
        PartyData seller = invoiceDTO.getSeller();
        seller.setPartyId(0);

        return new HashSet<>(Arrays.asList(
                buyer,
                seller)
        );
    }

    @Override
    public SummaryData generateSummaryDataFromInvoiceDTOAndInvoiceCommodities(InvoiceDTO invoiceDTO, Set<InvoiceCommodity> invoiceCommodities) {

        invoiceCommodities.forEach(commodity -> {
            invoiceDTO.getSummary().setVatAmount(invoiceDTO.getSummary().getVatAmount().add(commodity.getVatAmount()));
            invoiceDTO.getSummary().setBruttoAmount(invoiceDTO.getSummary().getBruttoAmount().add(commodity.getBruttoAmount()));
            invoiceDTO.getSummary().setNettoAmount(invoiceDTO.getSummary().getNettoAmount().add(commodity.getNettoAmount()));
        });
        return invoiceDTO.getSummary();
    }

    @Override
    public InvoiceFromPanel saveInvoiceFromPanelFromInvoiceDTOWithRelationships(InvoiceDTO invoiceDTO) {

        return saveInvoiceFromPanelWithRelationships(
                generateInvoiceFromPanelFromInvoiceDTO(invoiceDTO)
        );
    }

    @Override
    public InvoiceFromPanel saveInvoiceFromPanelWithRelationships(InvoiceFromPanel invoiceFromPanel) {

        Optional<Long> correctedInvoiceID = Optional.ofNullable(invoiceFromPanel.getId());
        AtomicReference<Long> invoiceId = new AtomicReference<>();


        correctedInvoiceID.ifPresentOrElse((correctedId) -> {
            Optional<InvoiceFromPanel> correctedInvoice = invoiceFromPanelRepository.findById(correctedId);
            invoiceFromPanel.setId(null);
            invoiceId.set(invoiceFromPanelRepository.save(invoiceFromPanel).getId());

            //todo orElse
            correctedInvoice.ifPresent(invoice -> {
                invoice.setCorrectionId(invoiceId.get());
                invoiceFromPanelRepository.save(invoice);
            });

        }, () -> invoiceId.set(invoiceFromPanelRepository.save(invoiceFromPanel).getId()));


        invoiceFromPanel.getInvoiceCommodities().forEach(commodity -> {
                    Optional<Long> correctedCommodityId = Optional.ofNullable(commodity.getId());
                    AtomicReference<Long> commodityId = new AtomicReference<>();
                    commodity.setInvoiceFromPanelId(invoiceId.get());

                    correctedCommodityId.ifPresent(correctedId -> {
                        Optional<InvoiceCommodity> commodityToCorrect = invoiceCommodityRepository.findById(correctedId);
                        commodity.setId(null);
                        commodityId.set(invoiceCommodityRepository.save(commodity).getId());

                        //todo OrElse
                        commodityToCorrect.ifPresent(correctedCommodity -> {
                            correctedCommodity.setCorrectionId(commodityId.get());
                            invoiceCommodityRepository.save(correctedCommodity);
                        });
                    });
                }
        );


        invoiceCommodityRepository.saveAll(invoiceFromPanel.getInvoiceCommodities());

        invoiceFromPanel.getInvoiceVatTables().forEach(vat -> vat.setInvoiceFromPanelId(invoiceId.get()));
        invoiceVatTableRepository.saveAll(invoiceFromPanel.getInvoiceVatTables());

        invoiceFromPanel.getPartiesData().forEach(data -> data.setInvoiceFromPanelId(invoiceId.get()));
        partyDataRepository.saveAll(invoiceFromPanel.getPartiesData());

        invoiceFromPanel.getSummaryData().setInvoiceFromPanelId(invoiceId.get());
        summaryDataRepository.save(invoiceFromPanel.getSummaryData());
        return invoiceFromPanel;
    }

    @Override
    public List<InvoiceFromPanel> getInvoicesFromPanelBetweenIssueDate(Date beginDate, Date endDate) {
        return invoiceFromPanelRepository.findAllByIssueDateBetween(beginDate, endDate);
    }

    @Override
    public InvoiceFromPanel getLastInvoiceFromPanel() {
        return invoiceFromPanelRepository.findFirstByOrderByIdDesc();
    }

    @Override
    public InvoiceFromPanel getInvoiceFromPanelById(Long id) {
        return invoiceFromPanelRepository.findById(id).get();
    }

}
