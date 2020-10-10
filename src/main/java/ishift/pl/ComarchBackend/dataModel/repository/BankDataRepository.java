package ishift.pl.ComarchBackend.dataModel.repository;


import ishift.pl.ComarchBackend.dataModel.model.BankData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDataRepository extends JpaRepository<BankData,Long> {
}
