package ishift.pl.ComarchBackend.dataModel.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "CDN.DeklNag")
public class DeclarationData {
    @Id
    @Column(name = "DkN_DkNID")
    private Long id;

    @Column(name = "DkN_TypDeklar")
    private Integer typDeklaracji;

    @Column(name = "DkN_Finalna")
    private Integer finalna;

    @Column(name = "DkN_RokMiesiac")
    private Integer rokMiesiac;

    @Column(name = "DkN_Kwota")
    private BigDecimal kwota;

    @Column(name = "DkN_PraID")
    private Integer praID;

    @Column(name = "DkN_TS_Mod")
    private Date modDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "DkW_DkNID", updatable = false, insertable = false)
    List<DeclarationDetails> declarationDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTypDeklaracji() {
        return typDeklaracji;
    }

    public void setTypDeklaracji(Integer typDeklaracji) {
        this.typDeklaracji = typDeklaracji;
    }

    public Integer getFinalna() {
        return finalna;
    }

    public void setFinalna(Integer finalna) {
        this.finalna = finalna;
    }

    public Integer getRokMiesiac() {
        return rokMiesiac;
    }

    public void setRokMiesiac(Integer rokMiesiac) {
        this.rokMiesiac = rokMiesiac;
    }

    public BigDecimal getKwota() {
        return kwota;
    }

    public void setKwota(BigDecimal kwota) {
        this.kwota = kwota;
    }

    public Integer getPraID() {
        return praID;
    }

    public void setPraID(Integer praID) {
        this.praID = praID;
    }

    public List<DeclarationDetails> getDeclarationDetails() {
        return declarationDetails;
    }

    public void setDeclarationDetails(List<DeclarationDetails> declarationDetails) {
        this.declarationDetails = declarationDetails;
    }
}
