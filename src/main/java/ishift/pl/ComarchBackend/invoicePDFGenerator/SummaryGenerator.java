package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPTable;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceVatTable;
import ishift.pl.ComarchBackend.webDataModel.model.SummaryData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SummaryGenerator extends SummaryTextGenerator {


    public SummaryGenerator(InvoiceFromPanel INVOICE_DATA) {
        super(INVOICE_DATA);
    }

    protected PdfPTable summary(InvoiceFromPanel invoice) {

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
        table.getDefaultCell().setBackgroundColor(GRAY);

        for (String s : TableBuilder.VAT_TABLE_HEADER) {
            table.addCell(medium9Phrase(s));
        }

        Optional.ofNullable(INVOICE_DATA.getInvoiceToCorrect()).ifPresentOrElse(invoiceToCorrect -> {
                    Set<String> allVatRates = new HashSet<>();
                    invoiceToCorrect.getInvoiceVatTables().forEach(VatTable -> allVatRates.add(VatTable.getVat()));
                    INVOICE_DATA.getInvoiceVatTables().forEach(VatTable -> allVatRates.add(VatTable.getVat()));

                    Set<String> sorted = allVatRates.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));

                    sorted.forEach(vatRate -> {

                        InvoiceVatTable vatTableToCorrect = createInvoiceVatTable(vatRate, INVOICE_DATA.getInvoiceToCorrect().getInvoiceVatTables());
                        InvoiceVatTable correctedVatTable = createInvoiceVatTable(vatRate, INVOICE_DATA.getInvoiceVatTables());

                        addRow(table, transformVat(vatTableToCorrect.getVat()), vatTableToCorrect, LIGHT_GRAY);
                        addRow(table, "po korekcie:", vatTableToCorrect, LIGHT_GRAY);
                        addRow(table, "korekta:", createDifferencesVatTable(vatTableToCorrect, correctedVatTable), LIGHT_GRAY);
                    });

                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                    addSummaryRow(table, "RAZEM", createDifferencesSummaryData(INVOICE_DATA.getInvoiceToCorrect().getSummaryData(), INVOICE_DATA.getSummaryData()));
                },
                () -> {
                    INVOICE_DATA.getInvoiceVatTables()
                            .forEach(invoiceVatTable -> addRow(table, transformVat(invoiceVatTable.getVat()), invoiceVatTable, LIGHT_GRAY));

                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                    addSummaryRow(table, "RAZEM", INVOICE_DATA.getSummaryData());
                });


        Optional.ofNullable(INVOICE_DATA.getInvoiceExchangeRate()).ifPresent(invoiceExchangeRate -> {
            Optional.ofNullable(INVOICE_DATA.getInvoiceToCorrect()).ifPresentOrElse(invoiceToCorrect ->
                    addSummaryRow(table, "Po przeliczeniu (PLN)",
                            createExchangeSummaryData(
                                    createDifferencesSummaryData(invoiceToCorrect.getSummaryData(), INVOICE_DATA.getSummaryData()),
                                    INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate()
                            )), () ->
                    addSummaryRow(table, "Po przeliczeniu (PLN)",
                            createExchangeSummaryData(INVOICE_DATA.getSummaryData(), INVOICE_DATA.getInvoiceExchangeRate().getExchangeRate())));


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


    private InvoiceVatTable createInvoiceVatTable(String vatRate, Set<InvoiceVatTable> invoiceVatTables) {

        return invoiceVatTables.stream()
                .filter(vatTable -> vatTable.getVat().equals(vatRate))
                .findFirst().orElse(new InvoiceVatTable(
                        vatRate,
                        new BigDecimal(0),
                        new BigDecimal(0),
                        new BigDecimal(0)
                ));
    }

    private void addRow(PdfPTable table, String _1stColumn, InvoiceVatTable invoiceVatTable, GrayColor color) {
        table.addCell(tableCell(Element.ALIGN_RIGHT, _1stColumn, color));
        table.addCell(tableCell(Element.ALIGN_RIGHT, invoiceVatTable.getNettoAmount().toString(), color));
        table.addCell(tableCell(Element.ALIGN_RIGHT, invoiceVatTable.getVatAmount().toString(), color));
        table.addCell(tableCell(Element.ALIGN_RIGHT, invoiceVatTable.getBruttoAmount().toString(), color));
    }

    private void addSummaryRow(PdfPTable table, String _1stColumn, SummaryData summaryData) {
        table.addCell(medium9Phrase(_1stColumn));
        table.addCell(medium9Phrase(summaryData.getNettoAmount().toString()));
        table.addCell(medium9Phrase(summaryData.getVatAmount().toString()));
        table.addCell(medium9Phrase(summaryData.getBruttoAmount().toString()));
    }

    private InvoiceVatTable createDifferencesVatTable(InvoiceVatTable before, InvoiceVatTable after) {
        InvoiceVatTable invoiceVatTable = new InvoiceVatTable();

        invoiceVatTable.setNettoAmount(bigDecimalSubtract(before.getNettoAmount(), after.getNettoAmount()));
        invoiceVatTable.setVatAmount(bigDecimalSubtract(before.getVatAmount(), after.getVatAmount()));
        invoiceVatTable.setBruttoAmount(bigDecimalSubtract(before.getBruttoAmount(), after.getBruttoAmount()));

        return invoiceVatTable;
    }

    private SummaryData createDifferencesSummaryData(SummaryData before, SummaryData after) {
        SummaryData summaryData = new SummaryData();
        summaryData.setNettoAmount(bigDecimalSubtract(before.getNettoAmount(), after.getNettoAmount()));
        summaryData.setBruttoAmount(bigDecimalSubtract(before.getBruttoAmount(), after.getBruttoAmount()));
        summaryData.setVatAmount(bigDecimalSubtract(before.getVatAmount(), after.getVatAmount()));
        return summaryData;
    }

    private SummaryData createExchangeSummaryData(SummaryData summaryData, Double exchangeRate) {
        SummaryData newSummaryData = new SummaryData();

        newSummaryData.setNettoAmount(summaryData.getNettoAmount()
                .multiply(BigDecimal.valueOf(exchangeRate))
                .setScale(2, RoundingMode.HALF_UP));

        newSummaryData.setBruttoAmount(summaryData.getBruttoAmount()
                .multiply(BigDecimal.valueOf(exchangeRate))
                .setScale(2, RoundingMode.HALF_UP));

        newSummaryData.setVatAmount(summaryData.getVatAmount()
                .multiply(BigDecimal.valueOf(exchangeRate))
                .setScale(2, RoundingMode.HALF_UP));

        return newSummaryData;
    }
}
