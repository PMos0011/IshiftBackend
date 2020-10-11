package ishift.pl.ComarchBackend.webDataModel.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.model.AccountingOffice;
import ishift.pl.ComarchBackend.webDataModel.repositiories.AccountingOfficeRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebCompanyDataRepository;
import ishift.pl.ComarchBackend.webDataModel.services.AccountingOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountingOfficeServiceImpl implements AccountingOfficeService {

    private final AccountingOfficeRepository accountingOfficeRepository;
    private final WebCompanyDataRepository webCompanyDataRepository;


    @Autowired
    public AccountingOfficeServiceImpl(AccountingOfficeRepository accountingOfficeRepository,
                                       WebCompanyDataRepository webCompanyDataRepository) {
        this.accountingOfficeRepository = accountingOfficeRepository;
        this.webCompanyDataRepository = webCompanyDataRepository;
    }

    @Override
    public AccountingOffice getAccountingOfficeDataByNameIfNotExistCreateNewAndSave(String name, String dbId) {

        return accountingOfficeRepository.save(
                accountingOfficeRepository.findByName(name)
                        .orElse(new AccountingOffice(name, dbId)));
    }

    @Override
    public AccountingOffice getAccountingOfficeDataByIdOrThrowRuntimeException(String id) {
        return accountingOfficeRepository.findByRandomId(id)
                .orElseGet(() ->
                        getAccountingOfficeByCustomerIdOrThrowRuntimeException(id)
                );
    }


    private AccountingOffice getAccountingOfficeByCustomerIdOrThrowRuntimeException(String id) {
        return accountingOfficeRepository.findByRandomId(
                webCompanyDataRepository.findByRandomId(id).getOfficeID()
        ).orElseThrow(() -> new RuntimeException("nie znaleziono danych biura"));
    }
}