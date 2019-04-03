package com.ericsson.example.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelWriter {

    public static void main(String[] args) {

        ExcelMockData mockData = new ExcelMockData();
        List<ExcelModel> excelData = mockData.getExcelData();

        ExcelWriter writer = new ExcelWriter();

        //using auto flush mode
        // writer.writeToExcelAutoFlush(excelData);

        //using manual flush mode
        writer.writeToExcelManualFlush(excelData);

    }

    /**
     * 写一个窗口为100行的工作表(rowAccessWindowSize = 100)。当行数达到101时，rownum=0的行被刷新到磁盘并从内存中删除，
     * 当rownum达到102时，rownum=1的行被刷新，以此类推。
     * @param excelModels
     */
    // using auto flush and default window size 100
    public void writeToExcelAutoFlush(List<ExcelModel> excelModels) {
        SXSSFWorkbook wb = null;
        FileOutputStream fos = null;
        try {
            // keep 100 rows in memory, exceeding rows will be flushed to disk
            wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE);
            Sheet sh = wb.createSheet();

            @SuppressWarnings("unchecked")
            Class<ExcelModel> classz = (Class<ExcelModel>) excelModels.get(0).getClass();

            Field[] fields = classz.getDeclaredFields();
            int noOfFields = fields.length;

            int rownum = 0;
            Row row = sh.createRow(rownum);
            for (int i = 0; i < noOfFields; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(fields[i].getName());
            }

            for (ExcelModel excelModel : excelModels) {
                row = sh.createRow(rownum + 1);
                int colnum = 0;
                for (Field field : fields) {
                    String fieldName = field.getName();
                    Cell cell = row.createCell(colnum);
                    Method method = null;
                    try {
                        method = classz.getMethod("get" + this.capitalizeInitialLetter(fieldName));
                    } catch (NoSuchMethodException nme) {
                        method = classz.getMethod("get" + fieldName);
                    }
                    Object value = method.invoke(excelModel, (Object[]) null);
                    cell.setCellValue((String) value);
                    colnum++;
                }
                rownum++;
            }
            fos = new FileOutputStream("sxssf.xlsx");
            wb.write(fos);
        } catch (Exception ex) {

        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 关闭自动刷新(windowSize=-1)，代码手动控制如何将部分数据写入磁盘
     * @param excelModels
     */
    // using manual flush and default window size 100
    public void writeToExcelManualFlush(List<ExcelModel> excelModels) {
        SXSSFWorkbook wb = null;
        FileOutputStream fos = null;
        try {
            // turn off auto-flushing and accumulate all rows in memory
            wb = new SXSSFWorkbook(-1);
            Sheet sh = wb.createSheet();

            Class<ExcelModel> classz = (Class<ExcelModel>) excelModels.get(0).getClass();

            Field[] fields = classz.getDeclaredFields();

            int rownum = 0;
            Row row = sh.createRow(rownum);
            for (int i = 0; i <  fields.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(fields[i].getName());
            }

            for (ExcelModel excelModel : excelModels) {
                row = sh.createRow(rownum + 1);
                int colnum = 0;
                for (Field field : fields) {
                    String fieldName = field.getName();
                    Cell cell = row.createCell(colnum);
                    Method method = null;
                    try {
                        method = classz.getMethod("get" + capitalizeInitialLetter(fieldName));
                    } catch (NoSuchMethodException nme) {
                        method = classz.getMethod("get" + fieldName);
                    }
                    Object value = method.invoke(excelModel, (Object[]) null);
                    cell.setCellValue((String) value);
                    colnum++;
                }
                // manually control how rows are flushed to disk
                if (rownum % 100 == 0) {
                    // retain 100 last rows and flush all others
                    ((SXSSFSheet) sh).flushRows(100);
                }
                rownum++;
            }
            fos = new FileOutputStream("sxssf.xlsx");
            wb.write(fos);
        } catch (Exception ex) {

        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 将字段名的第一个字母大写
     * @param s
     * @return
     */
    public static String capitalizeInitialLetter(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
