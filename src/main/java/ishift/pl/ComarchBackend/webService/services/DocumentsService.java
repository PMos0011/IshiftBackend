package ishift.pl.ComarchBackend.webService.services;


import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;

import java.util.List;

public interface DocumentsService {

    List<DeclarationData> getDeclarations(String key);

    DeclarationData getDeclarationById(String key, Long id);
}
