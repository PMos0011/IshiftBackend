package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.dataModel.model.TransferObject;

public interface SynchroService {

    String handleIncomingData(TransferObject transferObject);
}
