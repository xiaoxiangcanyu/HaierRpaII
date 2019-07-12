package Korean.Util;

import Korean.Domain.DataDO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据处理公共类
 */
public class BaseUtil {
    public static final String REGER = "[^，,.>0-9]";
    public static final String[] REGER_CURRENCY = {"USD","SD","EUR","GBP","JPY","CNY","HKD","AED","AUD"};//货币号
    /**
     * 该方法用于OCR模板扫面发票，获取所需数据
     * @param imageType
     * @param fileName
     * @param fileData
     * @return
     * @throws Exception
     */
    public static String ocrImageFile(String imageType,String fileName,byte[] fileData) throws Exception{
        String baseUrl = "http://ocrserver.openserver.cn:8090/OcrServer/ocr/ocrImageByTemplate";
//        String baseUrl = "http://10.138.93.103:8080/OcrServer/ocr/ocrImageByTemplate";
        HttpPost post = new HttpPost(baseUrl);
        ContentType contentType = ContentType.create("multipart/form-data", Charset.forName("UTF-8"));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName("UTF-8"));
        builder.addBinaryBody("file", fileData, contentType, fileName);// 文件流
        builder.addTextBody("imageType", imageType,contentType);// 类似浏览器表单提交，对应input的name和value
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpResponse response = httpclient.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                String result = EntityUtils.toString(response.getEntity(),"utf-8");
                return result;
            } else {
                throw new Exception(EntityUtils.toString(response.getEntity(),"utf-8"));
            }
        }finally {
            httpclient.close();
        }
    }

    /**
     * 清洗单号
     * @param IRNumber
     * @return
     */
    public static String clearIRNumber(String IRNumber){
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

    /**
     * 数据处理(必须使用货币号拆分出每条数据时使用)
     * @param data
     * @return
     */
    public static String clearData(String data){
        if (!"".equals(data)){
            data = data.replace("U","11");
            data = data.replace("O","0");
            data = data.replace("*",".");
            Pattern p = Pattern.compile(REGER);//提取有效数字
            data = p.matcher(data).replaceAll("").trim();
            if (!"".equals(data)){//获取到的数据不能为空
                data = data.replaceAll("(?:,|>)",".");//将标点换成小数点
                if (data.contains(".")){//判断小数点是否存在
                    String str = data.substring(data.indexOf(".",0) + 1);//截取小数点之后的字符串
                    if (str.length() >= 2){
                        data = data.substring(0, data.indexOf(".",0) + 3);
                    }
                }
            }
        }
        return data;
    }
    /**
     * 韩国OCR数据处理生成SAP表
     * @param middleEastSAPList
     */
    public static void excelOutput_SAP_AR(List<DataDO> middleEastSAPList, String filePath) {
        //创建表格
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //定义第一个sheet页
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("Sheet1");
        //第一个sheet页数据（生成台账表）
        Row row0 = xssfSheet.createRow(0);
        String[] headers = new String[]{"InvoiceReferenceNumber","VendorName", "CompanyName", "TotalAmount", "NetAmount", "InvoiceDate", "TaxBase","TaxAmount","Status"};
        for (int i = 0; i < headers.length; i++) {
            xssfSheet.setColumnWidth(i+1, 5000);
            XSSFCell cell = (XSSFCell) row0.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        int rowNum = 1;
        for (DataDO dataDO : middleEastSAPList){
            XSSFRow row = xssfSheet.createRow(rowNum);
            xssfSheet.setColumnWidth(rowNum, 5000);
            row.createCell(0).setCellValue(dataDO.getInvoiceReferenceNumber());
            row.createCell(1).setCellValue(dataDO.getVendorName());
            row.createCell(2).setCellValue(dataDO.getCompanyName());
            row.createCell(3).setCellValue(dataDO.getTotalAmount());
            row.createCell(4).setCellValue(dataDO.getNetAmount());
            row.createCell(5).setCellValue(dataDO.getInvoiceDate());
            row.createCell(6).setCellValue(dataDO.getTaxBase());
            row.createCell(7).setCellValue(dataDO.getTaxAmount());
            row.createCell(8).setCellValue(dataDO.getStatus().replace("没有NetAmount",""));
            rowNum++;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            xssfWorkbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
