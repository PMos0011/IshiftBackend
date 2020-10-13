package ishift.pl.ComarchBackend.dataModel.model;

import javax.persistence.*;

@Entity
@Table(name = "CDN.Kontrahenci")
public class Contractor {

    @Id
    @Column(name = "Knt_KntId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Knt_Nip")
    private String nip;

    @Column(name = "Knt_Nazwa1")
    private String name1;
    @Column(name = "Knt_Nazwa2")
    private String name2;
    @Column(name = "Knt_Nazwa3")
    private String name3;

    @Column(name = "Knt_Ulica")
    private String street;

    @Column(name = "Knt_NrDomu")
    private String streetNumber;

    @Column(name = "Knt_NrLokalu")
    private String localNumber;

    @Column(name = "Knt_KodPocztowy")
    private String zipCode;

    @Column(name = "Knt_Miasto")
    private String city;

    @Column(name = "Knt_Regon")
    private String regon;

    @Column(name = "Knt_Email")
    private String email;

    @Column(name = "Knt_Telefon1")
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(String localNumber) {
        this.localNumber = localNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegon() {
        return regon;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
