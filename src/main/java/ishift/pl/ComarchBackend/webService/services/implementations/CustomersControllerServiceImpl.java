package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.CompanyData;
import ishift.pl.ComarchBackend.dataModel.repository.CompanyDataRepository;
import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyShortDataDTO;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.services.CustomersControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomersControllerServiceImpl implements CustomersControllerService {

    private final WebCompanyDataRepository webCompanyDataRepository;
    private final CompanyDataRepository companyDataRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;

    @Autowired
    public CustomersControllerServiceImpl(WebCompanyDataRepository webCompanyDataRepository, CompanyDataRepository companyDataRepository) {
        this.webCompanyDataRepository = webCompanyDataRepository;
        this.companyDataRepository = companyDataRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(webCompanyDataRepository);
    }


    @Override
    public List<WebCompanyShortDataDTO> getCustomersNames(String id) {

        return webCompanyDataRepository.findAllByOfficeID(id).stream()
                .map(data -> new WebCompanyShortDataDTO(data.getRandomId(), data.getCompanyName())
                ).collect(Collectors.toList());
    }

    @Override
    public WebCompanyDataDTO getCompanyData(String id) {

        WebCompanyDataDTO webCompanyDataDTO = new WebCompanyDataDTO();

        ClientDatabaseContextHolder.set(dataBasesPairListSingleton.getDBNameFromKey(id));
        webCompanyDataDTO.setCompanyName(companyDataRepository.getCompanyName().getCompanyData());
        webCompanyDataDTO.setCompanyData(companyDataRepository.findAll());
        webCompanyDataDTO.setCompanyId(id);
        ClientDatabaseContextHolder.clear();

        return webCompanyDataDTO;
    }
}
