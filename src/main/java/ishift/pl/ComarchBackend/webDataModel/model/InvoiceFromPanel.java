package ishift.pl.ComarchBackend.webDataModel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceFromPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;
    private String invoiceTypeName;
    private Date issueDate;
    private String placeOfIssue;
    private Date sellDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoiceFromPanelId", updatable = false, insertable = false)
    private Set<InvoiceCommodity> invoiceCommodities;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoiceFromPanelId", updatable = false, insertable = false)
    private Set<PartyData> partiesData;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoiceFromPanelId", updatable = false, insertable = false)
    private Set<InvoiceVatTable> invoiceVatTables;


    @OneToOne(mappedBy = "invoiceFromPanel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SummaryData summaryData;

    public InvoiceFromPanel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<InvoiceCommodity> getInvoiceCommodities() {
        return invoiceCommodities;
    }

    public void setInvoiceCommodities(Set<InvoiceCommodity> invoiceCommodities) {
        this.invoiceCommodities = invoiceCommodities;
    }

    public Set<PartyData> getPartiesData() {
        return partiesData;
    }

    public void setPartiesData(Set<PartyData> partiesData) {
        this.partiesData = partiesData;
    }

    public Set<InvoiceVatTable> getInvoiceVatTables() {
        return invoiceVatTables;
    }

    public void setInvoiceVatTables(Set<InvoiceVatTable> invoiceVatTables) {
        this.invoiceVatTables = invoiceVatTables;
    }

    public SummaryData getSummaryData() {
        return summaryData;
    }

    public void setSummaryData(SummaryData summaryData) {
        this.summaryData = summaryData;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }
}
