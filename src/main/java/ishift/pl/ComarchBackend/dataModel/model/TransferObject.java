package ishift.pl.ComarchBackend.dataModel.model;

import java.util.List;

public class TransferObject {

    private String dbName;
    private String companyName;
    private String accountancyName;
    private String login;
    private String regon;
    private List<CompanyData> companyData;
    private List<BankAccount> bankAccounts;
    private List<Contractor> contractorList;
    private List<Invoice> invoiceList;

    public TransferObject() {
    }

    public TransferObject(String dbName,
                          String companyName,
                          String accountancyName,
                          String login, String regon,
                          List<CompanyData> companyData,
                          List<BankAccount> bankAccounts,
                          List<Contractor> contractorList,
                          List<Invoice> invoiceList) {
        this.dbName = dbName;
        this.companyName = companyName;
        this.accountancyName = accountancyName;
        this.login = login;
        this.regon = regon;
        this.companyData = companyData;
        this.bankAccounts = bankAccounts;
        this.contractorList = contractorList;
        this.invoiceList = invoiceList;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountancyName() {
        return accountancyName;
    }

    public void setAccountancyName(String accountancyName) {
        this.accountancyName = accountancyName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRegon() {
        return regon;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public List<CompanyData> getCompanyData() {
        return companyData;
    }

    public void setCompanyData(List<CompanyData> companyData) {
        this.companyData = companyData;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public List<Contractor> getContractorList() {
        return contractorList;
    }

    public void setContractorList(List<Contractor> contractorList) {
        this.contractorList = contractorList;
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }
}
