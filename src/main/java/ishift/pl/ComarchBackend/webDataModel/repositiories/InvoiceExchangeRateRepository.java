package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.InvoiceExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceExchangeRateRepository extends JpaRepository<InvoiceExchangeRate, Long> {
}
