package ishift.pl.ComarchBackend.webService.Bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapFromWeb implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        System.out.println("from web-services");
    }
}
