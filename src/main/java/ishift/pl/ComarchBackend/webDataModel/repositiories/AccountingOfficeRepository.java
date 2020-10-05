package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.AccountingOffice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountingOfficeRepository extends JpaRepository<AccountingOffice, Long> {

    Optional<AccountingOffice> findByName (String name);

    AccountingOffice findByRandomId(String id);
}
