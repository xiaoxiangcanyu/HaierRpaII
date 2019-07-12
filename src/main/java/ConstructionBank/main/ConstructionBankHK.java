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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructionBankHK {
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
        //读取完毕----------------------------------------------------------------------------------------------------
        //进行摘要补全--------------------------------------------------------------------------------------------------
        for (String str : List_str) {
            String row1_abstract = "";
            String row2_abstract = "";
            String row3_abstract = "";

//            通过MM来获取摘要的第一行
            if (str.contains("美元")) {
                //将另外3行
//                第一行
                for (String str1 : List_str) {
                    if (str1.contains(str)) {
                        String row1 = List_str.get(List_str.indexOf(str1) - 2);
                        if (row1.length() > 0 && row1.contains(" ")) {
                            row1_abstract = row1.substring(row1.indexOf(" "));
                        }
//                        System.out.println(row1_abstract);

                    }
                }
//                第二行
                for (String str1 : List_str) {
                    if (str1.contains(str)) {
                        String row2 = List_str.get(List_str.indexOf(str1) - 1);
                        if (row2.length() > 0 && row2.contains(" ")) {
                            row2_abstract = "-" + row2.substring(row2.lastIndexOf(" ")).trim();
                        }
//                        System.out.println(row2_abstract);

                    }
                }
//                第三行
                for (String str1 : List_str) {
                    if (str1.contains(str)) {
                        String row3 = List_str.get(List_str.indexOf(str1) + 1);
//                        System.out.println(row3);
                        if (row3.length() > 0 && row3.contains(" ")) {
                            row3_abstract = row3.substring(row3.lastIndexOf(" ")).trim();
                        }
//                        System.out.println(row3_abstract);

                    }
                }
//                System.out.println(str);
                Matcher matcher = pattern1.matcher(str);
                while (matcher.find()) {
                    BillDate = matcher.group();
                    //把时间截出来
                    String str_abstract = str.substring(str.indexOf(BillDate) + 10);
                    str = str.substring(0, str.indexOf(BillDate) + 10);
                    String hour = "";
                    if (str.contains(":")) {
                        hour = str.substring(str.indexOf(":") - 2, str.lastIndexOf(":") + 3);
                        str = str.replaceAll(hour, " ");
                    } else {
                        hour = "";
                    }
                    //截出金额
                    StringBuilder stringBuilder = new StringBuilder(str);
                    stringBuilder.insert(str.indexOf(".") + 3, " ");
                    str = stringBuilder.toString();
                    String str1 = str.substring(0, str.indexOf(".") + 3);
                    String str2 = str.substring(str.indexOf(".") + 3);
                    StringBuilder stringBuilder1 = new StringBuilder(str2);
                    stringBuilder1.insert(str2.indexOf(".") + 3, " ");
                    str2 = stringBuilder1.toString();
                    String ab_com = row1_abstract + row2_abstract + str_abstract + row3_abstract;
                    ab_com = ab_com.replace(" ","");
                    str = str1 + str2 +" "+ab_com ;

                    if (str.indexOf(" ") != 12) {
                        String s1 = str.substring(0, 12);
                        String s2 = str.substring(20);
                        str = s1 + " " + s2;
                    }
                    Matcher matcher1 =pattern.matcher(str);
                    str = matcher1.replaceAll(" ");
//                    System.out.println(str);
                    if (str.charAt(str.lastIndexOf(".")+3) == '美'){
                        StringBuilder stringBuilder2 = new StringBuilder(str);
                        stringBuilder2.insert(str.indexOf("美")," ");
                        str =stringBuilder2.toString();
                    }
                    if (String.valueOf(str.charAt(str.indexOf("元")+2)).matches("\\d")){
                        StringBuilder stringBuilder2 = new StringBuilder(str);
                        stringBuilder2.insert((str.indexOf("元")+2),"1 ");
                        str = stringBuilder2.toString();
//                        System.out.println(str);
                    }
                    List_complete.add(str);
                }
            }
        }
        //进行摘要补全--------------------------------------------------------------------------------------------------
        for (String str:List_complete){
            BankDO bankDO = new BankDO();
            Matcher matcher =pattern.matcher(str);
            str = matcher.replaceAll(" ");
//            System.out.println(str);
            String[] vl = str.split(" ");
//            System.out.println(Arrays.toString(vl));
            BankAccount = vl[0];
            DebitAmount = vl[1];
            CreditAmount =vl[2];
            Currency = vl[4];
            if (vl.length==8){
                Abstract = vl[7];
            }else {
                Abstract ="";
            }

            CreditAmount = CreditAmount.replace(",","");
            DebitAmount = DebitAmount.replace(",","");
//            System.out.println(BankAccount);
//            System.out.println(DebitAmount);

            bankDO.setBankAccount(BankAccount);
            bankDO.setDebitAmount(DebitAmount);
            bankDO.setCreditAmount(CreditAmount);
            bankDO.setCurrency(Currency);
            BillDate = vl[6];
            if (BillDate.length()>0){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-mm-dd");
                Date date = simpleDateFormat.parse(BillDate);
                BillDate = simpleDateFormat1.format(date);
            }

            bankDO.setBillDate(BillDate);
            if (!Abstract.equals("LIMITED")){
                bankDO.setAbstract(Abstract);
            }else {
                bankDO.setAbstract("");
            }
            list.add(bankDO);
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
