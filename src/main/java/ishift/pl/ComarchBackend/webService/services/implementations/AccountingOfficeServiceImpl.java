package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.model.AccountingOffice;
import ishift.pl.ComarchBackend.webDataModel.repositiories.AccountingOfficeRepository;
import ishift.pl.ComarchBackend.webService.services.AccountingOfficeService;
import org.springframework.stereotype.Service;

@Service
public class AccountingOfficeServiceImpl implements AccountingOfficeService {

    private final AccountingOfficeRepository accountingOfficeRepository;

    public AccountingOfficeServiceImpl(AccountingOfficeRepository accountingOfficeRepository) {
        this.accountingOfficeRepository = accountingOfficeRepository;
    }

    @Override
    public AccountingOffice getAccountingOfficeData(String id) {

        return accountingOfficeRepository.findByRandomId(id);
    }
}
