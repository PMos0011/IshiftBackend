package ishift.pl.ComarchBackend.webDataModel.model;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.util.Random;

@Entity
public class WebCompanyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String dbName;
    private String randomId;
    private String officeID;

    public WebCompanyData() {
    }

    public WebCompanyData(String companyName, String dbName, String officeID) {
        this.companyName = companyName;
        this.dbName = dbName;
        this.officeID = officeID;

        this.randomId = RandomStringUtils.randomAlphanumeric(
                new Random().ints(8, 15)
                        .findFirst().getAsInt());
    }

    public String getCompanyName() {
        return companyName;
    }

    public Long getId() {
        return id;
    }

    public String getDbName() {
        return dbName;
    }

    public String getRandomId() {
        return randomId;
    }

    public String getOfficeID() {
        return officeID;
    }
}
