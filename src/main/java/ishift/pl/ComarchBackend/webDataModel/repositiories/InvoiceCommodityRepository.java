package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.InvoiceCommodity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceCommodityRepository extends JpaRepository<InvoiceCommodity, Long> {
}
