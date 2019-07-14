package GeneralTemplate;

import GeneralTemplate.Domain.OCRDataDO;
import GeneralTemplate.Util.OCRBaseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static Korean.Util.BaseUtil.ocrImageFile;

/**
 * 通用OCR识别模板
 */
public class OCRGeneralTemplate extends OCRBaseUtil{
    public static void main(String[] args) throws Exception {
        String PicPath = "";//图片路径
        String SAPPath = "";//生成的SAP路径
        String JSONTemplate = "";//JSON模板
        //根据OCR的图片和JSON模板获取JSON数据
        List<OCRDataDO> OCRList = GetDataFromPic(PicPath, JSONTemplate);
        //生成SAP
        ExportSAPExcel(OCRList, SAPPath);
    }









}
