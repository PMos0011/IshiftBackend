package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.webDataModel.model.UserData;

public interface UserDataService {

    UserData createAndSaveNewUser(String login, String password, String userId);

    UserData getUserDataByLoginOrElseThrowRuntimeException(String login);
}
