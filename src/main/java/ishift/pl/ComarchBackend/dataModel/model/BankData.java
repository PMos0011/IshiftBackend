package ishift.pl.ComarchBackend.dataModel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CDN.BnkNazwy")
public class BankData {
    @Id
    @Column(name = "BNa_BNaId")
    private Long id;

    @Column(name = "BNa_Nazwa1")
    private String name;

    @Column(name = "BNa_Nazwa2")
    private String name2;

    @Column(name = "BNa_Ulica")
    private String street;

    @Column(name = "BNa_NrDomu")
    private String streetNumber;

    @Column(name = "BNa_NrLokalu")
    private String localNumber;

    @Column(name = "BNa_Miasto")
    private String city;

    @Column(name = "BNa_KodPocztowy")
    private String zipCode;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getName2() {
        return name2;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getLocalNumber() {
        return localNumber;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }
}
