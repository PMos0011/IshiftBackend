package ishift.pl.ComarchBackend.webDataModel.DTOModel;

import ishift.pl.ComarchBackend.dataModel.model.CompanyData;

import java.util.List;

public class WebCompanyDataDTO {
    private List<CompanyData> companyData;
    private String companyName;
    private String companyId;

    public List<CompanyData> getCompanyData() {
        return companyData;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyData(List<CompanyData> companyData) {
        this.companyData = companyData;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
