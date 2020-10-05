package ishift.pl.ComarchBackend.webService.controllers;


import ishift.pl.ComarchBackend.ComarchBackendApplication;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.webService.services.SynchroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SynchroController {

    private final SynchroService synchroService;
    public static boolean restart;

    @Autowired
    public SynchroController(SynchroService synchroService) {
        this.synchroService = synchroService;
        restart = false;
    }

    @PostMapping("/synchro")
    @ResponseStatus(HttpStatus.CREATED)
    public void test1(@RequestBody TransferObject transferObject) {

        synchroService.handleIncomingData(transferObject);
    }

    @GetMapping("/synchro")
    @ResponseStatus(HttpStatus.OK)
    public void restart() {
        if (restart)
            ComarchBackendApplication.restart();
    }
}
