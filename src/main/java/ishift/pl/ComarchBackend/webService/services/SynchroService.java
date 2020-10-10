package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SynchroService {


    ResponseEntity<String> setNewClientData(TransferObject transferObject);

    ResponseEntity<String> handleNewDeclarationData (List<DeclarationData> declarationData, String id);
}
