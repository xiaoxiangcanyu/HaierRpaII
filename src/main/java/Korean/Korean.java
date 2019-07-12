package Korean;

import Korean.Domain.DataDO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.im4java.core.Operation;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Korean.Util.BaseUtil.excelOutput_SAP_AR;
import static Korean.Util.BaseUtil.ocrImageFile;

/**
 * 韩国一次物流
 */
public class Korean {
    public static void main(String[] args) throws Exception {
        String companyCode = args[0];
        String filePath = args[1];
        String SAP_filepath = args[2];
//        String companyCode = "6430";
//        String filePath = "C:\\Users\\songyu\\Desktop\\haier_rpa所有资料\\OCR_Data\\项目交接文档\\一次物流-韩国-韩元图片\\7395450f44673aa0e056cdaab5f9e99.jpg";
//        String SAP_filepath = "C:\\Users\\songyu\\Desktop\\haier_rpa所有资料\\OCR_Data\\项目交接文档\\一次物流-韩国-韩元图片\\SAP.xlsx";
        //从路径中获取companycode和filename
        List<DataDO> SAPList = new ArrayList<>();
        int flag = 0;
        try {
            clearTLFunction(companyCode, filePath, SAP_filepath, SAPList);
            //检验字段是否完整，如果不完整的话输出status
            CheckStatus(SAPList);
            //将NetAmount为空的TotalAmount也置空
            OperationTotalAmount(SAPList);
            excelOutput_SAP_AR(SAPList, SAP_filepath);
        }catch (Exception e){
            DataDO dataDO = new DataDO();
            dataDO.setStatus("OCR数据识别不完整");
            SAPList.add(dataDO);
            excelOutput_SAP_AR(SAPList, SAP_filepath);
        }



    }

    /**
     * 将netamount为空的totalamount置空
     */
    private static void OperationTotalAmount(List<DataDO> SAPList) {

        for (DataDO dataDO:SAPList){
            if (dataDO.getNetAmount()!=null && dataDO.getNetAmount().length()>0){
                dataDO.setTotalAmount("");
            }
        }
    }

    /**
     * 校验字段是否完整，如果不完整的话，在status里面加入状态
     * @param sapList
     */
    private static void CheckStatus(List<DataDO> sapList) {
        for (DataDO dataDO:sapList){

            if (dataDO.getStatus()==null){
                String status = "";
                if(dataDO.getCompanyName()==null || dataDO.getCompanyName().length()==0){
                    status = status+" CompanyName为空 ";
                }
                if(dataDO.getInvoiceReferenceNumber()==null || dataDO.getInvoiceReferenceNumber().length()==0){
                    status = status+" InvoiceReferenceNumber为空 ";
                }
                if(dataDO.getVendorName()==null || dataDO.getVendorName().length()==0){
                    status = status+" VendorName为空 ";
                }
                if(dataDO.getTotalAmount()==null || dataDO.getTotalAmount().length()==0){
                    status = status+" TotalAmount为空 ";
                }
                if (dataDO.getNetAmount().length()==0){
                    status = status+"没有NetAmount";
                }
                if(dataDO.getInvoiceDate()==null || dataDO.getInvoiceDate().length()==0){
                    status = status+" InvoiceDate为空 ";
                }
                if (status.contains("读取的时候没有NetAmount")){
                    if(dataDO.getTaxBase()==null || dataDO.getTaxBase().length()==0){
                        status = status+" TaxBase为空 ";
                    }
                    if(dataDO.getTaxAmount()==null || dataDO.getTaxAmount().length()==0){
                        status = status+" TaxAmount为空 ";
                    }
                }
                dataDO.setStatus(status);
            }

        }
    }

    /**
     * 数据处理
     *
     * @param companyCode
     * @param filePath
     * @param SAP_filPath
     * @throws Exception
     */
    public static List<DataDO> clearTLFunction(String companyCode, String filePath, String SAP_filPath, List<DataDO> SAPList) throws Exception {

        File file = new File(filePath);
        byte[] fileData = FileUtils.readFileToByteArray(file);
        String InvoiceReferenceNumber = "";
        String VendorName = "";
        String CompanyName = "";
        String TotalAmount = "";
        String NetAmount = "";
        String InvoiceDate = "";
        String TaxBase = "";
        String TaxAmount = "";
        //获取json
        String json = ocrImageFile("PO-6430-0604", file.getName(), fileData);
        System.out.println("json" + json);
        JSONObject jsonObject = JSON.parseObject(json);
        //判断json数据是否返回成功
        if (jsonObject.get("result").toString().contains("success")) {
            JSONArray jsonArray = jsonObject.getJSONObject("ocrResult").getJSONArray("ranges");
            //获取税单前几个字段
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
                    case "VendorName":
                        if (object.get("value") != null) {
                            VendorName = cleanVendorName(object.get("value").toString());
                        } else {
                            VendorName = "";
                        }
                        break;
                    case "CompanyName":
                        if (object.get("value") != null) {
                            CompanyName = cleanCompanyName(object.get("value").toString());
                        } else {
                            CompanyName = "";
                        }
                        break;
                    case "TotalAmount":
                        if (object.get("value") != null) {
                            TotalAmount = cleanTotalAmount(object.get("value").toString());
                        } else {
                            TotalAmount = "";
                        }
                        break;
                    case "NetAmount":
                        if (object.get("value") != null) {
                            NetAmount = cleanNetAmount(object.get("value").toString());
                        } else {

                            NetAmount = "该数据的没有NetAmount";
                        }
                        break;
                    case "InvoiceDate":
                        if (object.get("value") != null) {
                            InvoiceDate = cleanInvoiceDate(object.get("value").toString());
                        } else {
                            InvoiceDate = "";
                        }
                        break;
                }
            }
            if ("该数据的没有NetAmount".equals(NetAmount)){
                DataDO dataDO =new DataDO();
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                Double TotalAmountTemp  =  Double.parseDouble(TotalAmount.replace(",",""));
                Double TaxBaseTemp = TotalAmountTemp / 1.1;
                TaxBase = decimalFormat.format(TaxBaseTemp);
                Double TaxAmountTemp = (TotalAmountTemp / 1.1)*0.1;
                TaxAmount = decimalFormat.format(TaxAmountTemp);
                dataDO.setCompanyName(CompanyName);
                dataDO.setInvoiceDate(InvoiceDate);
                dataDO.setNetAmount("");
                dataDO.setTotalAmount(TotalAmount);
                dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                dataDO.setVendorName(VendorName);
                dataDO.setTaxBase(TaxBase);
                dataDO.setTaxAmount(TaxAmount);
                SAPList.add(dataDO);
            }
            else {
                DataDO dataDO =new DataDO();
                dataDO.setCompanyName(CompanyName);
                dataDO.setInvoiceDate(InvoiceDate);
                dataDO.setTotalAmount(TotalAmount);
                dataDO.setInvoiceReferenceNumber(InvoiceReferenceNumber);
                dataDO.setVendorName(VendorName);
                dataDO.setNetAmount(NetAmount);
                SAPList.add(dataDO);
            }

        }else {
            DataDO dataDO = new DataDO();
            dataDO.setStatus(jsonObject.get("msg").toString());
            SAPList.add(dataDO);
        }

        return SAPList;
    }

    /**
     *
     * @param value
     * @return
     */
    private static String cleanTaxAmount(String value) {
        value = value.replace("〇","0");
//        value = value.replace(",","");
        value = value.replace(".",",");
        if (value.contains(",")){
            value =value.substring(0,value.lastIndexOf(",")+4);
        }
        return value;
    }

    /**
     * 清理VendorName
     * @param value
     * @return
     */
    private static String cleanVendorName(String value) {
        value= value.replace(" ","");
        return value;
    }

    /**
     * 清理Taxbase
     * @param value
     * @return
     */
    private static String clearTaxbase(String value) {
//        value =value.replace(",","");
        value = value.replace("〇","0");
        value = value.replace(".",",");
        return value;
    }


    /**
     * 清理InvoiceReferenceNumber
     *
     * @param value
     * @return
     */
    private static String cleanInvoiceReferenceNumber(String value) {
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

    /**
     * 清理CompanyName
     *
     * @param value
     * @return
     */
    private static String cleanCompanyName(String value) {
        value = value.replace(" ","");
        return value;
    }

    /**
     * 清理TotalAmount
     *
     * @param value
     * @return
     */
    private static String cleanTotalAmount(String value) {
//        value = value.replace(",","");
        value =value.replace("，",",");
        value = value.replace("〇","0");
        value = value.replace(".",",");
        value = value.replace("！","");
        if (value.contains(",")){
            value =value.substring(0,value.lastIndexOf(",")+4);
        }
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


}


