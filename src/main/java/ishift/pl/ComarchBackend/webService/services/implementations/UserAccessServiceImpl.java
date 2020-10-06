package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.UserDataDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.UsersListDTO;
import ishift.pl.ComarchBackend.webDataModel.model.UserData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.UserDataRepository;
import ishift.pl.ComarchBackend.webService.services.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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
    public boolean changeUserAccessData(UserDataDTO userDataDTO) {

        Optional<UserData> userDataOptional = userDataRepository.findByDbId(userDataDTO.getId());

        UserData userData = null;
        try {
            userData = userDataOptional.orElseThrow(ChangeSetPersister.NotFoundException::new);
        } catch (ChangeSetPersister.NotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if (passwordEncoder.matches(userDataDTO.getOldPassword(),userData.getPassword())) {

            String newName = userDataDTO.getLogin();
            String newPassword = userDataDTO.getPassword();

            if (!newName.equals(""))
                userData.setUserName(newName);

            if (!newPassword.equals(""))
                userData.setPassword(passwordEncoder.encode(newPassword));

            userDataRepository.save(userData);

            return true;
        }

        return false;
    }

    @Override
    public UsersListDTO getUsersList() {

        return new UsersListDTO(userDataRepository.getUsersList());
    }
}
