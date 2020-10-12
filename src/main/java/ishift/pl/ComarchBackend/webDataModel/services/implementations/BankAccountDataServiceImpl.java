package ishift.pl.ComarchBackend.webDataModel.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.BankAccount;
import ishift.pl.ComarchBackend.webDataModel.model.BankAccountData;
import ishift.pl.ComarchBackend.webDataModel.repositiories.BankAccountDataRepository;
import ishift.pl.ComarchBackend.webDataModel.services.BankAccountDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankAccountDataServiceImpl implements BankAccountDataService {

    private final BankAccountDataRepository bankAccountDataRepository;

    public BankAccountDataServiceImpl(BankAccountDataRepository bankAccountDataRepository) {
        this.bankAccountDataRepository = bankAccountDataRepository;
    }

    @Override
    public List<BankAccountData> convertBankAccountListToBankDataAccountListAndSave(List<BankAccount> bankAccountList) {

        List<BankAccountData> bankAccountDataList = new ArrayList<>();

        bankAccountList.forEach(acc->{
            bankAccountDataList.add(
                    new BankAccountData(
                            acc.getAccountNumber().replace(" ",""),
                            acc.getBankData().getName(),
                            acc.getBankData().getStreet(),
                            acc.getBankData().getStreetNumber(),
                            acc.getBankData().getLocalNumber(),
                            acc.getBankData().getZipCode(),
                            acc.getBankData().getCity()
                            )
            );
        });
        return bankAccountDataRepository.saveAll(bankAccountDataList);
    }
}
