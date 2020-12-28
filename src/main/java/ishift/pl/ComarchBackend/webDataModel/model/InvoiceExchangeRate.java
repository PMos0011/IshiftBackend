package ishift.pl.ComarchBackend.webDataModel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date exchangeDate;
    private Double exchangeRate;
    private String exchangeBasis;
    private Long invoiceFromPanelId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "invoiceFromPanelId", updatable = false, insertable = false)
    private InvoiceFromPanel invoiceFromPanel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getExchangeBasis() {
        return exchangeBasis;
    }

    public void setExchangeBasis(String exchangeBasis) {
        this.exchangeBasis = exchangeBasis;
    }

    public Long getInvoiceFromPanelId() {
        return invoiceFromPanelId;
    }

    public void setInvoiceFromPanelId(Long invoiceFromPanelId) {
        this.invoiceFromPanelId = invoiceFromPanelId;
    }
}
