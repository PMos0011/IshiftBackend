package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceTypeRepository extends JpaRepository<InvoiceType, Long> {
}
