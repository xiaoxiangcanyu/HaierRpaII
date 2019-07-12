package ConstructionBank.main;

import Domain.BankDO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructionBankQD {
    public static void main(String[] args) throws IOException, ParseException {
//        String EnterpriseName = args[0];
//        String FilePath = args[1];
//        String ExcelPath =args[2];
        //txt路径
        String FilePath = "C:\\Users\\songyu\\Desktop\\海尔项目二期\\银行对账\\建行\\65A0-建行-8150.txt";
        //生成的excel路径
        String ExcelPath = "C:\\Users\\songyu\\Desktop\\海尔项目二期\\银行对账\\建行\\Result\\" + FilePath.substring(FilePath.lastIndexOf("\\"), FilePath.lastIndexOf(".")) + ".xls";
//           读取txt文件
        List<BankDO> list = ReadTxt(FilePath);

        //生成数据表
            excelOutput_Data(list, ExcelPath);
        System.out.println("生成成功!");


    }

    private static List<BankDO> ReadTxt(String filePath) throws IOException, ParseException {
        List<BankDO> list = new ArrayList<BankDO>();
        File file = new File(filePath);
        String BankAccount = "";
        String BillDate = "";
        String Abstract = "";
        String DebitAmount = "";
        String CreditAmount = "";
        String DocumentNo = "";
        String Currency = "";
        List<String> List_str = new ArrayList<>();//原始的行数据集合
        List<String> List_complete = new ArrayList<>();//行补全之后的数据集合
        //多个空格变成单一空格
        String reg = "\\s+";
        Pattern pattern = Pattern.compile(reg);
        //摘取处理日期和生效日期
        String reg1 = "\\d{2}/\\d{2}/\\d{4}";
        //处理汉字
        String reg2 = "[(\\u4e00-\\u9fa5)]";
        String reg3 = "[(\\u4e00-\\u9fa5)]{1,}";
        Pattern pattern1 = Pattern.compile(reg1);
        //
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            String number = "";

            //读取每一行的文本数据，放到集合中
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
//                创建实体类
                //每一行的内容
                String str = s;
                //先将每一行的多个空格变成单一空格
                Matcher matcher = pattern.matcher(str);
                str = matcher.replaceAll(" ").trim();
//                System.out.println(str);
                List_str.add(str);
            }
            //

        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }

    /**
     * 导出Excel表格
     *
     * @param txtDataList
     * @param filePath
     */
    public static void excelOutput_Data(List<BankDO> txtDataList, String filePath) {
        //创建表格
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //定义第一个sheet页
        XSSFSheet xssfSheet = xssfWorkbook.createSheet("Sheet1");
        //第一个sheet页数据
        Row row0 = xssfSheet.createRow(0);
        String[] headers = new String[]{"企业名称", "银行账号", "单据日期", "摘要", "借方金额", "贷方金额", "币种"};
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = (XSSFCell) row0.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            xssfSheet.setColumnWidth(i, 5000);
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (BankDO bankDO : txtDataList) {
            if (bankDO.getCreditAmount() != null) {
                bankDO.setCreditAmount(decimalFormat.format(Double.parseDouble(bankDO.getCreditAmount())));
            }
            if (bankDO.getDebitAmount() != null) {
                bankDO.setDebitAmount(decimalFormat.format(Double.parseDouble(bankDO.getDebitAmount())));
            }
        }
        //第一个Sheet页导出数据
        int rowNum = 1;
        for (BankDO bankDO : txtDataList) {
            XSSFRow row = xssfSheet.createRow(rowNum);
            xssfSheet.setColumnWidth(rowNum, 5000);
            if (bankDO.getEnterpriseName() != null) {
                row.createCell(0).setCellValue(bankDO.getEnterpriseName());
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            } else {
                row.createCell(0).setCellValue("");
            }
            if (bankDO.getBankAccount() != null) {
                row.createCell(1).setCellValue(bankDO.getBankAccount());
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);

            } else {
                row.createCell(1).setCellValue("");
            }
            if (bankDO.getBillDate() != null) {
                row.createCell(2).setCellType(Cell.CELL_TYPE_STRING);
//                row.createCell(2).setCellValue(bankDO.getBillDate());
                row.getCell(2).setCellValue(bankDO.getBillDate());

            } else {
                row.createCell(2).setCellValue("");
            }
            if (bankDO.getAbstract() != null) {
                row.createCell(3).setCellValue(bankDO.getAbstract());
                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);

            } else {
                row.createCell(3).setCellValue("");
            }
            if (bankDO.getDebitAmount() != null) {
                row.createCell(4).setCellValue(Double.parseDouble(bankDO.getDebitAmount()));
                row.getCell(4).setCellType(Cell.CELL_TYPE_NUMERIC);
            } else {
                row.createCell(4).setCellValue("");
            }
            if (bankDO.getCreditAmount() != null) {
                row.createCell(5).setCellValue(Double.parseDouble(bankDO.getCreditAmount()));
                row.getCell(5).setCellType(Cell.CELL_TYPE_NUMERIC);
            } else {
                row.createCell(5).setCellValue("");
            }
            if (bankDO.getCurrency() != null) {
                row.createCell(6).setCellValue(bankDO.getCurrency());
                row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
            } else {
                row.createCell(5).setCellValue("");
            }
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
