package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.dataModel.model.TransferObject;

public interface SynchroService {

    void handleIncomingData(TransferObject transferObject);
}
