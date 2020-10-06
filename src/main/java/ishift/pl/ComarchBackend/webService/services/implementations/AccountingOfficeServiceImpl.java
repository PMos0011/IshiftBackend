package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.model.AccountingOffice;
import ishift.pl.ComarchBackend.webDataModel.repositiories.AccountingOfficeRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webService.services.AccountingOfficeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountingOfficeServiceImpl implements AccountingOfficeService {

    private final AccountingOfficeRepository accountingOfficeRepository;
    private final WebCompanyDataRepository webCompanyDataRepository;

    public AccountingOfficeServiceImpl(AccountingOfficeRepository accountingOfficeRepository,
                                       WebCompanyDataRepository webCompanyDataRepository) {
        this.accountingOfficeRepository = accountingOfficeRepository;
        this.webCompanyDataRepository = webCompanyDataRepository;
    }

    @Override
    public AccountingOffice getAccountingOfficeData(String id) {

        Optional<AccountingOffice> accountingOfficeOptional = Optional.ofNullable(accountingOfficeRepository.findByRandomId(id));

        AccountingOffice accountingOffice = accountingOfficeOptional.orElseGet( ()->
                accountingOfficeRepository.findByRandomId(
                        webCompanyDataRepository.findByRandomId(id).getOfficeID()
                )
        );

        return accountingOffice;
    }
}
