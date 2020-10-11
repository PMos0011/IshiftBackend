package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.webDataModel.model.WebCompanyData;

public interface WebCompanyDataService {

    WebCompanyData createNewWebCompanyDataAndSaveAndAddToPairListSingleton(String companyName, String dbName, String accOfficeId);
}
