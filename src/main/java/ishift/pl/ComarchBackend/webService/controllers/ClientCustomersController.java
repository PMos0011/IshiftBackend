package ishift.pl.ComarchBackend.webService.controllers;


import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webService.services.ClientCustomersControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("https://panel.ishift.pl")
@RestController
public class ClientCustomersController {

    private final ClientCustomersControllerService clientCustomersControllerService;

    @Autowired
    public ClientCustomersController(ClientCustomersControllerService clientCustomersControllerService) {
        this.clientCustomersControllerService = clientCustomersControllerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<WebCompanyDataDTO>> getAllCompanyName() {

        return new ResponseEntity<>(clientCustomersControllerService.getCustomersNames(), HttpStatus.OK);
    }
}
