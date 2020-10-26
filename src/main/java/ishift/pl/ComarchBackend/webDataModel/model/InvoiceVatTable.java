package ishift.pl.ComarchBackend.webDataModel.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class InvoiceVatTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long value;

    private BigDecimal vat;
    private BigDecimal vatAmount;
    private BigDecimal nettoAmount;
    private BigDecimal bruttoAmount;
    private Long invoiceFromPanelId;

    public InvoiceVatTable() {
    }

    public InvoiceVatTable(BigDecimal vat, BigDecimal vatAmount, BigDecimal nettoAmount, BigDecimal bruttoAmount, Long invoiceFromPanelId) {
        this.vat = vat;
        this.vatAmount = vatAmount;
        this.nettoAmount = nettoAmount;
        this.bruttoAmount = bruttoAmount;
        this.invoiceFromPanelId = invoiceFromPanelId;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getNettoAmount() {
        return nettoAmount;
    }

    public void setNettoAmount(BigDecimal nettoAmount) {
        this.nettoAmount = nettoAmount;
    }

    public BigDecimal getBruttoAmount() {
        return bruttoAmount;
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
