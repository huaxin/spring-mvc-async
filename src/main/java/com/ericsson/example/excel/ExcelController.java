package com.ericsson.example.excel;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static java.lang.System.in;

@Controller
public class ExcelController {

    @RequestMapping(value = "/downloadXlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    byte[] download() throws IOException, InvalidFormatException {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (int i = 0; i < 60000; i++) {
            Row newRow = sheet.createRow(i);
            for (int j = 0; j < 100; j++) {
                newRow.createCell(j).setCellValue("test" + Math.random());
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        byte[] bytes = os.toByteArray();
        return bytes;
    }

    @ResponseBody
    @GetMapping(path = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(@RequestParam(name="name", required=true) String name,
                           HttpServletRequest request, HttpServletResponse response) {

        try {
            String filePath = name;// 如 E:/test.docx
            response.setContentType("text/plain");//纯文本

            String fileName = "";
            int pos = 0;
            if( (pos = filePath.lastIndexOf("/")) > 0 ) {
                fileName = filePath.substring(pos + 1, filePath.length());//截取文件名字
            } else { fileName = filePath;} //没有父文件夹，如"1.txt"

            //设置响应头
            //适应中文文件名
            response.setHeader("Content-disposition","attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            // Assume file name is retrieved from database
            // For example D:\\file\\test.pdf
            File my_file = new File(filePath);
            System.out.println("要下载文件:" + my_file);
            if( !my_file.exists()){
                throw new Exception("文件不存在");
            }

            // This should send the file to browser
            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(my_file);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            in.close();
            out.flush();
            System.out.println("下载成功: " + name );

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            response.reset();
            IOUtils.closeQuietly(in);
        }
    }
}
