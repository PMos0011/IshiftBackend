package ishift.pl.ComarchBackend.dataModel.model;

import javax.persistence.*;

@Entity
@Table(name = "CDN.BnkRachunki")
public class BankAccount {
    @Id
    @Column(name = "BRa_BRaID")
    private Long id;

    @Column(name = "BRa_BNaID")
    private Integer bankID;

    @Column(name = "BRa_RachunekNr")
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "BRa_BNaID", updatable = false, insertable = false)
    private BankData bankData;

    public Long getId() {
        return id;
    }

    public Integer getBankID() {
        return bankID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BankData getBankData() {
        return bankData;
    }
}
