package ishift.pl.ComarchBackend.webService.services;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.UserDataDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.UsersListDTO;

public interface UserAccessService {

    boolean changeUserAccessData(UserDataDTO userDataDTO);

    UsersListDTO getUsersList();
}
