package ishift.pl.ComarchBackend.webDataModel.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class InvoiceVatTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long value;

    private String vat;
    private BigDecimal vatAmount;
    private BigDecimal nettoAmount;
    private BigDecimal bruttoAmount;
    private Long invoiceFromPanelId;

    public InvoiceVatTable() {
    }

    public InvoiceVatTable(String vat, BigDecimal vatAmount, BigDecimal nettoAmount, BigDecimal bruttoAmount) {
        this.vat = vat;
        this.vatAmount = vatAmount;
        this.nettoAmount = nettoAmount;
        this.bruttoAmount = bruttoAmount;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
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
}
