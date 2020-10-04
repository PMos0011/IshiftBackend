package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;

import java.util.List;

public interface ClientDocumentsService {

    List<DeclarationData> getDeclarations(String key);
}
