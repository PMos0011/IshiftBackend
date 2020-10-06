package ishift.pl.ComarchBackend.webDataModel.DTOModel;

public class WebCompanyShortDataDTO {

    private final String id;
    private final String companyName;


    public WebCompanyShortDataDTO(String id, String companyName) {
        this.id = id;
        this.companyName = companyName;
    }

    public String getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }
}
