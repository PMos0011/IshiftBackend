package ishift.pl.ComarchBackend.webDataModel.services.implementations;

import ishift.pl.ComarchBackend.dataModel.model.Invoice;
import ishift.pl.ComarchBackend.webDataModel.model.WebContactor;
import ishift.pl.ComarchBackend.webDataModel.model.WebInvoice;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebContractorRepository;
import ishift.pl.ComarchBackend.webDataModel.repositiories.WebInvoiceRepository;
import ishift.pl.ComarchBackend.webDataModel.services.WebInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebInvoiceServiceImpl implements WebInvoiceService {

    private final WebInvoiceRepository webInvoiceRepository;
    private final WebContractorRepository webContractorRepository;

    @Autowired
    public WebInvoiceServiceImpl(WebInvoiceRepository webInvoiceRepository, WebContractorRepository webContractorRepository) {
        this.webInvoiceRepository = webInvoiceRepository;
        this.webContractorRepository = webContractorRepository;
    }

    @Override
    public List<WebInvoice> convertInvoiceListToWebInvoiceListAndSave(List<Invoice> invoiceList) {

        List<WebInvoice> webInvoiceList = invoiceList.stream()
                .map(invoice -> {
                    String contractorName = (invoice.getName1() + " " + invoice.getName2() + " " + invoice.getName3()).trim();
                    //todo exception handle
                    WebContactor webContactor = webContractorRepository.findByName(contractorName).orElse(new WebContactor(0L,"nieznany"));
                     return new WebInvoice(webContactor.getId(),
                             webContactor.getName(),
                             invoice.getPaymentDate(),
                             invoice.getIssueDate(),
                             invoice.getDocName(),
                             invoice.getNetto(),
                             invoice.getVat(),
                             invoice.getBrutto(),
                             invoice.getBrutto(),
                             null,
                             "");

                })
                .collect(Collectors.toList());

        return webInvoiceRepository.saveAll(webInvoiceList);
    }
}
