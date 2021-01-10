package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFElements extends InvoiceTransforms {

    public final GrayColor LIGHT_GRAY = new GrayColor(0.93f);
    public final GrayColor GRAY = new GrayColor(0.85f);

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

    protected Paragraph addParagraphLight10(String s) {
        return new Paragraph(s, ROBOTO_LIGHT_10);
    }

    protected Paragraph addParagraphMedium10(String s) {
        return new Paragraph(s, ROBOTO_10);
    }

    protected Paragraph addSpacing(int spacing) {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(spacing);

        return p;
    }

    protected Phrase bold14Phrase(String s) {
        return new Phrase(s, ROBOTO_BOLD_14);
    }

    protected Phrase medium12Phrase(String s) {
        return new Phrase(s, ROBOTO_12);
    }

    protected Phrase light10Phrase(String s) {
        return new Phrase(s, ROBOTO_LIGHT_10);
    }

    protected Phrase light7Phrase(String s) {
        return new Phrase(s, ROBOTO_LIGHT_7);
    }

    protected Phrase light10Phrase(Date d) {

        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd.MM.yyyy");

        String s = formatter.format(d);
        return new Phrase(convertDate(d), ROBOTO_LIGHT_10);
    }

    protected Phrase medium9Phrase(String s) {
        return new Phrase(s, ROBOTO_9);
    }

    protected Phrase light9Phrase(String s) {
        return new Phrase(s, ROBOTO_LIGHT_9);
    }

    protected PdfPCell tableCell(int align, String s, GrayColor color) {
        PdfPCell cell = new PdfPCell(light9Phrase(s));
        cell.setBackgroundColor(color);
        cell.setHorizontalAlignment(align);
        cell.setPaddingLeft(3);
        cell.setPaddingRight(3);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setBorderColorTop(BaseColor.BLACK);

        return cell;
    }

    protected PdfPCell tableCell(String s) {
        return tableCell(Element.ALIGN_RIGHT, s, LIGHT_GRAY);
    }

    protected PdfPTable commodityNameTable(String s, String name) {
        float[] columnWidths = {1};
        PdfPTable table = new PdfPTable(columnWidths);
        table.getDefaultCell().setBorder(0);
        table.addCell(light7Phrase(s));
        table.addCell(light9Phrase(name));

        return table;
    }

    protected BigDecimal bigDecimalSubtract(BigDecimal before, BigDecimal after) {
        return after.subtract(before);
    }
}
