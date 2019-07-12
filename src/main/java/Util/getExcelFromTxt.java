package Util;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getExcelFromTxt{
    public static void main(String[] args) throws IOException {
        String TxtPath = "C:\\Users\\songyu\\Desktop\\haier_rpa所有资料\\OCR_Data\\海关单\\GTD_10216170_080419_0058575.pdf";
        ReadTxt(TxtPath);
    }

    private static void ReadTxt(String txtPath) throws IOException {
        File file = new File(txtPath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//        读取每一行
        String str = "";
        String reg = "(\\s+)";
        Pattern pattern = Pattern.compile(reg);

        while ((str=bufferedReader.readLine())!= null){
            Matcher matcher = pattern.matcher(str);
//            System.out.println(matcher.groupCount());
//            str = matcher.replaceAll(" ");
            if (matcher.find()){
                System.out.println(matcher.group(0));
            }
//            System.out.println(str);
        }
    }
}
