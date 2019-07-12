package ChinaBank.main;

import Domain.BankDO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChinaBank {
    public static void main(String[] args) throws IOException {
//        String EnterpriseName = args[0];
//        String FilePath = args[1];
//        String ExcelPath =args[2];
//        公司名称
        String EnterpriseName = "";
        //txt路径
        String FilePath = "C:\\Users\\songyu\\Desktop\\海尔项目二期\\银行对账\\中银香港\\Test\\";
        File file = new File(FilePath);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.getName().endsWith(".txt")) {
                FilePath = file1.getAbsolutePath();
                //生成的excel路径
                System.out.println(FilePath);
                String ExcelPath = "C:\\Users\\songyu\\Desktop\\海尔项目二期\\银行对账\\中银香港\\Test\\Result\\" + FilePath.substring(FilePath.lastIndexOf("\\"), FilePath.lastIndexOf(".")) + ".xls";
//           读取txt文件
                List<BankDO> list = ReadTxt(FilePath);
                for (BankDO bankDO : list) {
                    bankDO.setEnterpriseName(EnterpriseName);
                }
                //生成数据表
                excelOutput_Data(list, ExcelPath);
            }
        }

    }

    private static List<BankDO> ReadTxt(String filePath) throws IOException {
        List<BankDO> list = new ArrayList<BankDO>();
        File file = new File(filePath);
        String BankAccount = "";
        String BillDate = "";
        String Abstract = "";
        String DebitAmount = "";
        String CreditAmount = "";
        String DocumentNo = "";
        String Currency = "";
        //
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            String number = "";
            //多个空格变成单一空格
            String reg = "\\s+";
            Pattern pattern = Pattern.compile(reg);
            //摘取处理日期和生效日期
            String reg1 = "\\d{4}/\\d{2}/\\d{2}";
            //处理汉字
            String reg2 = "[(\\u4e00-\\u9fa5)]";
            String reg3 = "[(\\u4e00-\\u9fa5)]{1,}";
            List<String> List_str = new ArrayList<>();
            Pattern pattern1 = Pattern.compile(reg1);
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
//                创建实体类
                //每一行的内容
                String str = s;
                //先将每一行的多个空格变成单一空格
                Matcher matcher = pattern.matcher(str);
                str = matcher.replaceAll(" ").trim();
//                //获取账号--------------------------------------------------------
                if (str.contains("账号")) {
                    BankAccount = str.substring(str.indexOf("账号") + 2).trim();
                    BankAccount = BankAccount.replace("-", "");
//                    System.out.println(BankAccount);
                }
                //获取币种-----------------------------------------
                if (str.contains("货币")) {
                    String[] vl = str.split(" ");
                    Currency = vl[3];
//                    System.out.println(Currency);
                }
                List_str.add(str);
            }
//------------------------------------------------------------------------------------------------------------------------------------------------------------
            for (String str : List_str) {
                BankDO bankDO = new BankDO();
                bankDO.setDebitAmount("0");
                bankDO.setCreditAmount("0");
//                开始进行数据获取-----------------------------------------------------------------------------
//                统计空格数量
                int count = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == ' ') {
                        count++;
                    }
                }
                //            //获取借款金额和贷款金额---------------------------------------------------
                if (str.contains("扣账") || str.contains("入账")) {
                    String[] vl = str.split(" ");
                    if (vl.length > 4) {
                        if (!vl[4].matches(reg3)) {
//                            System.out.println(vl[4]);
                            //判断是借款还是贷款
                            if (str.contains("扣账")) {
                                DebitAmount = vl[4];
                                DebitAmount = DebitAmount.replace(",", "");
                                bankDO.setDebitAmount(DebitAmount);
//                                System.out.println("借款:"+DebitAmount);
                            } else if (str.contains("入账")) {
                                CreditAmount = vl[4];
                                CreditAmount = CreditAmount.replace(",", "");
                                bankDO.setCreditAmount(CreditAmount);
//                                System.out.println("贷款:"+CreditAmount);

                            }
                        }
                    }

                }
                //获取借款金额和贷款金额---------------------------------------------------
                //获取单据日期和交易摘要--------------------------------------------------------
                if (str.contains("扣账") || str.contains("入账")) {
//                    单据日期--------------------------------------------------------------------
                    Matcher matcher1 = pattern1.matcher(str);
                    while (matcher1.find()) {
                        BillDate = matcher1.group();
                    }
                    if (!"".equals(BillDate)) {
//                        System.out.println(BillDate);
//                        System.out.println(str);
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-mm-dd");
                            Date DateTemp = simpleDateFormat.parse(BillDate);
                            BillDate = simpleDateFormat1.format(DateTemp);
                            bankDO.setBillDate(BillDate);
//                        System.out.println("单据日期:"+BillDate);
                        } catch (Exception e) {
                            continue;
                        }

                    }
                    //单据日期-------------------------------------------------------------------
//交易摘要--------------------------------------------------------------------------------------------------------------------------------
                    if (count >= 7 && str.startsWith("2")) {
                        StringBuilder str3 = new StringBuilder(str);

                        for (String s1 : List_str) {
                            if (s1.contains(str3)) {
                                int index = List_str.indexOf(s1);
                                Set<String> set = new HashSet<>();
                                for (int i = 0; i < 3; i++) {
                                    if (!List_str.get(index + i).startsWith("2")) {
                                        set.add(List_str.get(index + i));
                                    }
                                }
                                for (String s2 : set) {
                                    str3.append(s2);
                                }
                            }
//
                        }
                        for (int i = 0; i < 7; i++) {
                            str3 = new StringBuilder(str3.substring(str3.indexOf(" ") + 1));
                        }
//                    System.out.println(str3.toString());
                        if (!"".equals(str3.toString())) {
                            Abstract = str3.toString();
                            bankDO.setAbstract(Abstract);
                        }
                    }
//交易摘要--------------------------------------------------------------------------------------------------------------------------------
                }
//                补全账号和币种
                for (BankDO bankDO1 : list) {
                    bankDO1.setBankAccount(BankAccount);
                    bankDO1.setCurrency(Currency);
                }

                //获取账号--------------------------------------------------------

                if (bankDO.getBillDate() != null) {
                    list.add(bankDO);
                }
            }
//            for (BankDO chinaBankDO1 : list) {
//                System.out.println("银行账号:" + chinaBankDO1.getBankAccount() + ",单据日期:" + chinaBankDO1.getBillDate() + ",摘要:" + chinaBankDO1.getAbstract() + ",借方金额:" + chinaBankDO1.getCreditAmount() + ",贷方金额:" + chinaBankDO1.getDebitAmount() + ",币种:" + chinaBankDO1.getCurrency());
//            }

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
    public static void excelOutput_Data(List<BankDO> txtDataList, String filePath) throws IOException {
        //创建表格
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //定义第一个sheet页
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Sheet1");
        //第一个sheet页数据
        Row row0 = hssfSheet.createRow(0);
        String[] headers = new String[]{"企业名称", "银行账号", "单据日期", "摘要", "借方金额", "贷方金额", "单据号", "币种", "参考代码3"};
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = (HSSFCell) row0.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            hssfSheet.setColumnWidth(i, 5000);
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
        Row row1 = hssfSheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = (HSSFCell) row1.createCell(i);
            cell.setCellValue("");
        }
        int rowNum = 2;
        for (BankDO bankDO : txtDataList) {
            HSSFRow row = hssfSheet.createRow(rowNum);
            hssfSheet.setColumnWidth(rowNum, 5000);
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
                System.out.println(bankDO.getDebitAmount());
                System.out.println(Double.parseDouble(bankDO.getDebitAmount()));
                row.createCell(4).setCellValue(Double.parseDouble(bankDO.getDebitAmount()));
                row.getCell(4).setCellType(Cell.CELL_TYPE_NUMERIC);
            } else {
                row.createCell(4).setCellValue("");
            }
            if (bankDO.getCreditAmount() != null) {
                System.out.println(bankDO.getCreditAmount());
                System.out.println(Double.parseDouble(bankDO.getCreditAmount()));
                row.createCell(5).setCellValue(Double.parseDouble(bankDO.getCreditAmount()));
                row.getCell(5).setCellType(Cell.CELL_TYPE_NUMERIC);
            } else {
                row.createCell(5).setCellValue("");
            }
            row.createCell(6).setCellValue("");
            if (bankDO.getCurrency() != null) {
                row.createCell(7).setCellValue(bankDO.getCurrency());
                row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
            } else {
                row.createCell(7).setCellValue("");
            }
            row.createCell(8).setCellValue("");
            rowNum++;
        }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

    }
}
