package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPTable;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;
import ishift.pl.ComarchBackend.webDataModel.model.PartyData;

import java.util.Optional;

public class HeaderGenerator extends PDFElements {

    public Paragraph headerParagraph(InvoiceFromPanel invoice_data) {
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_RIGHT);
        p.setSpacingAfter(40);
        p.add(addHeaderData(invoice_data));
        return p;
    }

    private PdfPTable addHeaderData(InvoiceFromPanel invoice_data) {

        float[] columnWidths = {1, 1};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setWidthPercentage(45);
        table.getDefaultCell().setPadding(2);
        table.getDefaultCell().setBorder(0);

        table.addCell(light10Phrase("Miejsce wystawienia:"));
        table.addCell(light10Phrase(invoice_data.getPlaceOfIssue()));
        table.addCell(light10Phrase("Data wystawienia:"));
        table.addCell(light10Phrase(invoice_data.getIssueDate()));
        table.addCell(light10Phrase("Data sprzedaży:"));
        table.addCell(light10Phrase(invoice_data.getSellDate()));

        return table;
    }

    public PdfPTable createInvoiceNumber(InvoiceFromPanel invoice_data) {

        float[] columnWidths = {1};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setPadding(7);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.85f));

        table.addCell(bold14Phrase(invoice_data.getInvoiceTypeName() + " " + invoice_data.getInvoiceNumber()));

        Optional.ofNullable(invoice_data.getInvoiceToCorrect()).ifPresent(invoiceToCorrect->{
            table.getDefaultCell().setPadding(2);
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.addCell(light10Phrase("Dotyczy: " + invoice_data.getInvoiceToCorrect().getInvoiceTypeName()
                    + " " + invoice_data.getInvoiceToCorrect().getInvoiceNumber()
                    + " z dnia: " + convertDate(invoice_data.getInvoiceToCorrect().getIssueDate())));
            table.addCell(light10Phrase("Powód korekty: " + invoice_data.getCorrectionReason()));
        });

        table.setSpacingAfter(35);
        return table;
    }

    public PdfPTable createPartiesTable(InvoiceFromPanel invoice_data) throws RuntimeException {

        float[] columnWidths = {1, 1};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.setSpacingAfter(40);
        table.getDefaultCell().setPadding(2);
        table.getDefaultCell().setBorder(0);

        //todo enums (0-seller, 1-buyer)
        PartyData sellerData = getPartyData(invoice_data, 0);
        PartyData buyerData = getPartyData(invoice_data, 1);

        table.addCell(medium12Phrase("Sprzedawca:"));
        table.addCell(medium12Phrase("Nabywca:"));
        table.addCell(light10Phrase(sellerData.getName()));
        table.addCell(light10Phrase(buyerData.getName()));
        table.addCell(light10Phrase(sellerData.getStreet()));
        table.addCell(light10Phrase(buyerData.getStreet()));
        table.addCell(light10Phrase(sellerData.getCity()));
        table.addCell(light10Phrase(buyerData.getCity()));
        table.addCell(light10Phrase(sellerData.getIdName() + " " + formatPartyIdValue(sellerData.getIdValue())));
        table.addCell(light10Phrase(buyerData.getIdName() + " " + formatPartyIdValue(buyerData.getIdValue())));
        if (invoice_data.getSummaryData().getBankAcc() != null) {
            table.addCell(light10Phrase(
                    transformAccountNumber(invoice_data.getSummaryData().getBankAcc())));
            table.addCell("");
        }

        return table;
    }

    private PartyData getPartyData(InvoiceFromPanel invoice, Integer partyId) {

        return invoice.getPartiesData().stream()
                .filter(data -> data.getPartyId().equals(partyId))
                .findFirst()
                .orElseThrow(()->new RuntimeException("party data not found"));
    }
}
