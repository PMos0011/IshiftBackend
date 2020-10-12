package ishift.pl.ComarchBackend.webService.controllers;


import ishift.pl.ComarchBackend.dataModel.model.CompanyData;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.AccessDataChangeDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyShortDataDTO;
import ishift.pl.ComarchBackend.webService.services.CustomersControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomersController {

    private final CustomersControllerService customersControllerService;

    @Autowired
    public CustomersController(CustomersControllerService customersControllerService) {
        this.customersControllerService = customersControllerService;
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<List<WebCompanyShortDataDTO>> getAllCompanyName(@PathVariable String id) {

        return new ResponseEntity<>(customersControllerService.getCustomersNames(id), HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity <WebCompanyDataDTO> getCompanyData(@PathVariable String id) {

        return new ResponseEntity<>(customersControllerService.getCompanyData(id), HttpStatus.OK);
    }

    @PutMapping("/settings/accessData")
    public ResponseEntity<String> updateAccessData(@RequestBody AccessDataChangeDTO accessDataChangeDTO) {

        return customersControllerService.changeUserAccessData(accessDataChangeDTO);
    }

    @PutMapping("/settings/myData/{id}")
    public ResponseEntity<String> updateUserData(@PathVariable String id, @RequestBody List<CompanyData> companyDataList) {

        return customersControllerService.changeUserData(companyDataList,id);
    }
}
