package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;

import java.util.List;

public interface SwapService {

    void saveCompanyData(TransferObject transferObject);

    void saveDeclarationData(List<DeclarationData> declarationDataList, String dbName) throws RuntimeException;

}
