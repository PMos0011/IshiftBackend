package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.dataModel.model.TransferObject;

public interface ClientSynchroService {

    void handleIncomingData(TransferObject transferObject);
}
