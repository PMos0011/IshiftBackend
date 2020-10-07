package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.UserDataDTO;
import ishift.pl.ComarchBackend.webDataModel.model.UserData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.UserDataRepository;
import ishift.pl.ComarchBackend.webService.services.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccessServiceImpl implements UserAccessService {

    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAccessServiceImpl(UserDataRepository userDataRepository, PasswordEncoder passwordEncoder) {
        this.userDataRepository = userDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<String> changeUserAccessData(UserDataDTO userDataDTO) {


        Optional<UserData> userDataNameOptional = userDataRepository.findByUserName(userDataDTO.getNewLogin());

        try {
            userDataNameOptional.ifPresent(userName -> {
                throw new RuntimeException("zajęta nazwa użytkownika");
            });
        } catch (Exception e) {
            return new ResponseEntity<>("zajęta nazwa użytkownika", HttpStatus.CONFLICT);
        }


        Optional<UserData> userDataOptional = userDataRepository.findByUserName(userDataDTO.getOldLogin());

        UserData userData = null;
        try {
            userData = userDataOptional.orElseThrow(ChangeSetPersister.NotFoundException::new);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>("nie znaleziono użytkownika", HttpStatus.NOT_FOUND);
        }

        if (passwordEncoder.matches(userDataDTO.getOldPassword(), userData.getPassword())) {

            String newName = userDataDTO.getNewLogin();
            String newPassword = userDataDTO.getNewPassword();

            if (!newName.equals(""))
                userData.setUserName(newName);

            if (!newPassword.equals(""))
                userData.setPassword(passwordEncoder.encode(newPassword));

            userDataRepository.save(userData);

            return new ResponseEntity<>("Dane zmieniono", HttpStatus.OK);
        }

        return new ResponseEntity<>("Błędne hasło", HttpStatus.BAD_REQUEST);
    }
}
