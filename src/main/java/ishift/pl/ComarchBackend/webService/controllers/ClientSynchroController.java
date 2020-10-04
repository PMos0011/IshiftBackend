package ishift.pl.ComarchBackend.webService.controllers;


import ishift.pl.ComarchBackend.ComarchBackendApplication;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.webService.services.ClientSynchroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("https://panel.ishift.pl")
public class ClientSynchroController {

    private final ClientSynchroService clientSynchroService;
    public static boolean restart;

    @Autowired
    public ClientSynchroController(ClientSynchroService clientSynchroService) {
        this.clientSynchroService = clientSynchroService;
        restart = false;
    }

    @PostMapping("/synchro")
    @ResponseStatus(HttpStatus.CREATED)
    public void test1(@RequestBody TransferObject transferObject) {

        clientSynchroService.handleIncomingData(transferObject);
    }

    @GetMapping("/synchro")
    @ResponseStatus(HttpStatus.OK)
    public void restart() {
        if (restart)
            ComarchBackendApplication.restart();
    }
}
