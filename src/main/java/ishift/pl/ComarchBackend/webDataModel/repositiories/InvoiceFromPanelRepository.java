package ishift.pl.ComarchBackend.webDataModel.repositiories;

import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface InvoiceFromPanelRepository extends JpaRepository<InvoiceFromPanel, Long> {

    List<InvoiceFromPanel> findAllBySellDateBetween(Date beginDate, Date endDate);

    InvoiceFromPanel findFirstByInvoiceTypeIdOrderByIdDesc(Integer id);

    @Query("from InvoiceFromPanel where invoiceTypeId = 3 and billingInvoice is null")
    List<InvoiceFromPanel> findAllAdvancedInvoices();
}
