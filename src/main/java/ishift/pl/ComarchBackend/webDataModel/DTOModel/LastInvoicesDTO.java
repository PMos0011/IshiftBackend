package ishift.pl.ComarchBackend.webDataModel.DTOModel;

import ishift.pl.ComarchBackend.webDataModel.model.InvoiceFromPanel;

public class LastInvoicesDTO {
    private InvoiceFromPanel sellInvoice;
    private InvoiceFromPanel correctionInvoice;
    private InvoiceFromPanel advancedInvoice;

    public LastInvoicesDTO(InvoiceFromPanel sellInvoice, InvoiceFromPanel correctionInvoice, InvoiceFromPanel advancedInvoice) {
        this.sellInvoice = sellInvoice;
        this.correctionInvoice = correctionInvoice;
        this.advancedInvoice = advancedInvoice;
    }

    public InvoiceFromPanel getSellInvoice() {
        return sellInvoice;
    }

    public void setSellInvoice(InvoiceFromPanel sellInvoice) {
        this.sellInvoice = sellInvoice;
    }

    public InvoiceFromPanel getCorrectionInvoice() {
        return correctionInvoice;
    }

    public void setCorrectionInvoice(InvoiceFromPanel correctionInvoice) {
        this.correctionInvoice = correctionInvoice;
    }

    public InvoiceFromPanel getAdvancedInvoice() {
        return advancedInvoice;
    }

    public void setAdvancedInvoice(InvoiceFromPanel advancedInvoice) {
        this.advancedInvoice = advancedInvoice;
    }
}
