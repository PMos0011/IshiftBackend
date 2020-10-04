package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;

import java.util.List;

public interface ClientCustomersControllerService {

    List<WebCompanyDataDTO> getCustomersNames();
}
