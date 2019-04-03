package com.ericsson.example.excel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class ExcelMockData {

    private List<ExcelModel> excelData;

    public ExcelMockData() {
    }

    public List<ExcelModel> getExcelData() {
        if (excelData == null) {
            populateExcelData();
        }
        return excelData;
    }

    public void setExcelData(List<ExcelModel> excelData) {
        this.excelData = excelData;
    }

    private void populateExcelData() {
        excelData = new ArrayList<>();
        Class<ExcelModel> classz = (Class<ExcelModel>) ExcelModel.class;
        Method[] methods = classz.getMethods();
        for (int i = 0; i < 20000; i++) {
            ExcelModel model = new ExcelModel();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith("set")) {
                    try {
                        method.invoke(model, getRandomString());
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            excelData.add(model);
        }
    }

    private String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

}
