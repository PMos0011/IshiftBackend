package ishift.pl.ComarchBackend.dataModel.repository;

import ishift.pl.ComarchBackend.dataModel.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findAllByIssueDateBetween(Date beginDate, Date endDate);
}
