package Korean.Domain;

/**
 *韩国一次性物流实体类
 */
public class DataDO {
    private String InvoiceReferenceNumber;
    private String VendorName;
    private String CompanyName;
    private String TotalAmount;
    private String NetAmount;
    private String InvoiceDate;
    private String TaxBase;
    private String TaxAmount;
    private String Status;

    public String getInvoiceReferenceNumber() {
        return InvoiceReferenceNumber;
    }

    public void setInvoiceReferenceNumber(String invoiceReferenceNumber) {
        InvoiceReferenceNumber = invoiceReferenceNumber;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getTaxBase() {
        return TaxBase;
    }

    public void setTaxBase(String taxBase) {
        TaxBase = taxBase;
    }

    public String getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        TaxAmount = taxAmount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
