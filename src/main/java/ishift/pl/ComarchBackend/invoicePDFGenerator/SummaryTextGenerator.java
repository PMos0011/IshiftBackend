package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class SummaryTextGenerator extends PDFElements {

    protected final InvoiceFromPanel INVOICE_DATA;
    private BigDecimal amountToPay;
    private final String SUMMARY_PREFIX;

    public SummaryTextGenerator(InvoiceFromPanel INVOICE_DATA) {
        this.INVOICE_DATA = INVOICE_DATA;

        Optional.ofNullable(INVOICE_DATA.getInvoiceToCorrect()).ifPresentOrElse(
                invoiceToCorrect -> amountToPay = bigDecimalSubtract(invoiceToCorrect.getSummaryData().getBruttoAmount(),
                        INVOICE_DATA.getSummaryData()
                                .getBruttoAmount().setScale(2, RoundingMode.HALF_UP)),
                () -> amountToPay = INVOICE_DATA.getSummaryData().getBruttoAmount());

        Optional.ofNullable(INVOICE_DATA.getSummaryData().getPaid()).ifPresent(paidAmount ->
                amountToPay = bigDecimalSubtract(INVOICE_DATA.getSummaryData().getPaid(), amountToPay)
                        .setScale(2, RoundingMode.HALF_UP));

        if (amountToPay.doubleValue() < 0) {
            SUMMARY_PREFIX = "Do zwrotu: ";
            amountToPay = amountToPay.abs();
        } else
            SUMMARY_PREFIX = "Do zapłaty: ";
    }

    protected PdfPTable summaryText() {

        float[] columnWidths = {50, 50};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(60);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

        Optional.ofNullable(INVOICE_DATA.getSummaryData().getPaid())
                .ifPresent(paidAmount -> {
                    table.addCell(light10Phrase("Zapłacono:"));
                    table.addCell(light10Phrase(paidAmount.toString() + " " + INVOICE_DATA.getCurrency()));

                    table.addCell(light10Phrase(SUMMARY_PREFIX));
                    table.addCell(light10Phrase(amountToPay.toString() + " " + INVOICE_DATA.getCurrency()));
                });

        table.getDefaultCell().setPaddingBottom(15);
        table.addCell(light10Phrase("RAZEM:"));

        if (INVOICE_DATA.getInvoiceToCorrect() != null) {
            table.addCell(light10Phrase(bigDecimalSubtract(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getBruttoAmount(), INVOICE_DATA.getSummaryData().getBruttoAmount()) + " " + INVOICE_DATA.getCurrency()));
        } else
            table.addCell(light10Phrase(INVOICE_DATA.getSummaryData().getBruttoAmount().toString() + " " + INVOICE_DATA.getCurrency()));

        if (INVOICE_DATA.getSummaryData().getPaid() != null) {
            table.getDefaultCell().setPaddingBottom(2);
        }

        if (INVOICE_DATA.getSummaryData().getPaidDay() != null) {
            table.addCell(light10Phrase("Data płatności:"));
            table.addCell(light10Phrase(INVOICE_DATA.getSummaryData().getPaidDay()));
        }
        if (INVOICE_DATA.getSummaryData().getPaymentOptionIdValue() != null) {
            table.addCell(light10Phrase("Metoda płatności:"));
            table.addCell(light10Phrase(INVOICE_DATA.getSummaryData().getPaymentOptionIdValue()));
        }

        return table;
    }

    protected PdfPTable paymentSummary() {

        float[] columnWidths = {2, 4, 2, 2};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(0);

        PdfPCell cell = new PdfPCell(medium12Phrase(SUMMARY_PREFIX + amountToPay.toString() + " " + INVOICE_DATA.getCurrency()));

        cell.setBackgroundColor(GRAY);
        cell.setPadding(6);
        cell.setColspan(4);
        cell.setBorder(0);
        table.addCell(cell);

        table.addCell(light10Phrase("Słownie:"));
        table.addCell("");


        Optional.ofNullable(INVOICE_DATA.getSummaryData().getPaymentDay()).ifPresentOrElse(paymentDay -> {
            table.addCell(light10Phrase("Termin płatności:"));
            table.addCell(light10Phrase(INVOICE_DATA.getSummaryData().getPaymentDay()));
        }, () -> {
            table.addCell("");
            table.addCell("");
        });

        PdfPCell cell_ = new PdfPCell(light10Phrase(NumberToWordsTranslator.translateNumberToWords(
                amountToPay.longValue())
                + " "
                + amountToPay.remainder(BigDecimal.ONE).toString().substring(2)
                + "/100"));

        cell_.setColspan(4);
        cell_.setBorder(0);
        table.addCell(cell_);

        table.setSpacingAfter(30);

        return table;
    }
}
