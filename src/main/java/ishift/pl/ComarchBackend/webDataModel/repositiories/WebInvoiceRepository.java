package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.WebInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebInvoiceRepository extends JpaRepository<WebInvoice,Long> {
}
