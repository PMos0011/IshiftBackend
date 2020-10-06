package ishift.pl.ComarchBackend.webService.controllers;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.UserDataDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.UsersListDTO;
import ishift.pl.ComarchBackend.webService.services.UserAccessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserAccessController {

    private final UserAccessService userAccessService;

    public UserAccessController(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    @GetMapping("/accessData")
    public ResponseEntity<UsersListDTO> getUsersList(){
        UsersListDTO usersListDTO = userAccessService.getUsersList();
        System.out.println(usersListDTO.getUserList().size());

        return new ResponseEntity<UsersListDTO>(usersListDTO,HttpStatus.OK);
    }

    @PutMapping("/accessData")
    public ResponseEntity updateUserData(@RequestBody UserDataDTO userDataDTO) {

        System.out.println(userDataDTO.getId() + userDataDTO.getLogin() + userDataDTO.getPassword() + userDataDTO.getOldPassword());

        if(userAccessService.changeUserAccessData(userDataDTO))
            return new ResponseEntity(HttpStatus.CREATED);
        else return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }
}
