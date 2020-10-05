package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;

import java.util.List;

public interface CustomersControllerService {

    List<WebCompanyDataDTO> getCustomersNames(String id);
}
