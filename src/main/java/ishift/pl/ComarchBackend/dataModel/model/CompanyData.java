package ishift.pl.ComarchBackend.dataModel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CDN.Firma")
public class CompanyData {
    @Id
    @Column(name = "Fir_FirID")
    private Long id;

    @Column(name = "Fir_Numer")
    private Integer companyNumber;

    @Column(name = "Fir_Wartosc")
    private String companyData;

    @Column(name = "Fir_Opis")
    private String dataDescription;

    public Long getId() {
        return id;
    }

    public Integer getCompanyNumber() {
        return companyNumber;
    }

    public String getCompanyData() {
        return companyData;
    }

    public String getDataDescription() {
        return dataDescription;
    }

    @Override
    public String toString() {
        return "CompanyData{" +
                "id=" + id +
                ", companyNumber=" + companyNumber +
                ", companyData='" + companyData + '\'' +
                ", dataDescription='" + dataDescription + '\'' +
                '}';
    }
}
