package ishift.pl.ComarchBackend.webDataModel.DTOModel;

import ishift.pl.ComarchBackend.webDataModel.model.InvoiceExchangeRate;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;
import ishift.pl.ComarchBackend.webDataModel.model.PartyData;
import ishift.pl.ComarchBackend.webDataModel.model.SummaryData;

import java.util.List;

public class InvoiceDTO {
    private List<CommodityDTO> commodities;
    private InvoiceFromPanel header;
    private PartyData seller;
    private PartyData buyer;
    private SummaryData summary;
    private List<Long> usedAdvInvoices;
    private InvoiceExchangeRate invoiceExchangeRate;

    public List<CommodityDTO> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<CommodityDTO> commodities) {
        this.commodities = commodities;
    }

    public InvoiceFromPanel getHeader() {
        return header;
    }

    public void setHeader(InvoiceFromPanel invoiceFromPanel) {
        this.header = invoiceFromPanel;
    }

    public PartyData getSeller() {
        return seller;
    }

    public void setSeller(PartyData seller) {
        this.seller = seller;
    }

    public PartyData getBuyer() {
        return buyer;
    }

    public void setBuyer(PartyData buyer) {
        this.buyer = buyer;
    }

    public SummaryData getSummary() {
        return summary;
    }

    public void setSummary(SummaryData summary) {
        this.summary = summary;
    }

    public List<Long> getUsedAdvInvoices() {
        return usedAdvInvoices;
    }

    public void setUsedAdvInvoices(List<Long> usedAdvInvoices) {
        this.usedAdvInvoices = usedAdvInvoices;
    }

    public InvoiceExchangeRate getInvoiceExchangeRate() {
        return invoiceExchangeRate;
    }

    public void setInvoiceExchangeRate(InvoiceExchangeRate invoiceExchangeRate) {
        this.invoiceExchangeRate = invoiceExchangeRate;
    }
}
