package ishift.pl.ComarchBackend.webDataModel.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.InvoiceDTO;
import ishift.pl.ComarchBackend.webDataModel.model.*;
import ishift.pl.ComarchBackend.webDataModel.services.InvoiceFromPanelService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceFromPanelServiceImpl implements InvoiceFromPanelService {

    @Override
    public InvoiceFromPanel generateInvoiceFromPanelFromInvoiceDTO(InvoiceDTO invoiceDTO) {

        InvoiceFromPanel invoiceFromPanel = invoiceDTO.getHeader();
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
                                commodity.getAmount(),
                                commodity.getDiscount(),
                                commodity.getMeasure(),
                                commodity.getName(),
                                commodity.getPrice(),
                                commodity.getVat())
                ).collect(Collectors.toSet());
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
    public void saveInvoiceFromPanelWithRelationships(InvoiceFromPanel invoiceFromPanel) {

    }
}
