package ishift.pl.ComarchBackend.databaseService.services;

import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabasePairKeyServiceImpl implements DatabasePairKeyService {

    private final WebCompanyDataRepository webCompanyDataRepository;

    @Autowired
    public DatabasePairKeyServiceImpl(WebCompanyDataRepository webCompanyDataRepository) {
        this.webCompanyDataRepository = webCompanyDataRepository;
    }

    @Override
    public List<Pair<String, String>> createDatabaseKeyPairList() {

        return webCompanyDataRepository.findAll()
                .stream()
                .map(company -> new MutablePair<String, String>(company.getRandomId(), company.getDbName()))
                .collect(Collectors.toList());
    }
}
