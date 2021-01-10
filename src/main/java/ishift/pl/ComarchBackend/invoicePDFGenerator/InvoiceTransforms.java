package ishift.pl.ComarchBackend.invoicePDFGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceTransforms {

    protected String transformAccountNumber(String s) {

        String number = s.substring(0, 2) + " ";

        int cykles = (s.length() - 2) / 4;

        for (int i = 0; i < cykles; i++) {
            int gap = i * 4 + 2;
            number = number + s.substring(gap, gap + 4) + " ";
        }

        return number;
    }

    protected String transformVat(String s) {

        try {
            return new BigDecimal(s)
                    .setScale(2, RoundingMode.HALF_UP)
                    .toString();
        } catch (Exception e) {
            return s;
        }
    }

    protected String formatPartyIdValue(String s) {
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

    protected String convertDate(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd.MM.yyyy");

        return formatter.format(d) + " r.";
    }
}
