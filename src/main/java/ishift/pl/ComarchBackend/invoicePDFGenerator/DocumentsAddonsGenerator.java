package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;

public class DocumentsAddonsGenerator extends PDFElements {
    private final InvoiceFromPanel INVOICE_DATA;

    public DocumentsAddonsGenerator(InvoiceFromPanel invoice_data) {
        INVOICE_DATA = invoice_data;
    }

    protected void addTaxExemptionReasons(Document document) throws DocumentException {

        if (INVOICE_DATA.getSummaryData().getVatExemptionLabelNp() != null) {
            document.add(addParagraphMedium10(INVOICE_DATA.getSummaryData().getVatExemptionLabelNp()));
            document.add(addParagraphLight10(INVOICE_DATA.getSummaryData().getVatExemptionValueNp()));
            document.add(addSpacing(12));
        }

        if (INVOICE_DATA.getSummaryData().getVatExemptionLabelZw() != null) {
            document.add(addParagraphMedium10(INVOICE_DATA.getSummaryData().getVatExemptionLabelZw()));
            document.add(addParagraphLight10(INVOICE_DATA.getSummaryData().getVatExemptionValueZw()));
            document.add(addSpacing(12));
        }
    }

    protected void addComments(Document document) throws DocumentException {
        if (INVOICE_DATA.getSummaryData().getComments() != null) {
            document.add(addParagraphMedium10("Uwagi:"));
            document.add(addParagraphLight10(INVOICE_DATA.getSummaryData().getComments()));
            document.add(addSpacing(35));
        }
    }
}
