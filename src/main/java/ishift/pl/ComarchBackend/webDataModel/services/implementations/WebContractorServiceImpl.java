package ishift.pl.ComarchBackend.webDataModel.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.Contractor;
import ishift.pl.ComarchBackend.webDataModel.model.WebContactor;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebContractorRepository;
import ishift.pl.ComarchBackend.webDataModel.services.WebContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebContractorServiceImpl implements WebContractorService {

    private final WebContractorRepository webContractorRepository;

    @Autowired
    public WebContractorServiceImpl(WebContractorRepository webContractorRepository) {
        this.webContractorRepository = webContractorRepository;
    }

    @Override
    public List<WebContactor> convertFromContractorListToWebContractorListAndSave(List<Contractor> contractorList) {

        List<WebContactor> webContactorList = contractorList.stream()
                .map(contractor -> new WebContactor(
                        contractor.getNip(),
                        contractor.getRegon(),
                        (contractor.getName1() + " " + contractor.getName2() + " " + contractor.getName3()).trim(),
                        contractor.getStreet(),
                        contractor.getStreetNumber(),
                        contractor.getLocalNumber(),
                        contractor.getZipCode(),
                        contractor.getCity(),
                        contractor.getEmail(),
                        contractor.getPhoneNumber()
                ))
                .collect(Collectors.toList());

        return webContractorRepository.saveAll(webContactorList);
    }
}
