package ishift.pl.ComarchBackend.dataModel.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapFromDataModel implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("from data model");
    }
}
