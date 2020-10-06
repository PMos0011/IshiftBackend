package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyShortDataDTO;

import java.util.List;

public interface CustomersControllerService {

    List<WebCompanyShortDataDTO> getCustomersNames(String id);

    WebCompanyDataDTO getCompanyData(String id);
}
