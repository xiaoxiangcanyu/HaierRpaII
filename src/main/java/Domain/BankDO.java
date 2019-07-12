package Domain;

public class BankDO {
    //企业名称
    private String EnterpriseName;
    //银行账号
    private String BankAccount;
    //单据日期
    private String BillDate;
    //摘要
    private String Abstract;
    //借方金额
    private String DebitAmount;
    //贷方金额
    private String CreditAmount;
    //单据号
    private String DocumentNo;
    //币种
    private String Currency;


    public String getEnterpriseName() {
        return EnterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        EnterpriseName = enterpriseName;
    }

    public String getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(String bankAccount) {
        BankAccount = bankAccount;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getDebitAmount() {
        return DebitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        DebitAmount = debitAmount;
    }

    public String getCreditAmount() {
        return CreditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        CreditAmount = creditAmount;
    }

    public String getDocumentNo() {
        return DocumentNo;
    }

    public void setDocumentNo(String documentNo) {
        DocumentNo = documentNo;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }
}
