package HaiGuan.main;

import Domain.HaiGuanDO;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HaiGuan {
    public static void main(String[] args) throws IOException {
//        String PdfPath = args[0];
//        String ExcelPath = args[1];
        String PdfPath = "C:\\Users\\songyu\\Desktop\\haier_rpa所有资料\\OCR_Data\\项目交接文档\\俄罗斯海关单和发票\\1\\GTD_10317120_130519_0034744.pdf";
        String ExcelPath = "C:\\Users\\songyu\\Desktop\\haier_rpa所有资料\\OCR_Data\\项目交接文档\\俄罗斯海关单和发票\\1\\excel.xls";
        String TxtPath = PdfPath.substring(0,PdfPath.lastIndexOf("."))+".txt";

        ReadPdf(PdfPath,TxtPath);
        List<HaiGuanDO> list = new ArrayList<>();
        //原始的数据行
        List<String> Str_list = ReadTxt(TxtPath);
        //进行处理产生待生成数据
        list = GenerateDate(list, Str_list);
        excelOutput_Data(list, ExcelPath);
        File file = new File(TxtPath);
        if (file.exists()){
            boolean result = file.delete();
            System.out.println(result);
        }
        System.out.println("生成成功!");
    }

    private static void ReadPdf(String pdfPath, String txtPath) {
        PdfDocument doc = new PdfDocument();

        //        //加载PDF文件
        doc.loadFromFile(pdfPath);

        StringBuilder sb = new StringBuilder();

        PdfPageBase page;

        //遍历PDF页面，获取文本
        for (int i = 0; i < 1; i++) {
            page = doc.getPages().get(i);
            sb.append(page.extractText(true));
        }

        FileWriter writer;

        try {
//将文本写入文本文件
            writer = new FileWriter(txtPath);
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.close();
    }

    private static List<HaiGuanDO> GenerateDate(List<HaiGuanDO> list, List<String> str_list) {
        String GTDNumber = "";
        String GTDQuantity = "";
        String GTDAmount = "";
        String DutyAmount = "";
        String VATAmount = "";
        String DuytAmount1 = "";
        String DuytAmount2 = "";
        HaiGuanDO haiGuanDO = new HaiGuanDO();
        for (String str : str_list) {
            if (str.contains("ДЕКЛАРАЦИЯ НА ТОВАРЫ")) {
                GTDNumber = str.substring(str.lastIndexOf("A") + 1, str.length()).trim();
                haiGuanDO.setGTDNumber(GTDNumber);

//                System.out.println(GTDNumber);
            }

            if (str.contains("5 Всего т-ов 6 Всего мест 7 Справочный номер")) {
                GTDQuantity = str_list.get(str_list.indexOf("5 Всего т-ов 6 Всего мест 7 Справочный номер") + 1);
                GTDQuantity = GTDQuantity.substring(GTDQuantity.indexOf(" ") + 1, GTDQuantity.length());
                haiGuanDO.setGTDQuantity(GTDQuantity);
//                System.out.println(GTDQuantity);
            }
//            System.out.println(str);
            if (str.contains("средства на границе 22 Валюта и общая сумма по счету 23 Курс валюты 24 Характер сделки")) {
                GTDAmount = str_list.get(str_list.indexOf(str) + 1);
                String[] vl = GTDAmount.split(" ");
                List<String> list1 = Arrays.asList(vl);
                for (String str1 : list1) {
                    if (str1.contains(".00")) {
                        GTDAmount = str1;
                    }
                }
                haiGuanDO.setGTDAmount(GTDAmount);
//                System.out.println(GTDAmount);
            }
            if (str.contains("1010-")) {
                DuytAmount1 = str;
                String[] vl1 = DuytAmount1.split("-");
                DuytAmount1 = vl1[1];
//                System.out.println(DuytAmount1);
            }
            if (str.contains("2010-")) {
                DuytAmount2 = str;
                String[] vl1 = DuytAmount2.split("-");
                DuytAmount2 = vl1[1];
                Double sum = Double.parseDouble(DuytAmount1) + Double.parseDouble(DuytAmount2);
                DutyAmount = sum.toString();
                haiGuanDO.setDutyAmount(DutyAmount);

            }
            if (str.contains("5010-")) {
                VATAmount = str;
                String[] vl1 = VATAmount.split("-");
                VATAmount = vl1[1];
//                System.out.println(VATAmount);
                haiGuanDO.setVATAmount(VATAmount);
            }
        }
        if (DuytAmount1.equals("")){
            DuytAmount1 = "0.00";
        }
        if (DuytAmount2.equals("")){
            DuytAmount2 = "0.00";
            Double sum = Double.parseDouble(DuytAmount1) + Double.parseDouble(DuytAmount2);
            haiGuanDO.setDutyAmount(sum.toString());

        }
        list.add(haiGuanDO);

        return list;
    }

    private static List<String> ReadTxt(String txtPath) throws IOException {
        FileReader fileReader = new FileReader(txtPath);
        BufferedReader br = new BufferedReader(fileReader);//构造一个BufferedReader类来读取文件
        String s = null;
        String number = "";
        //多个空格变成单一空格
        String reg = "\\s+";
        Pattern pattern = Pattern.compile(reg);

        List<String> Str_list = new ArrayList<>(); //原始的行数据
        while ((s = br.readLine()) != null) {
            //使用readLine方法，一次读一行
            String str = s;
            Matcher matcher = pattern.matcher(str);
            str = matcher.replaceAll(" ").trim();
            if (str.length() > 0) {
                Str_list.add(str);
            }
        }
        br.close();
        fileReader.close();
        return Str_list;
    }

    /**
     * 导出Excel表格
     *
     * @param txtDataList
     * @param filePath
     */
    public static void excelOutput_Data(List<HaiGuanDO> txtDataList, String filePath) throws IOException {
        //创建表格
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //定义第一个sheet页
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Sheet1");
        //第一个sheet页数据
        Row row0 = hssfSheet.createRow(0);
        String[] headers = new String[]{"GTDNumber", "GTDQuantity", "GTDAmount", "DutyAmount", "VATAmount"};
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = (HSSFCell) row0.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            hssfSheet.setColumnWidth(i, 5000);
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        int rowNum = 1;
        for (HaiGuanDO guanDO : txtDataList) {
            HSSFRow row = hssfSheet.createRow(rowNum);
            hssfSheet.setColumnWidth(rowNum, 5000);
            if (guanDO.getGTDNumber() != null) {
                row.createCell(0).setCellValue(guanDO.getGTDNumber());
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            } else {
                row.createCell(0).setCellValue("");
            }
            if (guanDO.getGTDQuantity() != null) {
                row.createCell(1).setCellValue(guanDO.getGTDQuantity());
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);

            } else {
                row.createCell(1).setCellValue("");
            }
            if (guanDO.getGTDAmount() != null) {
                row.createCell(2).setCellType(Cell.CELL_TYPE_STRING);
//                row.createCell(2).setCellValue(guanDO.getBillDate());
                row.getCell(2).setCellValue(guanDO.getGTDAmount());

            } else {
                row.createCell(2).setCellValue("");
            }
            if (guanDO.getDutyAmount() != null) {
                row.createCell(3).setCellValue(guanDO.getDutyAmount());
                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
            } else {
                row.createCell(3).setCellValue("");
            }
            if (guanDO.getVATAmount() != null) {
                row.createCell(4).setCellValue(Double.parseDouble(guanDO.getVATAmount()));
                row.getCell(4).setCellType(Cell.CELL_TYPE_NUMERIC);
            } else {
                row.createCell(4).setCellValue("");
            }

            rowNum++;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        hssfWorkbook.write(fileOutputStream);

    }
}
