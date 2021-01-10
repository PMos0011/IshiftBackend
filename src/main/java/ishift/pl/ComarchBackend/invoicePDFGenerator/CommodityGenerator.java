package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPTable;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceCommodity;
import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CommodityGenerator extends PDFElements {

    public PdfPTable createCommodity(InvoiceFromPanel invoice) throws  RuntimeException{

        float[] columnWidths = {3, 32, 6, 6, 10, 6, 11, 6, 9, 11};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setPaddingBottom(5);
        table.getDefaultCell().setPaddingTop(5);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorderColor(BaseColor.WHITE);
        table.getDefaultCell().setBackgroundColor(GRAY);

        for (String s : TableBuilder.COMMODITY_TABLE_HEADER) {
            table.addCell(medium9Phrase(s));
        }

        AtomicInteger counter = new AtomicInteger(0);
        invoice.getInvoiceCommodities().forEach(commodity -> {
            counter.getAndIncrement();

            table.getDefaultCell().setBackgroundColor(LIGHT_GRAY);

           Optional.ofNullable(invoice.getInvoiceToCorrect())
                    .ifPresentOrElse(invoiceToCorrect -> {
                                var commodityToCorrect = invoiceToCorrect.getInvoiceCommodities().stream()
                                        .filter(toCorrect -> commodityFilter(toCorrect, commodity))
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException("Correction commodity not found"));


                                table.addCell(tableCell(Element.ALIGN_LEFT, String.valueOf(counter.get()), LIGHT_GRAY));
                                table.addCell(commodityNameTable("przed korektą:", commodity.getName()));
                                table.addCell(tableCell(Element.ALIGN_CENTER, commodityToCorrect.getMeasure(), LIGHT_GRAY));
                                addRows(table, commodityToCorrect, LIGHT_GRAY);

                                table.getDefaultCell().setBorderColorTop(BaseColor.WHITE);

                                table.addCell(tableCell(Element.ALIGN_LEFT, " ", LIGHT_GRAY));
                                table.addCell(commodityNameTable("po korekcie:", commodity.getName()));
                                table.addCell(tableCell(Element.ALIGN_CENTER, commodity.getMeasure(), LIGHT_GRAY));
                                addRows(table, commodity, LIGHT_GRAY);


                                table.addCell(tableCell("korekta: "));
                                addRows(table, createDifferencesCommodity(commodityToCorrect, commodity), GRAY);
                            },


                            () -> {
                                table.addCell(tableCell(Element.ALIGN_LEFT, String.valueOf(counter.get()), LIGHT_GRAY));
                                table.addCell(tableCell(Element.ALIGN_LEFT, commodity.getName(), LIGHT_GRAY));
                                table.addCell(tableCell(Element.ALIGN_CENTER, commodity.getMeasure(), LIGHT_GRAY));
                                addRows(table, commodity, LIGHT_GRAY);
                            });
        });

        table.setSpacingAfter(25);
        return table;
    }

    private void addRows(PdfPTable table, InvoiceCommodity commodity, GrayColor color) {

        table.addCell(tableCell(Element.ALIGN_RIGHT, commodity.getAmount().toString(), color));
        table.addCell(tableCell(Element.ALIGN_RIGHT, commodity.getPrice().toString(), color));
        table.addCell(tableCell(Element.ALIGN_RIGHT, commodity.getDiscount().toString(), color));
        table.addCell(tableCell(Element.ALIGN_RIGHT, commodity.getNettoAmount().toString(), color));
        table.addCell(tableCell(Element.ALIGN_RIGHT,
                transformVat(commodity.getVat()), color));
        table.addCell(tableCell(Element.ALIGN_RIGHT, commodity.getVatAmount().toString(), color));
        table.addCell(tableCell(Element.ALIGN_RIGHT, commodity.getBruttoAmount().toString(), color));

    }

    private boolean commodityFilter(InvoiceCommodity commodityToCorrect, InvoiceCommodity correctingCommodity) {

        return commodityToCorrect.getCorrectionId() == null
                ? commodityToCorrect.getId().equals(correctingCommodity.getId())
                : commodityToCorrect.getCorrectionId().equals(correctingCommodity.getId());
    }

    private InvoiceCommodity createDifferencesCommodity(InvoiceCommodity before, InvoiceCommodity after) {
        InvoiceCommodity commodity = new InvoiceCommodity();

        commodity.setAmount(
                bigDecimalSubtract(after.getAmount(), before.getAmount())
        );
        commodity.setPrice(
                bigDecimalSubtract(after.getPrice(), before.getPrice())
        );
        commodity.setDiscount(
                bigDecimalSubtract(after.getDiscount(), before.getDiscount())
        );
        commodity.setNettoAmount(
                bigDecimalSubtract(after.getNettoAmount(), before.getNettoAmount())
        );
        commodity.setVatAmount(
                bigDecimalSubtract(after.getVatAmount(), before.getVatAmount())
        );
        commodity.setBruttoAmount(
                bigDecimalSubtract(after.getBruttoAmount(), before.getBruttoAmount())
        );

        return commodity;

    }
}
