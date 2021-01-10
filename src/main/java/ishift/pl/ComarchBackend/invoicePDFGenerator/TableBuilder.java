package ishift.pl.ComarchBackend.invoicePDFGenerator;

public class TableBuilder {
    protected final static String[] COMMODITY_TABLE_HEADER = {
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

    protected final static String[] VAT_TABLE_HEADER = {
            "Stawka VAT [%]",
            "Wartość netto",
            "Kwota VAT",
            "Wartośc brutto"
    };
}
