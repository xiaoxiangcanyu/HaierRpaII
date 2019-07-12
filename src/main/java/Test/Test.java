package Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class Test {
    public static void main(String[] args) throws IOException, InvalidFormatException {
       String filePath = "C:\\Users\\songyu\\Desktop\\HES-PO Claiming-KRW\\0001.jpg";
        File file = new File(filePath);
        System.out.println(file.getName());
    }



}
