package Domain;

public class HaiGuanDO {
    private String GTDNumber;
    private String GTDQuantity;
    private String GTDAmount;
    private String DutyAmount;
    private String VATAmount;

    public String getGTDNumber() {
        return GTDNumber;
    }

    public void setGTDNumber(String GTDNumber) {
        this.GTDNumber = GTDNumber;
    }

    public String getGTDQuantity() {
        return GTDQuantity;
    }

    public void setGTDQuantity(String GTDQuantity) {
        this.GTDQuantity = GTDQuantity;
    }

    public String getGTDAmount() {
        return GTDAmount;
    }

    public void setGTDAmount(String GTDAmount) {
        this.GTDAmount = GTDAmount;
    }

    public String getDutyAmount() {
        return DutyAmount;
    }

    public void setDutyAmount(String dutyAmount) {
        DutyAmount = dutyAmount;
    }

    public String getVATAmount() {
        return VATAmount;
    }

    public void setVATAmount(String VATAmount) {
        this.VATAmount = VATAmount;
    }
}
