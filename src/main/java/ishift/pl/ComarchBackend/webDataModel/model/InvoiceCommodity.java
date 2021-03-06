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
    private Long correctionId;

    public InvoiceCommodity(Long id, BigDecimal amount, BigDecimal discount, String measure, String name, BigDecimal price, String vat) {
        this.id = id;
        this.amount = amount;
        this.discount = discount;
        this.measure = measure;
        this.name = name;
        this.price = price;
        this.vat = vat;

        BigDecimal numVat;
        try {
            numVat = new BigDecimal(vat);
        }
        catch (Exception e){
            numVat = new BigDecimal(0);
        }

        BigDecimal d = new BigDecimal(100).setScale(4,RoundingMode.HALF_UP);
        d=d.subtract(discount);
        d=d.divide(new BigDecimal(100),RoundingMode.HALF_UP);

        BigDecimal discountPrice = price.multiply(d).setScale(2,RoundingMode.HALF_UP);
        this.nettoAmount = discountPrice.multiply(amount).setScale(2,RoundingMode.HALF_UP);
        BigDecimal v= numVat.divide(new BigDecimal(100),4,RoundingMode.HALF_UP);
        this.vatAmount = nettoAmount.multiply(v).setScale(2,RoundingMode.HALF_UP);
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
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount.setScale(2, RoundingMode.HALF_EVEN);
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
        return price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2,RoundingMode.HALF_EVEN);;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public BigDecimal getNettoAmount() {
        return nettoAmount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setNettoAmount(BigDecimal nettoAmount) {
        this.nettoAmount = nettoAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
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

    public Long getCorrectionId() {
        return correctionId;
    }

    public void setCorrectionId(Long correctionId) {
        this.correctionId = correctionId;
    }
}
