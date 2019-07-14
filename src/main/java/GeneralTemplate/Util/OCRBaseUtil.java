package GeneralTemplate.Util;

import GeneralTemplate.Domain.OCRDataDO;
import Korean.Domain.DataDO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Korean.Util.BaseUtil.ocrImageFile;

public class OCRBaseUtil {
    /**
     * 根据OCR的图片和JSON模板获取JSON数据
     *
     * @param picPath
     * @param jsonTemplate
     * @return
     */
    protected static List<OCRDataDO> GetDataFromPic(String picPath, String jsonTemplate) throws Exception {
        //==========================================初始化所有识别参数=========================================================================
        String Invoicenum = "";
        String Amount = "";
        String InvoiceReferenceNumber = "";
        String InvoiceReferenceNumber2 = "";
        String POShortText = "";
        String PurchaseOrderNumber = "";
        String Quantity = "";
        String TaxAmount = "";
        String TotalAmount = "";
        String UnitPrice = "";
        String CompanyCode = "";
        String Currency = "";
        String POSONumber = "";
        String GoodDescription = "";
        String PostingDate = "";
        String TaxCode = "";
        String Status = "";
        String Text = "";
        String BaselineDate = "";
        String ExchangeRate = "";
        String PaymentBlock = "";
        String Assignment = "";
        String HeaderText = "";
        String Filepath = "";
        String OrderShipmentDate = "";
        String ActualShipmentDate = "";
        String OCRStatus = "";
        String DownloadStatus= "";
        String VendorName = "";
        String CompanyName = "";
        String NetAmount= "";
        String InvoiceDate= "";
        String TaxBase= "";
        String item = "";
        //==========================================初始化所有识别参数=========================================================================


        //==========================================初始化OCR数据集合=========================================================================
        List<OCRDataDO> OCRList = new ArrayList<>();
        //==========================================初始化OCR数据集合=========================================================================

        File file = new File(picPath);
        byte[] fileData = FileUtils.readFileToByteArray(file);
        String json = ocrImageFile(jsonTemplate, file.getName(), fileData);
        JSONObject jsonObject = JSON.parseObject(json);
        //识别成功则进行数据封装
        if (jsonObject.get("result").toString().contains("success")) {
            JSONArray jsonArray = jsonObject.getJSONObject("ocrResult").getJSONArray("ranges");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                switch (object.get("rangeId").toString()) {
                    case "InvoiceReferenceNumber":
                        if (object.get("value") != null) {
                            InvoiceReferenceNumber = cleanInvoiceReferenceNumber(object.get("value").toString());
                        } else {
                            InvoiceReferenceNumber = "";
                        }
                        break;
                    case "InvoiceReferenceNumber2":
                        if (object.get("value") != null) {
                            InvoiceReferenceNumber2 = cleanInvoiceReferenceNumber2(object.get("value").toString());
                        } else {
                            InvoiceReferenceNumber2 = "";
                        }
                        break;
                    case "Amount":
                        if (object.get("value") != null) {
                            Amount = cleanAmount(object.get("value").toString());
                        } else {
                            Amount = "";
                        }
                        break;
                    case "POShortText":
                        if (object.get("value") != null) {
                            POShortText = cleanPOShortText(object.get("value").toString());
                        } else {
                            POShortText = "";
                        }
                        break;
                    case "PurchaseOrderNumber":
                        if (object.get("value") != null) {
                            PurchaseOrderNumber = cleanPurchaseOrderNumber(object.get("value").toString());
                        } else {
                            PurchaseOrderNumber = "";
                        }
                        break;
                    case "Quantity":
                        if (object.get("value") != null) {
                            Quantity = cleanQuantity(object.get("value").toString());
                        } else {
                            Quantity = "";
                        }
                        break;
                    case "TaxAmount":
                        if (object.get("value") != null) {
                            TaxAmount = cleanTaxAmount(object.get("value").toString());
                        } else {
                            TaxAmount = "";
                        }
                        break;
                    case "TotalAmount":
                        if (object.get("value") != null) {
                            TotalAmount = cleanTotalAmount(object.get("value").toString());
                        } else {
                            TotalAmount = "";
                        }
                        break;
                    case "UnitPrice":
                        if (object.get("value") != null) {
                            UnitPrice = cleanUnitPrice(object.get("value").toString());
                        } else {
                            UnitPrice = "";
                        }
                        break;
                    case "CompanyCode":
                        if (object.get("value") != null) {
                            CompanyCode = cleanCompanyCode(object.get("value").toString());
                        } else {
                            CompanyCode = "";
                        }
                        break;
                    case "Currency":
                        if (object.get("value") != null) {
                            Currency = cleanCurrency(object.get("value").toString());
                        } else {
                            Currency = "";
                        }
                        break;
                    case "POSONumber":
                        if (object.get("value") != null) {
                            POSONumber = cleanPOSONumber(object.get("value").toString());
                        } else {
                            POSONumber = "";
                        }
                        break;
                    case "GoodDescription":
                        if (object.get("value") != null) {
                            GoodDescription = cleanGoodDescription(object.get("value").toString());
                        } else {
                            GoodDescription = "";
                        }
                        break;
                    case "PostingDate":
                        if (object.get("value") != null) {
                            PostingDate = cleanPostingDate(object.get("value").toString());
                        } else {
                            PostingDate = "";
                        }
                        break;
                    case "TaxCode":
                        if (object.get("value") != null) {
                            TaxCode = cleanTaxCode(object.get("value").toString());
                        } else {
                            TaxCode = "";
                        }
                        break;
                    case "Text":
                        if (object.get("value") != null) {
                            Text = cleanText(object.get("value").toString());
                        } else {
                            Text = "";
                        }
                        break;
                    case "BaselineDate":
                        if (object.get("value") != null) {
                            BaselineDate = cleanBaselineDate(object.get("value").toString());
                        } else {
                            BaselineDate = "";
                        }
                        break;
                    case "ExchangeRate":
                        if (object.get("value") != null) {
                            ExchangeRate = cleanExchangeRate(object.get("value").toString());
                        } else {
                            ExchangeRate = "";
                        }
                        break;
                    case "PaymentBlock":
                        if (object.get("value") != null) {
                            PaymentBlock = cleanPaymentBlock(object.get("value").toString());
                        } else {
                            PaymentBlock = "";
                        }
                        break;
                    case "Assignment":
                        if (object.get("value") != null) {
                            Assignment = cleanAssignment(object.get("value").toString());
                        } else {
                            Assignment = "";
                        }
                        break;
                    case "HeaderText":
                        if (object.get("value") != null) {
                            HeaderText = cleanHeaderText(object.get("value").toString());
                        } else {
                            HeaderText = "";
                        }
                        break;
                    case "OrderShipmentDate":
                        if (object.get("value") != null) {
                            OrderShipmentDate = cleanOrderShipmentDate(object.get("value").toString());
                        } else {
                            OrderShipmentDate = "";
                        }
                        break;
                    case "ActualShipmentDate":
                        if (object.get("value") != null) {
                            ActualShipmentDate = cleanActualShipmentDate(object.get("value").toString());
                        } else {
                            ActualShipmentDate = "";
                        }
                        break;
                    case "VendorName":
                        if (object.get("value") != null) {
                            VendorName = cleanVendorName(object.get("value").toString());
                        } else {
                            VendorName = "";
                        }
                        break;
                    case "NetAmount":
                        if (object.get("value") != null) {
                            NetAmount = cleanNetAmount(object.get("value").toString());
                        } else {
                            NetAmount = "";
                        }
                        break;
                    case "InvoiceDate":
                        if (object.get("value") != null) {
                            InvoiceDate = cleanInvoiceDate(object.get("value").toString());
                        } else {
                            InvoiceDate = "";
                        }
                        break;
                    case "TaxBase":
                        if (object.get("value") != null) {
                            TaxBase = cleanTaxBase(object.get("value").toString());
                        } else {
                            TaxBase = "";
                        }
                        break;
                        default:
                            break;
                }
                OCRDataDO ocrDataDO = new OCRDataDO();
                ocrDataDO.setAmount(Amount);
                ocrDataDO.setInvoiceDate(InvoiceDate);
                ocrDataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                ocrDataDO.setInvoiceReferenceNumber2(InvoiceReferenceNumber2);
                ocrDataDO.setPOShortText(POShortText);
                ocrDataDO.setPurchaseOrderNumber(PurchaseOrderNumber);
                ocrDataDO.setQuantity(Quantity);
                ocrDataDO.setTaxAmount(TaxAmount);
                ocrDataDO.setTotalAmount(TotalAmount);
                ocrDataDO.setUnitPrice(UnitPrice);
                ocrDataDO.setCompanyCode(CompanyCode);
                ocrDataDO.setCurrency(Currency);
                ocrDataDO.setPOSONumber(POSONumber);
                ocrDataDO.setGoodDescription(GoodDescription);
                ocrDataDO.setPostingDate(PostingDate);
                ocrDataDO.setTaxCode(TaxCode);
                ocrDataDO.setText(Text);
                ocrDataDO.setBaselineDate(BaselineDate);
                ocrDataDO.setExchangeRate(ExchangeRate);
                ocrDataDO.setPaymentBlock(PaymentBlock);
                ocrDataDO.setAssignment(Assignment);
                ocrDataDO.setHeaderText(HeaderText);
                ocrDataDO.setFilepath(Filepath);
                ocrDataDO.setOrderShipmentDate(OrderShipmentDate);
                ocrDataDO.setActualShipmentDate(ActualShipmentDate);
                ocrDataDO.setVendorName(VendorName);
                ocrDataDO.setCompanyName(CompanyName);
                ocrDataDO.setNetAmount(NetAmount);
                ocrDataDO.setInvoiceDate(InvoiceDate);
                ocrDataDO.setTaxCode(TaxCode);
                ocrDataDO.setStatus(Status);
                ocrDataDO.setDownloadStatus(DownloadStatus);
                OCRList.add(ocrDataDO);

            }
        }
        //识别失败则根据OCR识别状态生成SAP
        else {
            OCRDataDO dataDO = new OCRDataDO();
            dataDO.setStatus(jsonObject.get("msg").toString());
            OCRList.add(dataDO);
        }

        return OCRList;
    }



    private static String cleanInvoiceReferenceNumber(String IRNumber) {
        if (!"".equals(IRNumber)){
            IRNumber = IRNumber.replace("CQ","C0");
            IRNumber = IRNumber.replace("HQ","H0");
            IRNumber = IRNumber.replace("HQQ","H00");
            IRNumber = IRNumber.replace("H0Q","H00");
            IRNumber = IRNumber.trim();
            if (IRNumber.contains(" ")){
                IRNumber = IRNumber.substring(0,IRNumber.indexOf(" "));
            }
            StringBuilder sb = new StringBuilder(IRNumber);
//            sb.replace(0, 2, "C0");
            String str = sb.substring(6,8);
            switch (str){
                case "BO":
                    sb.replace(6, 8, "B0");
                    break;
                case "CB":
                    sb.replace(6, 8, "GB");
                    break;
                case "1T":
                    sb.replace(6, 8, "IT");
                    break;
                case "1P":
                    sb.replace(6, 8, "JP");
                    break;
                case "IP":
                    sb.replace(6, 8, "JP");
                    break;
                case "jP":
                    sb.replace(6, 8, "JP");
                    break;
                case "jp":
                    sb.replace(6, 8, "JP");
                    break;
                case "」P":
                    sb.replace(6, 8, "JP");
                    break;
            }
            if (IRNumber.length() >= 12){
                if ("SH".equals(sb.substring(10,12))){
                    sb.replace(10, 12, "5H");
                }
            }
            String str1 = sb.toString().substring(8);
            str1 = str1.replace("O","0");
            str1 = str1.replace("Q","0");
            IRNumber = sb.toString().substring(0,8) + str1;
        }
        return IRNumber;
    }

    private static String cleanInvoiceReferenceNumber2(String invnumber) {
        if (!"".equals(invnumber)){
            Pattern pp = Pattern.compile("[^0-9]");
            invnumber = pp.matcher(invnumber).replaceAll("").trim();
        }
        return invnumber;
    }

    private static String cleanInvoicedate(String invoiceDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        try {
            if (!"".equals(invoiceDate)) {
                invoiceDate = invoiceDate.replace("-", "/");
                invoiceDate = invoiceDate.replace(" ", "/");
                invoiceDate = sdf1.format(sdf.parse(invoiceDate));
            }
        } catch (ParseException e) {
            invoiceDate = invoiceDate + " ";
        }
        return invoiceDate;
    }

    private static String cleanAmount(String amount) {
        if (!"".equals(amount)){
            if ("| ".equals(amount.substring(0,2))){
                amount = amount.replace("| ", "");
            }
            if ("1 ".equals(amount.substring(0,2))){
                amount = amount.replace("1 ", "");
            }

            amount =amount.toUpperCase();
            amount =amount.replace(",◦〔",".00");
            amount =amount.replace(".◦〔",".00");
            amount = amount.replace(",O〔",".00");
            amount = amount.replace(".O〔",".00");
            amount = amount.replace(",0〔",".00");
            amount =amount.replace(",O（",".00");
            amount = amount.replace(",0（",".00");
            amount =amount.replace(".O（",".00");
            amount = amount.replace(".0（",".00");
            amount = amount.replace(".0(",".00");
            amount = amount.replace(".0C",".00");
            amount = amount.replace(".O(",".00");
            amount = amount.replace(".OC",".00");
            amount = amount.replace(",0(",".00");
            amount = amount.replace(",0C",".00");
            amount = amount.replace(",O(",".00");
            amount = amount.replace(",OC",".00");
            amount = amount.replace(",", ".");
            amount = amount.replace("，", ".");
            amount = amount.replace(" ", ".");
            amount = amount.replace("S", "5");
            amount = amount.replace("I", "1");
            amount = amount.replace("l", "1");
            amount = amount.replaceAll("(?:I|!)", "1");
            amount = amount.replace("(", "1");
            amount = amount.replace(")", "1");
            amount = amount.replace("（", "1");
            amount = amount.replace("）", "1");
            amount = amount.replace("J", "1");
            amount = amount.replace("^", "");
            amount = amount.replace("&", "8");
            amount = amount.replace("g", "8");
            amount = amount.replace("o", "0");
            amount = amount.replace("O", "0");
            amount = amount.replace("Q", "0");
            amount = amount.replace("（", "1");
            amount = amount.replace("）", "1");
            amount = amount.replace("|", "1");
            amount = amount.replace("\\", "1");
            amount = amount.replace("/", "1");
            amount = amount.replace("\n", "");
            amount = amount.replace("↓", "1");
            amount = amount.replace("—", "");
            amount = amount.replace("一", "");
            amount = amount.replace("■", "");
            amount = amount.replace("-", "");
            amount = amount.replace(" ", "");
            try {
                amount = amount.substring(0,amount.indexOf(".")) + amount.substring(amount.indexOf("."),amount.indexOf(".") + 3);
                Float amount_parse = Float.parseFloat(amount);
            } catch (Exception e) {
                amount = amount + " ";
            }
        }
        return amount;
    }
    private static String cleanPOShortText(String poShortText) {
        if (!"".equals(poShortText)){
            poShortText = poShortText.replace(" ","");
            poShortText = poShortText.replace("_","");
            poShortText = poShortText.replace("\\/","V");
            Pattern p = Pattern.compile("H[^H]*K");
            Matcher m = p.matcher(poShortText);
            List<String> stringList = new ArrayList<>();
            //获取符合表达式(H*K)的子串
            while(m.find()){
                stringList.add(m.group());
            }
            if (stringList.size() >0){
                poShortText = poShortText.substring(poShortText.lastIndexOf(stringList.get(stringList.size() - 1)) + stringList.get(stringList.size() - 1).length());
            }else if (stringList.size() < 0){
                poShortText="";
            }
        }else {
            poShortText = "";
        }
        return poShortText;
    }


    private static String cleanPurchaseOrderNumber(String value) {
        return value;
    }

    private static String cleanQuantity(String quantity) {
        if (!"".equals(quantity)){
            quantity = quantity.replace("-", "");
            quantity = quantity.replace("|", "");
            quantity = quantity.replace("丨", "");
            quantity = quantity.replace("—", "");
            quantity = quantity.replace("~", "");
            quantity = quantity.replace("，", "");
            quantity = quantity.replace(",", "");
            quantity = quantity.replace("_", "");
            quantity = quantity.replace("?", "");
            quantity = quantity.replace("？", "");
            quantity = quantity.replace("j", "");
            quantity = quantity.replace("J", "");
            quantity = quantity.replace("I", "");
            quantity = quantity.replace("i", "");
            quantity = quantity.replace(":", "");
            quantity = quantity.replace("：", "");
            quantity = quantity.replace("一", "");
            quantity = quantity.replace("O", "0");
            quantity = quantity.replace(" ", "");
            Pattern p = Pattern.compile("[^0-9]");
            quantity = p.matcher(quantity).replaceAll("").trim();
            try {
                Float quantity_parse = Float.parseFloat(quantity);
            } catch (Exception e) {
                quantity = quantity + " ";
            }
        }
        return  quantity;
    }

    private static String cleanTaxAmount(String taxAmount) {
        if (!"".equals(taxAmount)){
            if ("| ".equals(taxAmount.substring(0,2))){
                taxAmount = taxAmount.replace("| ", "");
            }
            if ("1 ".equals(taxAmount.substring(0,2))){
                taxAmount = taxAmount.replace("1 ", "");
            }
            taxAmount = taxAmount.replace("|", "");
            taxAmount = taxAmount.replace("\"", "");
            taxAmount = taxAmount.replace("”", "");
            taxAmount = taxAmount.replace("-", "");
            taxAmount = taxAmount.replace("“", "");
            taxAmount = taxAmount.replace("^", "");
            taxAmount = taxAmount.replace("S", "5");
            taxAmount = taxAmount.replace("l", "1");
            taxAmount = taxAmount.replaceAll("(?:I|!)", "1");
            taxAmount = taxAmount.replace("(", "1");
            taxAmount = taxAmount.replace(")", "1");
            taxAmount = taxAmount.replace("（", "1");
            taxAmount = taxAmount.replace("）", "1");
            taxAmount = taxAmount.replace("&", "8");
            taxAmount = taxAmount.replace("^", "");
            taxAmount = taxAmount.replace("g", "8");
            taxAmount = taxAmount.replace("o", "0");
            taxAmount = taxAmount.replace("O", "0");
            taxAmount = taxAmount.replace("Q", "0");
            taxAmount = taxAmount.replace("（", "1");
            taxAmount = taxAmount.replace("）", "1");
            taxAmount = taxAmount.replace("|", "1");
            taxAmount = taxAmount.replace("\\", "1");
            taxAmount = taxAmount.replace("/", "1");
            taxAmount = taxAmount.replace("\n", "");
            taxAmount = taxAmount.replace("↓", "1");
            taxAmount = taxAmount.replace("S", "8");
            taxAmount = taxAmount.replace(",", ".");
            taxAmount = taxAmount.replace("，", ".");
            taxAmount = taxAmount.replace(" ", "");
            try {
                taxAmount = taxAmount.substring(0,taxAmount.indexOf(".")) + taxAmount.substring(taxAmount.indexOf("."),taxAmount.indexOf(".") + 3);
                Float taxAmount_parse = Float.parseFloat(taxAmount);
            } catch (Exception e) {
                taxAmount = taxAmount + " ";
            }
        }else {
            taxAmount = " ";
        }
        return taxAmount;
    }

    private static String cleanTotalAmount(String totalamount) {
        if (!"".equals(totalamount)){
            if ("| ".equals(totalamount.substring(0,2))){
                totalamount = totalamount.replace("| ", "");
            }
            if ("1 ".equals(totalamount.substring(0,2))){
                totalamount = totalamount.replace("1 ", "");
            }
            totalamount = totalamount.replace("|", "");
            totalamount = totalamount.replace("：", "");
            totalamount = totalamount.replace("^", "");
            totalamount = totalamount.replace(",", ".");
            totalamount = totalamount.replace("，", ".");
            totalamount = totalamount.replace("-", "");
            totalamount = totalamount.replace("S", "5");
            totalamount = totalamount.replace("I", "1");
            totalamount = totalamount.replace("l", "1");
            totalamount = totalamount.replaceAll("(?:I|!)", "1");
            totalamount = totalamount.replace("(", "1");
            totalamount = totalamount.replace(")", "1");
            totalamount = totalamount.replace("（", "1");
            totalamount = totalamount.replace("）", "1");
            totalamount = totalamount.replace("&", "8");
            totalamount = totalamount.replace("o", "0");
            totalamount = totalamount.replace("O", "0");
            totalamount = totalamount.replace("（", "1");
            totalamount = totalamount.replace("）", "1");
            totalamount = totalamount.replace("|", "1");
            totalamount = totalamount.replace("\\", "1");
            totalamount = totalamount.replace("/", "1");
            totalamount = totalamount.replace("\n", "");
            totalamount = totalamount.replace("↓", "1");
            totalamount = totalamount.replace(":","");
            totalamount = totalamount.replace(" ", "");
            try {
                totalamount = totalamount.substring(0,totalamount.indexOf(".")) + totalamount.substring(totalamount.indexOf("."),totalamount.indexOf(".") + 3);
                Float totalamount_parse =Float.parseFloat(totalamount);
            } catch (Exception e) {
                totalamount = totalamount + " ";
            }
        }else {
            totalamount = " ";
        }
        return totalamount;
    }


    private static String cleanUnitPrice(String price) {
        if (!"".equals(price)){
            if ("| ".equals(price.substring(0,2))){
                price = price.replace("| ", "");
            }
            if ("1 ".equals(price.substring(0,2))){
                price = price.replace("1 ", "");
            }
            price = price.replace(",", ".");
            price = price.replace("，", ".");
            price = price.replace("^", "");
            price = price.replace("S", "5");
            price = price.replace("I", "1");
            price = price.replace("l", "1");
            price = price.replaceAll("(?:I|!)", "1");
            price = price.replace("(", "1");
            price = price.replace(")", "1");
            price = price.replace("（", "1");
            price = price.replace("）", "1");
            price = price.replace("&", "8");
            price = price.replace("o", "0");
            price = price.replace("O", "0");
            price = price.replace("（", "1");
            price = price.replace("）", "1");
            price = price.replace("|", "1");
            price = price.replace("\\", "1");
            price = price.replace("/", "1");
            price = price.replace("\n", "");
            price = price.replace("↓", "1");
            price = price.replace("\n", "");
            price = price.replace(" ", "");
//            System.out.println("在拼接之前之前price:"+price);
            try {
                price = price.substring(0,price.indexOf(".")) + price.substring(price.indexOf("."),price.indexOf(".") + 3);
                System.out.println("输出结果price："+price);
                double price_parse = Double.parseDouble(price);
            } catch (Exception e) {
                price = price + " ";
            }
        }
        return price;
    }

    private static String cleanCurrency(String value) {
        return value;
    }

    private static String cleanCompanyCode(String value) {
        return value;
    }
    private static String cleanPOSONumber(String value) {
        return value;
    }
    private static String cleanGoodDescription(String value) {
        return value;
    }
    private static String cleanPostingDate(String value) {
        return value;
    }
    private static String cleanTaxCode(String value) {
        return value;
    }
    private static String cleanText(String value) {
        return value;
    }
    private static String cleanBaselineDate(String value) {
        return value;
    }
    private static String cleanExchangeRate(String value) {
        return value;
    }
    private static String cleanPaymentBlock(String value) {
        return null;
    }
    private static String cleanAssignment(String value) {
        return null;
    }
    private static String cleanHeaderText(String value) {
        return null;
    }

    private static String cleanOrderShipmentDate(String value) {
        return value;
    }

    private static String cleanActualShipmentDate(String value) {
        return value;
    }

    private static String cleanVendorName(String value) {
        value= value.replace(" ","");
        return value;
    }

    /**
     * 清理NetAmount
     *
     * @param value
     * @return
     */
    private static String cleanNetAmount(String value) {
        List<String> list = new ArrayList<>();
        value = value.replace(",","");
        String reg = "\\d+";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        value = list.get(0);
        value = value.replace("〇","0");
        value = value.replace(".",",");
        if (value.contains(",")){
            value =value.substring(0,value.lastIndexOf(",")+4);
        }
        if (value.length()>=4){
            char[] vl = value.toCharArray();
            StringBuilder stringBuilder = new StringBuilder("");
            for (int i = vl.length-1;i>=0;i--){
                stringBuilder.append(vl[i]);
            }
            String str = stringBuilder.insert(3,",").toString();
            StringBuilder stringBuilder1 = new StringBuilder("");
            char[] strarray = str.toCharArray();
            for (int i = strarray.length-1;i>=0;i--){
                stringBuilder1.append(strarray[i]);
            }
            value = stringBuilder1.toString();
        }
        return value;
    }
    /**
     * 清理InvoiceDate
     *
     * @param value
     * @return
     */
    private static String cleanInvoiceDate(String value) {
        value = value.replace("卜","1-");
        return value;
    }

    private static String cleanTaxBase(String value) {
        return value;
    }
    /**
     * 生成SAP
     *
     * @param etlList
     * @param sapPath
     */
    protected static void ExportSAPExcel(List<OCRDataDO> etlList, String sapPath) {
        //创建表格
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //定义第一个sheet页
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("Sheet1");
        //第一个sheet页数据（生成台账表）
        Row row0 = xssfSheet.createRow(0);
        String[] headers = new String[]{"Amount","InvoiceReferenceNumber", "InvoiceReferenceNumber2", "POShortText", "PurchaseOrderNumber", "Quantity", "TaxAmount","TotalAmount","UnitPrice","CompanyCode","Currency","POSONumber","GoodDescription","PostingDate","TaxCode","Text","BaselineDate","ExchangeRate","PaymentBlock","Assignment","HeaderText","OrderShipmentDate","ActualShipmentDate","VendorName","CompanyName","NetAmount","InvoiceDate","TaxBase","Status"};
        for (int i = 0; i < headers.length; i++) {
            xssfSheet.setColumnWidth(i+1, 5000);
            XSSFCell cell = (XSSFCell) row0.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        int rowNum = 1;
        for (OCRDataDO ocrDataDO : etlList){
            XSSFRow row = xssfSheet.createRow(rowNum);
            xssfSheet.setColumnWidth(rowNum, 5000);
            row.createCell(0).setCellValue(ocrDataDO.getAmount());
            row.createCell(1).setCellValue(ocrDataDO.getInvoiceReferenceNumber());
            row.createCell(2).setCellValue(ocrDataDO.getInvoiceReferenceNumber2());
            row.createCell(3).setCellValue(ocrDataDO.getPOShortText());
            row.createCell(4).setCellValue(ocrDataDO.getPurchaseOrderNumber());
            row.createCell(5).setCellValue(ocrDataDO.getQuantity());
            row.createCell(6).setCellValue(ocrDataDO.getTaxAmount());
            row.createCell(7).setCellValue(ocrDataDO.getTotalAmount());
            row.createCell(8).setCellValue(ocrDataDO.getUnitPrice());
            row.createCell(9).setCellValue(ocrDataDO.getCompanyCode());
            row.createCell(10).setCellValue(ocrDataDO.getCurrency());
            row.createCell(11).setCellValue(ocrDataDO.getPOSONumber());
            row.createCell(12).setCellValue(ocrDataDO.getGoodDescription());
            row.createCell(13).setCellValue(ocrDataDO.getPostingDate());
            row.createCell(14).setCellValue(ocrDataDO.getTaxCode());
            row.createCell(15).setCellValue(ocrDataDO.getText());
            row.createCell(16).setCellValue(ocrDataDO.getBaselineDate());
            row.createCell(17).setCellValue(ocrDataDO.getExchangeRate());
            row.createCell(18).setCellValue(ocrDataDO.getPaymentBlock());
            row.createCell(19).setCellValue(ocrDataDO.getAssignment());
            row.createCell(20).setCellValue(ocrDataDO.getHeaderText());
            row.createCell(21).setCellValue(ocrDataDO.getOrderShipmentDate());
            row.createCell(22).setCellValue(ocrDataDO.getActualShipmentDate());
            row.createCell(23).setCellValue(ocrDataDO.getVendorName());
            row.createCell(24).setCellValue(ocrDataDO.getCompanyName());
            row.createCell(25).setCellValue(ocrDataDO.getNetAmount());
            row.createCell(26).setCellValue(ocrDataDO.getInvoiceDate());
            row.createCell(27).setCellValue(ocrDataDO.getTaxBase());
            row.createCell(28).setCellValue(ocrDataDO.getStatus());
            rowNum++;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(sapPath);
            xssfWorkbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
