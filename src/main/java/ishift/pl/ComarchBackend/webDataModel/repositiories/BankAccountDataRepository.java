package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.BankAccountData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountDataRepository extends JpaRepository<BankAccountData, Long> {
}
