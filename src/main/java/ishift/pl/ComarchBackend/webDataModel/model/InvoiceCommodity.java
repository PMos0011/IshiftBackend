package ishift.pl.ComarchBackend.webDataModel.model;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.CommodityDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Entity
public class InvoiceCommodity extends CommodityDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private BigDecimal discount;
    private String measure;
    private String name;
    private BigDecimal price;
    private BigDecimal vat;
    private BigDecimal nettoAmount;
    private BigDecimal vatAmount;
    private BigDecimal bruttoAmount;
    private Long invoiceFromPanelId;

    public InvoiceCommodity(BigDecimal amount, BigDecimal discount, String measure, String name, BigDecimal price, BigDecimal vat, Long webInvoiceFromPanelId) {
        this.amount = amount;
        this.discount = discount;
        this.measure = measure;
        this.name = name;
        this.price = price;
        this.vat = vat;
        this.invoiceFromPanelId = webInvoiceFromPanelId;

        MathContext round = new MathContext(2);

        this.nettoAmount = price.multiply(amount);
        BigDecimal d = discount.divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
        d = new BigDecimal(1).subtract(d);
        this.nettoAmount = nettoAmount.multiply(d);
       // this.nettoAmount = this.nettoAmount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal v= vat.divide(new BigDecimal(100),4,RoundingMode.HALF_UP);
        this.vatAmount = nettoAmount.multiply(v);
        this.bruttoAmount = this.nettoAmount.add(this.vatAmount);
    }

    public InvoiceCommodity() {
    }

    public Long getInvoiceFromPanelId() {
        return invoiceFromPanelId;
    }

    public void setInvoiceFromPanelId(Long invoiceFromPanelId) {
        this.invoiceFromPanelId = invoiceFromPanelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public BigDecimal getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public String getMeasure() {
        return measure;
    }

    @Override
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public BigDecimal getVat() {
        return vat;
    }

    @Override
    public void setVat(BigDecimal vat) {
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
}
