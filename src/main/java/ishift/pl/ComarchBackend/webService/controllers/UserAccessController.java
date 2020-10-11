package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.UserDataDTO;
import ishift.pl.ComarchBackend.webService.services.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserAccessController {

    private final UserAccessService userAccessService;

    @Autowired
    public UserAccessController(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    @PutMapping("/accessData")
    public ResponseEntity<String> updateUserData(@RequestBody UserDataDTO userDataDTO) {

        return userAccessService.changeUserAccessData(userDataDTO);
    }
}
