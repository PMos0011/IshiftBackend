package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;
import ishift.pl.ComarchBackend.webDataModel.model.PartyData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoicePDFGenerator {

    private static final String APP_PATH = System.getProperty("user.dir");

    private final String ROBOTO_FONT = APP_PATH + "/fonts/Roboto-Medium.ttf";
    private final String ROBOTO_FONT_BOLD = APP_PATH + "/fonts/Roboto-Bold.ttf";
    private final String ROBOTO_FONT_LIGHT = APP_PATH + "/fonts/Roboto-Light.ttf";

    private final Font ROBOTO_BOLD_14 = FontFactory.getFont(ROBOTO_FONT_BOLD, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14);
    private final Font ROBOTO_12 = FontFactory.getFont(ROBOTO_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
    private final Font ROBOTO_10 = FontFactory.getFont(ROBOTO_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
    private final Font ROBOTO_LIGHT_10 = FontFactory.getFont(ROBOTO_FONT_LIGHT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
    private final Font ROBOTO_LIGHT_9 = FontFactory.getFont(ROBOTO_FONT_LIGHT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9);
    private final Font ROBOTO_9 = FontFactory.getFont(ROBOTO_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 9);

    private final InvoiceFromPanel INVOICE_DATA;
    private BigDecimal amountToPay;

    public InvoicePDFGenerator(InvoiceFromPanel INVOICE_DATA) {
        this.INVOICE_DATA = INVOICE_DATA;
        amountToPay = INVOICE_DATA.getSummaryData().getBruttoAmount();
    }

    public byte[] createInvoice() throws IOException, DocumentException {

        Document document = new Document(PageSize.A4);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        document.add(headerParagraph());
        document.add(createInvoiceNumber());
        document.add(createPartiesTable());
        document.add(createCommodity());
        document.add(summary());

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

        document.add(paymentSummary());

        if (INVOICE_DATA.getSummaryData().getComments() != null) {
            document.add(addParagraphMedium10("Uwagi:"));
            document.add(addParagraphLight10(INVOICE_DATA.getSummaryData().getComments()));
            document.add(addSpacing(35));
        }

        document.add(signatures());

        byte[] bytes = document.getRole().getBytes();

        document.close();

        byte[] b = baos.toByteArray();
        baos.flush();
        baos.close();
        return b;
    }

    private PartyData getPartyData(Integer partyId) {

        //todo null values
        return INVOICE_DATA.getPartiesData().stream()
                .filter(data -> data.getPartyId().equals(partyId))
                .findFirst().get();
    }

    private String transformAccountNumber(String s) {

        String number = s.substring(0, 2) + " ";

        int cykles = (s.length() - 2) / 4;

        for (int i = 0; i < cykles; i++) {
            int gap = i * 4 + 2;
            number = number + s.substring(gap, gap + 4) + " ";
        }

        return number;
    }

    private String transformVat(String s) {

        try {
            return new BigDecimal(s)
                    .setScale(2, RoundingMode.HALF_UP)
                    .toString();
        } catch (Exception e) {
            return s;
        }
    }

    private String formatPartyIdValue(String s) {
        if (s.length() == 10) {
            return s.substring(0, 3) + "-"
                    + s.substring(3, 5) + "-"
                    + s.substring(5, 7) + "-"
                    + s.substring(7);
        } else if (s.length() == 9) {
            return s.substring(0, 3) + "-"
                    + s.substring(3, 6) + "-"
                    + s.substring(6);
        } else
            return s;
    }

    private Paragraph addParagraphLight10(String s) {
        return new Paragraph(s, ROBOTO_LIGHT_10);
    }

    private Paragraph addParagraphMedium10(String s) {
        return new Paragraph(s, ROBOTO_10);
    }

    private Paragraph addSpacing(int spacing) {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(spacing);

        return p;
    }

    private Phrase bold14Phrase(String s) {
        return new Phrase(s, ROBOTO_BOLD_14);
    }

    private Phrase medium12Phrase(String s) {
        return new Phrase(s, ROBOTO_12);
    }

    private Phrase light10Phrase(String s) {
        return new Phrase(s, ROBOTO_LIGHT_10);
    }

    private Phrase light10Phrase(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd.MM.yyyy");

        String s = formatter.format(d);
        return new Phrase(s + " r.", ROBOTO_LIGHT_10);
    }

    private Phrase medium9Phrase(String s) {
        return new Phrase(s, ROBOTO_9);
    }

    private Phrase light9Phrase(String s) {
        return new Phrase(s, ROBOTO_LIGHT_9);
    }

    private PdfPCell commodityTableCell(int align, String s) {
        PdfPCell cell = new PdfPCell(light9Phrase(s));

        cell.setBackgroundColor(new GrayColor(0.93f));
        cell.setHorizontalAlignment(align);
        cell.setPaddingLeft(3);
        cell.setPaddingRight(3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setBorderColor(BaseColor.WHITE);

        return cell;
    }

    private PdfPCell signatureCell(String s) {

        PdfPCell cell = new PdfPCell(light10Phrase(s));
        cell.setBorder(0);
        cell.setBorderWidthTop(0.3f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        return cell;
    }

    private Paragraph headerParagraph() {
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_RIGHT);
        p.setSpacingAfter(40);
        p.add(addHeaderData());
        return p;
    }

    private PdfPTable addHeaderData() {

        float[] columnWidths = {1, 1};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setWidthPercentage(45);
        table.getDefaultCell().setPadding(2);
        table.getDefaultCell().setBorder(0);

        table.addCell(light10Phrase("Miejsce wystawienia:"));
        table.addCell(light10Phrase(INVOICE_DATA.getPlaceOfIssue()));
        table.addCell(light10Phrase("Data wystawienia:"));
        table.addCell(light10Phrase(INVOICE_DATA.getIssueDate()));
        table.addCell(light10Phrase("Data sprzedaży:"));
        table.addCell(light10Phrase(INVOICE_DATA.getSellDate()));

        return table;
    }

    private PdfPTable createInvoiceNumber() {

        float[] columnWidths = {1};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setPadding(7);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.85f));

        table.addCell(bold14Phrase("Faktura numer: " + INVOICE_DATA.getInvoiceNumber()));
        table.setSpacingAfter(35);

        return table;
    }

    private PdfPTable createPartiesTable() {

        float[] columnWidths = {1, 1};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.setSpacingAfter(40);
        table.getDefaultCell().setPadding(2);
        table.getDefaultCell().setBorder(0);

        //todo enums (0-seller, 1-buyer)
        PartyData sellerData = getPartyData(0);
        PartyData buyerData = getPartyData(1);

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
        if (INVOICE_DATA.getSummaryData().getBankAcc() != null) {
            table.addCell(light10Phrase(
                    transformAccountNumber(INVOICE_DATA.getSummaryData().getBankAcc())));
            table.addCell("");
        }

        return table;
    }

    private PdfPTable createCommodity() {

        float[] columnWidths = {3, 32, 6, 6, 10, 6, 11, 6, 9, 11};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setPaddingBottom(5);
        table.getDefaultCell().setPaddingTop(5);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorderColor(BaseColor.WHITE);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.85f));

        for (String s : TableBuilder.COMMODITY_TABLE_HEADER) {
            table.addCell(medium9Phrase(s));
        }

        AtomicInteger counter = new AtomicInteger(0);
        INVOICE_DATA.getInvoiceCommodities().forEach(commodity -> {
            counter.getAndIncrement();
            table.addCell(commodityTableCell(Element.ALIGN_LEFT, String.valueOf((counter.get()))));
            table.addCell(commodityTableCell(Element.ALIGN_LEFT, commodity.getName()));
            table.addCell(commodityTableCell(Element.ALIGN_CENTER, commodity.getMeasure()));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getAmount().toString()));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getPrice().toString()));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getDiscount().toString()));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getNettoAmount().toString()));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT,
                    transformVat(commodity.getVat())));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getVatAmount().toString()));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getBruttoAmount().toString()));
        });

        table.setSpacingAfter(25);
        return table;
    }

    private PdfPTable summary() {

        float[] columnWidths = {55, 10, 35};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(0);

        table.addCell(summaryVatTable());
        table.addCell("");
        table.addCell(summaryText());
        table.setSpacingAfter(20);
        return table;
    }

    private PdfPTable summaryVatTable() {

        float[] columnWidths = {25, 25, 25, 25};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setPaddingBottom(6);
        table.getDefaultCell().setPaddingTop(6);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorderColor(BaseColor.WHITE);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.85f));

        for (String s : TableBuilder.VAT_TABLE_HEADER) {
            table.addCell(medium9Phrase(s));
        }

        INVOICE_DATA.getInvoiceVatTables().forEach(commodity -> {
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT,
                    transformVat(commodity.getVat())));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getNettoAmount().toString()));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getVatAmount().toString()));
            table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getBruttoAmount().toString()));
        });

        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(medium9Phrase("RAZEM"));
        table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getNettoAmount().toString()));
        table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getVatAmount().toString()));
        table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getBruttoAmount().toString()));

        table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table.getDefaultCell().setBorder(0);
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        return table;
    }

    private PdfPTable summaryText() {

        float[] columnWidths = {50, 50};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(60);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

        if (INVOICE_DATA.getSummaryData().getPaid() != null) {

            amountToPay = INVOICE_DATA.getSummaryData().getBruttoAmount().subtract(
                    INVOICE_DATA.getSummaryData().getPaid().setScale(2, RoundingMode.HALF_EVEN)
            );


            table.addCell(light10Phrase("Zapłacono:"));
            table.addCell(light10Phrase(INVOICE_DATA.getSummaryData().getPaid().toString() + " PLN"));

            table.addCell(light10Phrase("Do zapłaty:"));
            table.addCell(light10Phrase(amountToPay.toString() + " PLN"));
        }
        table.getDefaultCell().setPaddingBottom(15);
        table.addCell(light10Phrase("RAZEM:"));
        table.addCell(light10Phrase(INVOICE_DATA.getSummaryData().getBruttoAmount().toString() + " PLN"));

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

    private PdfPTable paymentSummary() {

        float[] columnWidths = {2, 4, 2, 2};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(0);

        PdfPCell cell = new PdfPCell(medium12Phrase("Do zapłaty: " + amountToPay.toString() + " PLN"));
        cell.setBackgroundColor(new GrayColor(0.85f));
        cell.setPadding(6);
        cell.setColspan(4);
        cell.setBorder(0);
        table.addCell(cell);

        table.addCell(light10Phrase("Słownie:"));
        table.addCell("");
        if (INVOICE_DATA.getSummaryData().getPaymentDay() != null) {
            table.addCell(light10Phrase("Termin płatności:"));
            table.addCell(light10Phrase(INVOICE_DATA.getSummaryData().getPaymentDay()));
        } else {
            table.addCell("");
            table.addCell("");
        }

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

    private PdfPTable signatures() {

        float[] columnWidths = {45, 10, 45};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidthPercentage(80);
        table.getDefaultCell().setBorder(0);

        table.addCell(signatureCell("Osoba upoważniona do wystawienia"));
        table.addCell("");
        table.addCell(signatureCell("Osoba upoważniona do odbioru"));

        return table;
    }
}
