package ishift.pl.ComarchBackend.webDataModel.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapFromWebDataModel implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {

        System.out.println("from web-data-model");

    }
}
