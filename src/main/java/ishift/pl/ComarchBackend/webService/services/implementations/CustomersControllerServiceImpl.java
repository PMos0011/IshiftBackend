package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.CompanyData;
import ishift.pl.ComarchBackend.dataModel.repository.CompanyDataRepository;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.AccessDataChangeDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyShortDataDTO;
import ishift.pl.ComarchBackend.webDataModel.model.UserData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.UserDataRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.services.CustomersControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomersControllerServiceImpl implements CustomersControllerService {

    private final WebCompanyDataRepository webCompanyDataRepository;
    private final CompanyDataRepository companyDataRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;
    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomersControllerServiceImpl(WebCompanyDataRepository webCompanyDataRepository,
                                          CompanyDataRepository companyDataRepository,
                                          UserDataRepository userDataRepository,
                                          PasswordEncoder passwordEncoder) {
        this.webCompanyDataRepository = webCompanyDataRepository;
        this.companyDataRepository = companyDataRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
        this.userDataRepository = userDataRepository;
        this.passwordEncoder =passwordEncoder;
    }


    @Override
    public List<WebCompanyShortDataDTO> getCustomersNames(String id) {

        return webCompanyDataRepository.findAllByOfficeID(id).stream()
                .map(data -> new WebCompanyShortDataDTO(data.getRandomId(), data.getCompanyName())
                ).collect(Collectors.toList());
    }

    @Override
    public WebCompanyDataDTO getCompanyData(String id) {

        WebCompanyDataDTO webCompanyDataDTO = new WebCompanyDataDTO();

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));

        webCompanyDataDTO.setCompanyName(companyDataRepository.getCompanyName().getCompanyData());
        webCompanyDataDTO.setCompanyData(companyDataRepository.findAll());
        webCompanyDataDTO.setCompanyId(id);

        ClientDatabaseContextHolder.clear();

        return webCompanyDataDTO;
    }

    @Override
    public ResponseEntity<String> changeUserAccessData(AccessDataChangeDTO accessDataChangeDTO) {


        Optional<UserData> userDataNameOptional = userDataRepository.findByUserName(accessDataChangeDTO.getNewLogin());

        try {
            userDataNameOptional.ifPresent(userName -> {
                throw new RuntimeException("zajęta nazwa użytkownika");
            });
        } catch (Exception e) {
            return new ResponseEntity<>("zajęta nazwa użytkownika", HttpStatus.CONFLICT);
        }


        Optional<UserData> userDataOptional = userDataRepository.findByUserName(accessDataChangeDTO.getOldLogin());

        UserData userData = null;
        try {
            userData = userDataOptional.orElseThrow(ChangeSetPersister.NotFoundException::new);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>("nie znaleziono użytkownika", HttpStatus.NOT_FOUND);
        }

        if (passwordEncoder.matches(accessDataChangeDTO.getOldPassword(), userData.getPassword())) {

            String newName = accessDataChangeDTO.getNewLogin();
            String newPassword = accessDataChangeDTO.getNewPassword();

            if (!newName.equals(""))
                userData.setUserName(newName);

            if (!newPassword.equals(""))
                userData.setPassword(passwordEncoder.encode(newPassword));

            userDataRepository.save(userData);

            return new ResponseEntity<>("Dane zmieniono", HttpStatus.OK);
        }

        return new ResponseEntity<>("Błędne hasło", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> changeUserData(List<CompanyData> companyDataList, String id) {
        //todo exception handle (admin_user)
        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        companyDataRepository.saveAll(companyDataList);
        ClientDatabaseContextHolder.clear();

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
