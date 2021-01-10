package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ishift.pl.ComarchBackend.webDataModel.model.*;

import java.io.ByteArrayOutputStream;


public class InvoicePDFGenerator extends PDFElements {

    private final InvoiceFromPanel INVOICE_DATA;

    public InvoicePDFGenerator(InvoiceFromPanel INVOICE_DATA) {
        this.INVOICE_DATA = INVOICE_DATA;
    }

    public byte[] createInvoice() throws Exception {

        HeaderGenerator headerGenerator = new HeaderGenerator();
        CommodityGenerator commodityGenerator = new CommodityGenerator();
        SummaryGenerator summaryGenerator = new SummaryGenerator(INVOICE_DATA);
        SignaturesGenerator signaturesGenerator = new SignaturesGenerator();
        DocumentsAddonsGenerator documentsAddonsGenerator = new DocumentsAddonsGenerator(INVOICE_DATA);

        Document document = new Document(PageSize.A4);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        try {
            document.add(headerGenerator.headerParagraph(INVOICE_DATA));
            document.add(headerGenerator.createInvoiceNumber(INVOICE_DATA));
            document.add(headerGenerator.createPartiesTable(INVOICE_DATA));
            document.add(commodityGenerator.createCommodity(INVOICE_DATA));
            document.add(summaryGenerator.summary(INVOICE_DATA));
            documentsAddonsGenerator.addTaxExemptionReasons(document);
            document.add(summaryGenerator.paymentSummary());
            documentsAddonsGenerator.addComments(document);
            document.add(signaturesGenerator.signatures());

        } catch (RuntimeException e) {
            throw new Exception(e);
        }

        document.close();
        byte[] b = baos.toByteArray();
        baos.flush();
        baos.close();
        return b;
    }
}
