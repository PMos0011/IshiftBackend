package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.dataModel.model.BankAccount;
import ishift.pl.ComarchBackend.webDataModel.model.BankAccountData;

import java.util.List;

public interface BankAccountDataService {

    List<BankAccountData> convertBankAccountListToBankDataAccountListAndSave(List<BankAccount>bankAccountList);
}
