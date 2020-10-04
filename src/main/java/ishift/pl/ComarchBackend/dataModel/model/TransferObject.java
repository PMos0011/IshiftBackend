package ishift.pl.ComarchBackend.dataModel.model;

import java.util.List;

public class TransferObject {

    private String dbName;
    private String companyName;
    private List<DeclarationData> declarationData;

    public TransferObject(String dbName, String companyName, List <DeclarationData> declarationData) {
        this.dbName = dbName;
        this.companyName = companyName;
        this.declarationData = declarationData;
    }

    public TransferObject(){};

    public String getDbName() {
        return dbName;
    }

    public List<DeclarationData> getDeclarationData() {
        return declarationData;
    }

    public String getCompanyName() {
        return companyName;
    }
}
