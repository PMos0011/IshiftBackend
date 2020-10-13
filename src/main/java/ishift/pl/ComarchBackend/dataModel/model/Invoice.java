package ishift.pl.ComarchBackend.dataModel.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CDN.VatNag")
public class Invoice {

    @Id
    @Column(name = "VaN_VaNID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VaN_Typ")
    private Integer typ;

    @Column(name = "VaN_PodID")
    private Long contactorId;

    @Column(name = "VaN_RokMies")
    private Integer date;

    @Column(name = "VaN_KntNazwa1")
    private String name1;

    @Column(name = " VaN_KntNazwa2")
    private String name2;

    @Column(name = "VaN_KntNazwa3")
    private String name3;

    @Column(name = " VaN_DataZap")
    private Date paymentDate;

    @Column(name = " VaN_DataWys")
    private Date issueDate;

    @Column(name = "VaN_Dokument")
    private String docName;

    @Column(name = "VaN_RazemNetto")
    private BigDecimal netto;

    @Column(name = "VaN_RazemVAT")
    private BigDecimal vat;

    @Column(name = "VaN_RazemBrutto")
    private BigDecimal brutto;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTyp() {
        return typ;
    }

    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    public Long getContactorId() {
        return contactorId;
    }

    public void setContactorId(Long contactorId) {
        this.contactorId = contactorId;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
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
}
