package ishift.pl.ComarchBackend.invoicePDFGenerator;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class SignaturesGenerator extends PDFElements {

    private PdfPCell signatureCell(String s) {

        PdfPCell cell = new PdfPCell(light10Phrase(s));
        cell.setBorder(0);
        cell.setBorderWidthTop(0.3f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        return cell;
    }

    protected PdfPTable signatures() {

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
