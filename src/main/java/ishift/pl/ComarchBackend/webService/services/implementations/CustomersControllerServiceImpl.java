package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.services.CustomersControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomersControllerServiceImpl implements CustomersControllerService {

    private final WebCompanyDataRepository webCompanyDataRepository;

    @Autowired
    public CustomersControllerServiceImpl(WebCompanyDataRepository webCompanyDataRepository) {
        this.webCompanyDataRepository = webCompanyDataRepository;
    }


    @Override
    public List<WebCompanyDataDTO> getCustomersNames(String id) {

        return webCompanyDataRepository.findAllByOfficeID(id).stream()
                .map(data -> new WebCompanyDataDTO(data.getRandomId(), data.getCompanyName())
                ).collect(Collectors.toList());
    }
}
