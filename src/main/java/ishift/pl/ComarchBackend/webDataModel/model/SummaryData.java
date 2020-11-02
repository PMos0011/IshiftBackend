package ishift.pl.ComarchBackend.webDataModel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class SummaryData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankAcc;
    @Lob
    private String comments;
    private BigDecimal paid;
    private Date paidDay;
    private Date paymentDay;
    private String paymentOptionIdValue;
    private String statusIdValue;
    private Long invoiceFromPanelId;
    private BigDecimal vatAmount;
    private BigDecimal nettoAmount;
    private BigDecimal bruttoAmount;
    private String vatExemptionLabelNp;
    @Lob
    private String vatExemptionValueNp;
    private String vatExemptionLabelZw;
    @Lob
    private String vatExemptionValueZw;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "invoiceFromPanelId", updatable = false, insertable = false)
    private InvoiceFromPanel invoiceFromPanel;

    public SummaryData() {
        this.vatAmount = new BigDecimal(0);
        this.nettoAmount = new BigDecimal(0);
        this.bruttoAmount = new BigDecimal(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BigDecimal getPaid() {
        if (paid != null)
            return paid.setScale(2, RoundingMode.HALF_EVEN);

        return null;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public Date getPaidDay() {
        return paidDay;
    }

    public void setPaidDay(Date paidDay) {
        this.paidDay = paidDay;
    }

    public Date getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(Date paymentDay) {
        this.paymentDay = paymentDay;
    }

    public String getPaymentOptionIdValue() {
        return paymentOptionIdValue;
    }

    public void setPaymentOptionIdValue(String paymentOptionIdValue) {
        this.paymentOptionIdValue = paymentOptionIdValue;
    }

    public String getStatusIdValue() {
        return statusIdValue;
    }

    public void setStatusIdValue(String statusIdValue) {
        this.statusIdValue = statusIdValue;
    }

    public BigDecimal getVatAmount() {
        return vatAmount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getNettoAmount() {
        return nettoAmount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setNettoAmount(BigDecimal nettoAmount) {
        this.nettoAmount = nettoAmount;
    }

    public BigDecimal getBruttoAmount() {
        return bruttoAmount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setBruttoAmount(BigDecimal bruttoAmount) {
        this.bruttoAmount = bruttoAmount;
    }

    public Long getInvoiceFromPanelId() {
        return invoiceFromPanelId;
    }

    public void setInvoiceFromPanelId(Long invoiceFromPanelId) {
        this.invoiceFromPanelId = invoiceFromPanelId;
    }

    public InvoiceFromPanel getInvoiceFromPanel() {
        return invoiceFromPanel;
    }

    public void setInvoiceFromPanel(InvoiceFromPanel invoiceFromPanel) {
        this.invoiceFromPanel = invoiceFromPanel;
    }

    public String getVatExemptionLabelNp() {
        return vatExemptionLabelNp;
    }

    public void setVatExemptionLabelNp(String vatExemptionLabelNp) {
        this.vatExemptionLabelNp = vatExemptionLabelNp;
    }

    public String getVatExemptionValueNp() {
        return vatExemptionValueNp;
    }

    public void setVatExemptionValueNp(String vatExemptionValueNp) {
        this.vatExemptionValueNp = vatExemptionValueNp;
    }

    public String getVatExemptionLabelZw() {
        return vatExemptionLabelZw;
    }

    public void setVatExemptionLabelZw(String vatExemptionLabelZw) {
        this.vatExemptionLabelZw = vatExemptionLabelZw;
    }

    public String getVatExemptionValueZw() {
        return vatExemptionValueZw;
    }

    public void setVatExemptionValueZw(String vatExemptionValueZw) {
        this.vatExemptionValueZw = vatExemptionValueZw;
    }
}
