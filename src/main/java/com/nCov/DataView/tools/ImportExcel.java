package com.nCov.DataView.tools;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * program: ImportExcel
 * description: Excel的导入工具
 * author: SoCMo
 * create: 2019/12/7 16:22
 */
public class ImportExcel {
    /**
     * @Description: 从Excel表格中获取数据
     * @Param: [in, fileName]
     * @Return: java.util.List<java.util.List < java.lang.Object>>
     * @Author: SoCMo
     * @Date: 2019/12/7
     */
    public static Sheet getBankListByExcel(InputStream in, String fileName) throws IOException, AllException {
        Workbook workbook = workbookVersion(in, fileName);

        //List<List<Object>> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null || !sheet.getSheetName().equals("每日报送信息")) {
            throw new AllException(EmAllException.UNIFY_ERROR);
        }

        return sheet;
    }


    /**
     * @Description: Excel版本识别并转为Workbook
     * @Param: [in, fileName]
     * @Return: org.apache.poi.ss.usermodel.Workbook
     * @Author: SoCMo
     * @Date: 2019/12/7
     */
    public static Workbook workbookVersion(InputStream in, String fileName) throws IOException, AllException {
        Workbook workbook;
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            throw new AllException(EmAllException.BAD_FILE_TYPE);
        }

        String fileType = fileName.substring(index);
        if (fileType.equals(".xls")) {
            workbook = new HSSFWorkbook(in);
        } else if (fileType.equals(".xlsx")) {
            workbook = new XSSFWorkbook(in);
        } else {
            throw new AllException(EmAllException.BAD_FILE_TYPE);
        }
        return workbook;
    }
}
