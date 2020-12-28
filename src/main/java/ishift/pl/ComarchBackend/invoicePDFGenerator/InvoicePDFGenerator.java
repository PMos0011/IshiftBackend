package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ishift.pl.ComarchBackend.webDataModel.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InvoicePDFGenerator {

//    private final String ROBOTO_FONT = "/home/Comarch/fonts/Roboto-Medium.ttf";
//    private final String ROBOTO_FONT_BOLD = "/home/Comarch/fonts/Roboto-Bold.ttf";
//    private final String ROBOTO_FONT_LIGHT = "/home/Comarch/fonts/Roboto-Light.ttf";

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
    private final Font ROBOTO_LIGHT_7 = FontFactory.getFont(ROBOTO_FONT_LIGHT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);

    private final InvoiceFromPanel INVOICE_DATA;
    private BigDecimal amountToPay;
    private String summaryPrefix;

    public InvoicePDFGenerator(InvoiceFromPanel INVOICE_DATA) {
        this.INVOICE_DATA = INVOICE_DATA;
        amountToPay = INVOICE_DATA.getSummaryData().getBruttoAmount();
        this.summaryPrefix = "Do zapłaty: ";
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

    private Phrase light7Phrase(String s) {
        return new Phrase(s, ROBOTO_LIGHT_7);
    }

    private Phrase light10Phrase(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd.MM.yyyy");

        String s = formatter.format(d);
        return new Phrase(convertDate(d), ROBOTO_LIGHT_10);
    }

    private String convertDate(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd.MM.yyyy");

        return formatter.format(d) + " r.";
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
        cell.setBorderColorTop(BaseColor.BLACK);

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

        table.addCell(bold14Phrase(INVOICE_DATA.getInvoiceTypeName() + " " + INVOICE_DATA.getInvoiceNumber()));

        if (INVOICE_DATA.getInvoiceToCorrect() != null) {
            table.getDefaultCell().setPadding(2);
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.addCell(light10Phrase("Dotyczy: " + INVOICE_DATA.getInvoiceToCorrect().getInvoiceTypeName()
                    + " " + INVOICE_DATA.getInvoiceToCorrect().getInvoiceNumber()
                    + " z dnia: " + convertDate(INVOICE_DATA.getInvoiceToCorrect().getIssueDate())));
            table.addCell(light10Phrase("Powód korekty: " + INVOICE_DATA.getCorrectionReason()));
        }

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

    private PdfPTable commodityNameTable(String s, String name) {
        float[] columnWidths = {1};
        PdfPTable table = new PdfPTable(columnWidths);
        table.getDefaultCell().setBorder(0);
        table.addCell(light7Phrase(s));
        table.addCell(light9Phrase(name));

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

            table.getDefaultCell().setBackgroundColor(new GrayColor(0.93f));

            if (INVOICE_DATA.getInvoiceToCorrect() != null) {
                InvoiceCommodity commodityToCorrect =
                        INVOICE_DATA.getInvoiceToCorrect().getInvoiceCommodities().stream().filter(toCorrect ->
                                commodityFilter(toCorrect, commodity)
                        ).findFirst().get();

                table.addCell(commodityTableCell(Element.ALIGN_LEFT, String.valueOf(counter.get())));
                table.addCell(commodityNameTable("przed korektą:", commodity.getName()));
                table.addCell(commodityTableCell(Element.ALIGN_CENTER, commodityToCorrect.getMeasure()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodityToCorrect.getAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodityToCorrect.getPrice().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodityToCorrect.getDiscount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodityToCorrect.getNettoAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT,
                        transformVat(commodityToCorrect.getVat())));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodityToCorrect.getVatAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodityToCorrect.getBruttoAmount().toString()));

                table.getDefaultCell().setBorderColorTop(BaseColor.WHITE);

                table.addCell(commodityTableCell(Element.ALIGN_LEFT, " "));
                table.addCell(commodityNameTable("po korekcie:", commodity.getName()));
                table.addCell(commodityTableCell(Element.ALIGN_CENTER, commodity.getMeasure()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getPrice().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getDiscount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getNettoAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT,
                        transformVat(commodity.getVat())));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getVatAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, commodity.getBruttoAmount().toString()));


                table.addCell(commodityTableCell("korekta: "));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(commodityToCorrect.getAmount(), commodity.getAmount())));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(commodityToCorrect.getPrice(), commodity.getPrice())));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(commodityToCorrect.getDiscount(), commodity.getDiscount())));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(commodityToCorrect.getNettoAmount(), commodity.getNettoAmount())));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT, " "));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(commodityToCorrect.getVatAmount(), commodity.getVatAmount())));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(commodityToCorrect.getBruttoAmount(), commodity.getBruttoAmount())));


            } else {
                table.addCell(commodityTableCell(Element.ALIGN_LEFT, String.valueOf(counter.get())));
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
            }

        });

        table.setSpacingAfter(25);
        return table;
    }

    //todo refactor
    private PdfPCell commodityTableCell(String s) {
        PdfPCell cell = new PdfPCell(light9Phrase(s));
        cell.setColspan(3);
        cell.setBackgroundColor(new GrayColor(0.85f));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingLeft(3);
        cell.setPaddingRight(3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setBorderColor(BaseColor.WHITE);

        return cell;
    }

    private PdfPCell commodityTableCellWhite(int align, String s) {
        PdfPCell cell = new PdfPCell(light9Phrase(s));
        cell.setBackgroundColor(new GrayColor(0.85f));
        cell.setHorizontalAlignment(align);
        cell.setPaddingLeft(3);
        cell.setPaddingRight(3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setBorderColorTop(BaseColor.BLACK);

        return cell;
    }

    private String calculateCommoditiesSummary(BigDecimal before, BigDecimal after) {
        return after.subtract(before).toString();
    }

    private String calculateCommoditiesSummary(BigDecimal before, BigDecimal after, Double multiply) {
        return after.subtract(before).multiply(BigDecimal.valueOf(multiply))
                .setScale(2, RoundingMode.HALF_UP).toString();
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

        //todo
        if (INVOICE_DATA.getInvoiceToCorrect() != null) {
            Set<String> allVatRates = new HashSet<>();
            INVOICE_DATA.getInvoiceToCorrect().getInvoiceVatTables().forEach(VatTable -> allVatRates.add(VatTable.getVat()));
            INVOICE_DATA.getInvoiceVatTables().forEach(VatTable -> allVatRates.add(VatTable.getVat()));

            Set<String> sorted = allVatRates.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));

            sorted.forEach(vatRate -> {

                InvoiceVatTable vatTableToCorrect =
                        INVOICE_DATA.getInvoiceToCorrect().getInvoiceVatTables().stream()
                                .filter(vatTable -> vatTable.getVat().equals(vatRate))
                                .findFirst().orElse(new InvoiceVatTable(
                                vatRate,
                                new BigDecimal(0),
                                new BigDecimal(0),
                                new BigDecimal(0)
                        ));

                InvoiceVatTable correctedVatTable =
                        INVOICE_DATA.getInvoiceVatTables().stream()
                                .filter(vatTable -> vatTable.getVat().equals(vatRate))
                                .findFirst().orElse(new InvoiceVatTable(
                                vatRate,
                                new BigDecimal(0),
                                new BigDecimal(0),
                                new BigDecimal(0)
                        ));

                table.addCell(commodityTableCell(Element.ALIGN_RIGHT,
                        transformVat(vatTableToCorrect.getVat())));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, vatTableToCorrect.getNettoAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, vatTableToCorrect.getVatAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, vatTableToCorrect.getBruttoAmount().toString()));

                table.addCell(commodityTableCell(Element.ALIGN_RIGHT,
                        "po korekcie:"));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, correctedVatTable.getNettoAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, correctedVatTable.getVatAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, correctedVatTable.getBruttoAmount().toString()));


                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        "korekta:"));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(vatTableToCorrect.getNettoAmount(), correctedVatTable.getNettoAmount())));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(vatTableToCorrect.getVatAmount(), correctedVatTable.getVatAmount())));
                table.addCell(commodityTableCellWhite(Element.ALIGN_RIGHT,
                        calculateCommoditiesSummary(vatTableToCorrect.getBruttoAmount(), correctedVatTable.getBruttoAmount())));

            });

            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);


            table.addCell(medium9Phrase("RAZEM"));
            table.addCell(medium9Phrase(calculateCommoditiesSummary(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getNettoAmount(), INVOICE_DATA.getSummaryData().getNettoAmount())));
            table.addCell(medium9Phrase(calculateCommoditiesSummary(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getVatAmount(), INVOICE_DATA.getSummaryData().getVatAmount())));
            table.addCell(medium9Phrase(calculateCommoditiesSummary(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getBruttoAmount(), INVOICE_DATA.getSummaryData().getBruttoAmount())));

            // table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            // table.getDefaultCell().setBorder(0);

        } else {
            INVOICE_DATA.getInvoiceVatTables().forEach(invoiceVatTable -> {

                table.addCell(commodityTableCell(Element.ALIGN_RIGHT,
                        transformVat(invoiceVatTable.getVat())));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, invoiceVatTable.getNettoAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, invoiceVatTable.getVatAmount().toString()));
                table.addCell(commodityTableCell(Element.ALIGN_RIGHT, invoiceVatTable.getBruttoAmount().toString()));
            });


            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

            table.addCell(medium9Phrase("RAZEM"));
            table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getNettoAmount().toString()));
            table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getVatAmount().toString()));
            table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getBruttoAmount().toString()));

        }

        Optional.ofNullable(INVOICE_DATA.getInvoiceExchangeRate()).ifPresent(invoiceExchangeRate -> {

            //todo
            if (INVOICE_DATA.getInvoiceToCorrect() != null) {
                table.addCell(medium9Phrase("Po przeliczeniu (PLN)"));
                table.addCell(medium9Phrase(calculateCommoditiesSummary(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getNettoAmount(),
                        INVOICE_DATA.getSummaryData().getNettoAmount(),
                        INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate())));
                table.addCell(medium9Phrase(calculateCommoditiesSummary(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getVatAmount(),
                        INVOICE_DATA.getSummaryData().getVatAmount(),
                        INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate())));
                table.addCell(medium9Phrase(calculateCommoditiesSummary(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getBruttoAmount(),
                        INVOICE_DATA.getSummaryData().getBruttoAmount(),
                        INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate())));

            } else {

                table.addCell(medium9Phrase("Po przeliczeniu (PLN)"));
                table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getNettoAmount()
                        .multiply(BigDecimal.valueOf(INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate()))
                        .setScale(2, RoundingMode.HALF_UP).toString()));
                table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getVatAmount().multiply(BigDecimal.valueOf(INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate()))
                        .setScale(2, RoundingMode.HALF_UP).toString()));
                table.addCell(medium9Phrase(INVOICE_DATA.getSummaryData().getBruttoAmount().multiply(BigDecimal.valueOf(INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate()))
                        .setScale(2, RoundingMode.HALF_UP).toString()));
            }

            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.getDefaultCell().setBorder(0);
            table.getDefaultCell().setPaddingBottom(1);
            table.getDefaultCell().setPaddingTop(1);

            table.getDefaultCell().setColspan(4);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(light9Phrase(" Przeliczono po kursie 1 " + INVOICE_DATA.getCurrency() + " = " + INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate() + " PLN"));

            if (!INVOICE_DATA.getInvoiceExchangeRate().getExchangeBasis().equals("własny kurs"))
                table.addCell(light9Phrase(" tabela kursów średnich NBP nr " + INVOICE_DATA.getInvoiceExchangeRate().getExchangeBasis() + " z dnia " + convertDate(INVOICE_DATA.getInvoiceExchangeRate().getExchangeDate())));


        });


        return table;
    }

    private PdfPTable summaryText() {

        float[] columnWidths = {50, 50};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(60);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

        //todo

        if (amountToPay.doubleValue() < 0) {
            summaryPrefix = "Do zwrotu: ";
            amountToPay = amountToPay.abs();
        }

        if (INVOICE_DATA.getInvoiceToCorrect() == null) {

            if (INVOICE_DATA.getSummaryData().getPaid() != null) {

                amountToPay = INVOICE_DATA.getSummaryData().getBruttoAmount().subtract(
                        INVOICE_DATA.getSummaryData().getPaid().setScale(2, RoundingMode.HALF_EVEN)
                );


                table.addCell(light10Phrase("Zapłacono:"));
                table.addCell(light10Phrase(INVOICE_DATA.getSummaryData().getPaid().toString() + " " + INVOICE_DATA.getCurrency()));

                table.addCell(light10Phrase(summaryPrefix));
                table.addCell(light10Phrase(amountToPay.toString() + " " + INVOICE_DATA.getCurrency()));
            }
        }


        table.getDefaultCell().setPaddingBottom(15);
        table.addCell(light10Phrase("RAZEM:"));


        //todo refactor correction
        if (INVOICE_DATA.getInvoiceToCorrect() != null) {
            table.addCell(light10Phrase(calculateCommoditiesSummary(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getBruttoAmount(), INVOICE_DATA.getSummaryData().getBruttoAmount()) + " " + INVOICE_DATA.getCurrency()));
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

    private PdfPTable paymentSummary() {

        float[] columnWidths = {2, 4, 2, 2};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(0);

        //todo
        if (INVOICE_DATA.getInvoiceToCorrect() != null) {
            amountToPay = INVOICE_DATA.getSummaryData().getBruttoAmount().subtract(INVOICE_DATA.getInvoiceToCorrect().getSummaryData().getBruttoAmount());
            if (amountToPay.doubleValue() < 0) {
                summaryPrefix = "Do zwrotu: ";
                amountToPay = amountToPay.abs();
            }
        }

        PdfPCell cell = new PdfPCell(medium12Phrase(summaryPrefix + amountToPay.toString() + " " + INVOICE_DATA.getCurrency()));

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

    private boolean commodityFilter(InvoiceCommodity commodityToCorrect, InvoiceCommodity correctingCommodity) {

        return commodityToCorrect.getCorrectionId() == null
                ? commodityToCorrect.getId().equals(correctingCommodity.getId())
                : commodityToCorrect.getCorrectionId().equals(correctingCommodity.getId());
    }
}
