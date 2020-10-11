package ishift.pl.ComarchBackend.webDataModel.services.implementations;

import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.model.WebCompanyData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webDataModel.services.WebCompanyDataService;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.stereotype.Service;

@Service
public class WebCompanyDataServiceImpl implements WebCompanyDataService {

    private final WebCompanyDataRepository webCompanyDataRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;

    public WebCompanyDataServiceImpl(WebCompanyDataRepository webCompanyDataRepository) {
        this.webCompanyDataRepository = webCompanyDataRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
    }

    @Override
    public WebCompanyData createNewWebCompanyDataAndSaveAndAddToPairListSingleton(String companyName, String dbName, String accOfficeId) {

        WebCompanyData webCompanyData = webCompanyDataRepository.save(new WebCompanyData(companyName, dbName, accOfficeId));

        dataBasesPairListSingleton.getDatabasesList().add(
                new MutablePair<>(webCompanyData.getRandomId(), webCompanyData.getDbName()));

        return webCompanyData;
    }
}
