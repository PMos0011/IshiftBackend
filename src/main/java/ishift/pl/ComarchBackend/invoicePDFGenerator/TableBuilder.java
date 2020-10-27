package ishift.pl.ComarchBackend.invoicePDFGenerator;

public class TableBuilder {
    public final static String[] COMMODITY_TABLE_HEADER = {
            "Lp",
            "Nazwa towaru lub usługi",
            "Jm",
            "Ilość",
            "Cena netto",
            "Rabat [%]",
            "Wartość netto",
            "VAT [%]",
            "Kwota VAT",
            "Wartość brutto"
    };

    public final static String[] VAT_TABLE_HEADER = {
            "Stawka VAT [%]",
            "Wartość netto",
            "Kwota VAT",
            "Wartośc brutto"
    };
}
