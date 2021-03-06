package ishift.pl.ComarchBackend.webDataModel.model;

import javax.persistence.*;

@Entity
public class Swap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String databaseName;
    @Lob
    private byte[] customerData;
    @Lob
    private byte[] declarationData;

    public Swap() {
    }

    public Swap(String databaseName) {
        this.databaseName = databaseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public byte[] getCustomerData() {
        return customerData;
    }

    public void setCustomerData(byte[] customerData) {
        this.customerData = customerData;
    }

    public byte[] getDeclarationData() {
        return declarationData;
    }

    public void setDeclarationData(byte[] declarationData) {
        this.declarationData = declarationData;
    }
}
