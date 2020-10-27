package ishift.pl.ComarchBackend.webService.bootstrap;

import ishift.pl.ComarchBackend.databaseService.configuration.ClientDatabaseContextHolder;
import ishift.pl.ComarchBackend.databaseService.data.DataBasesPairListSingleton;
import ishift.pl.ComarchBackend.invoicePDFGenerator.InvoicePDFGenerator;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;
import ishift.pl.ComarchBackend.webDataModel.repositiories.InvoiceFromPanelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BootstrapFromWeb implements CommandLineRunner {

    private final InvoiceFromPanelRepository invoiceFromPanelRepository;
    private final DataBasesPairListSingleton dataBasesPairListSingleton;

    public BootstrapFromWeb(InvoiceFromPanelRepository invoiceFromPanelRepository) {
        this.invoiceFromPanelRepository = invoiceFromPanelRepository;
        this.dataBasesPairListSingleton = DataBasesPairListSingleton.getInstance(null);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("from web-services");

        ClientDatabaseContextHolder.set("cdn_adam_ma_lej_shdnffbcg");
        Optional<InvoiceFromPanel> invoiceFromPanel = invoiceFromPanelRepository.findById(1L);
        ClientDatabaseContextHolder.clear();

        InvoicePDFGenerator invoicePDFGenerator = new InvoicePDFGenerator(invoiceFromPanel.get());
        invoicePDFGenerator.createInvoice();
    }
}
