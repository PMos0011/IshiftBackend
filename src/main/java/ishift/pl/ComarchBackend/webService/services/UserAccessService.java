package ishift.pl.ComarchBackend.webService.services;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.UserDataDTO;
import org.springframework.http.ResponseEntity;

public interface UserAccessService {

    ResponseEntity<String> changeUserAccessData(UserDataDTO userDataDTO);
}
