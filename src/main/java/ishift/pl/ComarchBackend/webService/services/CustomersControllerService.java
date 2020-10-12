package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.dataModel.model.CompanyData;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.AccessDataChangeDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyShortDataDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomersControllerService {

    List<WebCompanyShortDataDTO> getCustomersNames(String id);

    WebCompanyDataDTO getCompanyData(String id);

    ResponseEntity<String> changeUserAccessData(AccessDataChangeDTO accessDataChangeDTO);

    ResponseEntity<String> changeUserData(List<CompanyData> companyDataList, String id);
}
