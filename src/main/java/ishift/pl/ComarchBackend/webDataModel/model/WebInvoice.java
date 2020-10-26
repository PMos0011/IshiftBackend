package ishift.pl.ComarchBackend.webDataModel.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class WebInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contactorId;

    private String contactorName;

    private Date paymentDate;

    private Date issueDate;

    private String docName;

    private BigDecimal netto;

    private BigDecimal vat;

    private BigDecimal brutto;

    private BigDecimal paymentStatus;

    private Long docType;

    private String currency;

    public WebInvoice() {
    }

    public WebInvoice(Long contactorId,
                      String contactorName,
                      Date paymentDate,
                      Date issueDate,
                      String docName,
                      BigDecimal netto,
                      BigDecimal vat,
                      BigDecimal brutto,
                      BigDecimal paymentStatus,
                      Long docType,
                      String currency) {
        this.contactorId = contactorId;
        this.contactorName = contactorName;
        this.paymentDate = paymentDate;
        this.issueDate = issueDate;
        this.docName = docName;
        this.netto = netto;
        this.vat = vat;
        this.brutto = brutto;
        this.paymentStatus = paymentStatus;
        this.docType = docType;
        this.currency=currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactorId() {
        return contactorId;
    }

    public void setContactorId(Long contactorId) {
        this.contactorId = contactorId;
    }

    public String getContactorName() {
        return contactorName;
    }

    public void setContactorName(String contactorName) {
        this.contactorName = contactorName;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public BigDecimal getNetto() {
        return netto;
    }

    public void setNetto(BigDecimal netto) {
        this.netto = netto;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getBrutto() {
        return brutto;
    }

    public void setBrutto(BigDecimal brutto) {
        this.brutto = brutto;
    }

    public Long getDocType() {
        return docType;
    }

    public void setDocType(Long docType) {
        this.docType = docType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(BigDecimal paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
