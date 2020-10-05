package ishift.pl.ComarchBackend.webService.controllers;


import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webService.services.CustomersControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomersController {

    private final CustomersControllerService customersControllerService;

    @Autowired
    public CustomersController(CustomersControllerService customersControllerService) {
        this.customersControllerService = customersControllerService;
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<List<WebCompanyDataDTO>> getAllCompanyName(@PathVariable String id) {

        return new ResponseEntity<>(customersControllerService.getCustomersNames(id), HttpStatus.OK);
    }
}
