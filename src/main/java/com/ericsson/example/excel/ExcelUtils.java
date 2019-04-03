package com.ericsson.example.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelUtils {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws FileNotFoundException, InvalidFormatException {
        long startTime = System.currentTimeMillis();
        String filePath = "C:\\Users\\elnghxn\\111.xlsx";
        SXSSFWorkbook sxssfWorkbook = null;
        BufferedOutputStream outputStream = null;
        try {
            //这样表示SXSSFWorkbook只会保留100条数据在内存中，其它的数据都会写到磁盘里，这样的话占用的内存就会很少
            sxssfWorkbook = new SXSSFWorkbook(100);
            //获取第一个Sheet页
            SXSSFSheet sheet = sxssfWorkbook.createSheet("sheet1");
            for (int i = 0; i < 50; i++) {
                for (int z = 0; z < 10000; z++) {
                    SXSSFRow row = sheet.createRow(i * 10000 + z);
                    for (int j = 0; j < 10; j++) {
                        row.createCell(j).setCellValue("你好：" + j);
                    }
                }
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
            sxssfWorkbook.write(outputStream);
            outputStream.flush();
            sxssfWorkbook.dispose();// 释放workbook所占用的所有windows资源
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sxssfWorkbook != null) {
                try {
                    sxssfWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("输出完毕。。。。");
            long endTime = System.currentTimeMillis();
            System.out.println(endTime - startTime);
        }

    }


    /**
     * 这种方式效率比较低并且特别占用内存，数据量越大越明显
     *
     * @param args
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */
    public static void xsswork(String[] args) throws FileNotFoundException, InvalidFormatException {
        long startTime = System.currentTimeMillis();
        BufferedOutputStream outPutStream = null;
        XSSFWorkbook workbook = null;
        FileInputStream inputStream = null;
        String filePath = "E:\\txt\\666.xlsx";
        try {
            workbook = new XSSFWorkbook();
            workbook.createSheet("测试");
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < 50; i++) {
                for (int z = 0; z < 10000; z++) {
                    XSSFRow row = sheet.createRow(i * 10000 + z);
                    for (int j = 0; j < 10; j++) {
                        row.createCell(j).setCellValue("你好：" + j);
                    }
                }
            }
            outPutStream = new BufferedOutputStream(new FileOutputStream(filePath));
            workbook.write(outPutStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outPutStream != null) {
                try {
                    outPutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

    /**
     * 先创建一个XSSFWorkbook对象
     *
     * @param filePath
     * @return
     */
    public static XSSFWorkbook getWorkBook(String filePath) {
        XSSFWorkbook workbook = null;
        try {
            File fileXlsxPath = new File(filePath);
            BufferedOutputStream outPutStream = new BufferedOutputStream(new FileOutputStream(fileXlsxPath));
            workbook = new XSSFWorkbook();
            workbook.createSheet("测试");
            workbook.write(outPutStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

}
