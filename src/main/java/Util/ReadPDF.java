package Util;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReadPDF {

    public static void main(String[] args) {
//创建PdfDocument实例
        PdfDocument doc = new PdfDocument();

        //遍历文件夹里面的pdf文件
        File file = new File("C:\\Users\\songyu\\Desktop\\20190620\\");
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.getName().endsWith(".PDF") || file1.getName().endsWith(".pdf")) {
                String filePath = file1.getAbsolutePath();
                String fileName = file1.getName();
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".txt";
                System.out.println("绝对路径:" + filePath);
                System.out.println("文件名:" + fileName);
                //        //加载PDF文件
                doc.loadFromFile(filePath);

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
                    writer = new FileWriter("C:\\Users\\songyu\\Desktop\\20190620\\ouput\\"+fileName);
                    writer.write(sb.toString());
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                doc.close();
            }

        }

//    }
    }
}