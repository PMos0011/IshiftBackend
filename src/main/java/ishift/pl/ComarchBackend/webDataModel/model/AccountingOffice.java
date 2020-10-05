package ishift.pl.ComarchBackend.webDataModel.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountingOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String randomId;

    public AccountingOffice() {
    }

    public AccountingOffice(String companyName, String randomId) {
        this.name = companyName;
        this.randomId = randomId;
    }

    public String getCompanyName() {
        return name;
    }

    public String getRandomId() {
        return randomId;
    }

}
