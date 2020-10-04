package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.DTOModel.WebCompanyDataDTO;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.services.ClientCustomersControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientCustomersControllerServiceImpl implements ClientCustomersControllerService {

    private final WebCompanyDataRepository webCompanyDataRepository;

    @Autowired
    public ClientCustomersControllerServiceImpl(WebCompanyDataRepository webCompanyDataRepository) {
        this.webCompanyDataRepository = webCompanyDataRepository;
    }


    @Override
    public List<WebCompanyDataDTO> getCustomersNames() {

        return webCompanyDataRepository.findAll().stream()
                .map(data -> new WebCompanyDataDTO(data.getRandomId(), data.getCompanyName())
                ).collect(Collectors.toList());
    }
}
