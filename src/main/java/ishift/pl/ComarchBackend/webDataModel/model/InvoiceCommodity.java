package ishift.pl.ComarchBackend.webDataModel.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class InvoiceCommodity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private BigDecimal discount;
    private String measure;
    private String name;
    private BigDecimal price;
    private String vat;
    private BigDecimal nettoAmount;
    private BigDecimal vatAmount;
    private BigDecimal bruttoAmount;
    private Long invoiceFromPanelId;

    public InvoiceCommodity(BigDecimal amount, BigDecimal discount, String measure, String name, BigDecimal price, String vat, Long webInvoiceFromPanelId) {
        this.amount = amount;
        this.discount = discount;
        this.measure = measure;
        this.name = name;
        this.price = price;
        this.vat = vat;
        this.invoiceFromPanelId = webInvoiceFromPanelId;

        BigDecimal numVat;
        try {
            numVat = new BigDecimal(vat);
        }
        catch (Exception e){
            numVat = new BigDecimal(0);
        }

        this.nettoAmount = price.multiply(amount);
        BigDecimal d = discount.divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        d = new BigDecimal(1).subtract(d);
        this.nettoAmount = nettoAmount.multiply(d);
        BigDecimal v= numVat.divide(new BigDecimal(100),4,RoundingMode.HALF_UP);
        this.vatAmount = nettoAmount.multiply(v);
        this.bruttoAmount = this.nettoAmount.add(this.vatAmount);
    }

    public InvoiceCommodity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public BigDecimal getNettoAmount() {
        return nettoAmount;
    }

    public void setNettoAmount(BigDecimal nettoAmount) {
        this.nettoAmount = nettoAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
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
