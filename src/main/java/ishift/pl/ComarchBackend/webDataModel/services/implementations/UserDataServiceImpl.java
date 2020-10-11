package ishift.pl.ComarchBackend.webDataModel.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.model.UserData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.UserDataRepository;
import ishift.pl.ComarchBackend.webDataModel.services.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final PasswordEncoder passwordEncoder;
    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDataServiceImpl(PasswordEncoder passwordEncoder, UserDataRepository userDataRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserData createAndSaveNewUser(String login, String password, String userId) {

        UserData userData = new UserData(login,
                passwordEncoder.encode(password),
                "ROLE_USER",
                userId);

        return userDataRepository.save(userData);
    }

    @Override
    public UserData getUserDataByLoginOrElseThrowRuntimeException(String login) {

        return userDataRepository.findByUserName(login)
                .orElseThrow(() ->
                        new RuntimeException("błędny użytkownik bądź hasło"));
    }
}
