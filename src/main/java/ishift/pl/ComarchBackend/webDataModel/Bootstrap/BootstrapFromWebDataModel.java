package ishift.pl.ComarchBackend.webDataModel.Bootstrap;

import ishift.pl.ComarchBackend.webDataModel.model.UserData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapFromWebDataModel implements CommandLineRunner {

    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapFromWebDataModel(UserDataRepository userDataRepository, PasswordEncoder passwordEncoder) {
        this.userDataRepository = userDataRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {

        System.out.println("from web-data-model");
        createUser();

    }

    public void addAdminUser() {
        UserData userAdmin = new UserData("admin",
                passwordEncoder.encode("admin"),
                "ROLE_ADMIN");

        userDataRepository.save(userAdmin);
    }

    public void createUser(){

        if(userDataRepository.findByUserName("mb")==null) {
            UserData userAdmin = new UserData("mb",
                    passwordEncoder.encode("mbmbmbmb"),
                    "ROLE_ADMIN");

            userDataRepository.save(userAdmin);
        }
    }
}
