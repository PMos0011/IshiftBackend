package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.webDataModel.model.AccountingOffice;

public interface AccountingOfficeService {

    AccountingOffice getAccountingOfficeDataByNameIfNotExistCreateNewAndSave(String name, String dbId);

    AccountingOffice getAccountingOfficeDataByIdOrThrowRuntimeException(String id);
}
