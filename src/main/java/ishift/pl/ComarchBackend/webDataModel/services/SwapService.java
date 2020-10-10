package ishift.pl.ComarchBackend.webDataModel.services;


import ishift.pl.ComarchBackend.dataModel.model.BankAccount;
import ishift.pl.ComarchBackend.dataModel.model.DeclarationData;
import ishift.pl.ComarchBackend.dataModel.model.TransferObject;
import ishift.pl.ComarchBackend.webDataModel.model.Swap;

import java.util.List;
import java.util.Optional;

public interface SwapService {

    void saveCompanyData(TransferObject transferObject);

    void saveDeclarationData(List<DeclarationData> declarationDataList, String dbName) throws RuntimeException;

}
