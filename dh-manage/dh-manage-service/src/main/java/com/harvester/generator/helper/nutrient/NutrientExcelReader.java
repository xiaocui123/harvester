package com.harvester.generator.helper.nutrient;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cui on 2017/11/8.
 */
public class NutrientExcelReader {

    public static List<NutrientDataType> readExcel(File file) {
        List<NutrientDataType> lstNutrient = Lists.newArrayList();
        try {
            FileInputStream excelFile = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            String haiyu = datatypeSheet.getSheetName().substring(6);

            Iterator<Row> iterator = datatypeSheet.iterator();

            //忽略第一行
            iterator.next();
            NutrientDataType nutrient = null;
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();

                MeaturedDataType meaturedData = new MeaturedDataType();
                boolean mainRow = false;
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    int columnIndex = currentCell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            String zhanwei = (String) getCellValue(currentCell);
                            if (!Strings.isNullOrEmpty(zhanwei)) {
                                //将上一站位信息加入列表
                                if (nutrient != null) {
                                    lstNutrient.add(nutrient);
                                }

                                nutrient = new NutrientDataType();
                                nutrient.setZhanwei(zhanwei);
                                nutrient.setHaiyu(haiyu);
                                mainRow = true;
                            } else {
                                mainRow = false;
                            }
                            break;
                        case 1:
                            if (mainRow) {
                                nutrient.setLon((Double) getCellValue(currentCell));
                            }
                            break;
                        case 2:
                            if (mainRow) {
                                nutrient.setLat((Double) getCellValue(currentCell));
                            }
                            break;
                        case 3:
                            if (mainRow) {
                                nutrient.setDate(String.valueOf(getCellValue(currentCell)));
                            }
                            break;
                        case 4:
                            if (mainRow) {
                                nutrient.setTime(String.valueOf(getCellValue(currentCell)));
                            }
                            break;
                        case 5:
                            if (mainRow) {
                                try {
                                    nutrient.setDepth((Double) getCellValue(currentCell));
                                } catch (Exception e) {
                                    nutrient.setDepth(100000);
                                }
                            }
                            break;
                        case 6:
                            meaturedData.setLevel((Double) getCellValue(currentCell));
                            break;
                        case 7:
                            meaturedData.setPo4(getMeaturedData(currentCell));
                            break;
                        case 8:
                            meaturedData.setNo3(getMeaturedData(currentCell));
                            break;
                        case 9:
                            meaturedData.setSio3(getMeaturedData(currentCell));
                            break;
                    }
                }
                nutrient.getLstMeaturedData().add(meaturedData);
            }
            //最后一条
            lstNutrient.add(nutrient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lstNutrient;
    }

    private static double getMeaturedData(Cell currentCell) {
        Object result = getCellValue(currentCell);
        if (result instanceof String) {
            if ("未检出".equals(result)) {
                return -99.9;
            } else if ("数据异常".equals(result)) {
                return -9999.90;
            } else {
                throw new IllegalArgumentException("未定义的数据错误标识【" + result + "】");
            }

        } else {
            return (Double) result;
        }
    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();

            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();

            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //  如果是date类型则 ，获取该cell的date值
                    short dataFormat = cell.getCellStyle().getDataFormat();
                    if (dataFormat == 14 || dataFormat == 31 || dataFormat == 57 || dataFormat == 58) {
                        //日期
                        double value = cell.getNumericCellValue();
                        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                        return new SimpleDateFormat("yyyy-MM-dd").format(date);

                    } else if (dataFormat == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        //时间
                        return new SimpleDateFormat("HH:mm").format(cell.getDateCellValue());
                    } else {
                        //不支持
                        System.out.println(cell.getRow().getRowNum() + ":" + cell.getColumnIndex());
                        throw new IllegalArgumentException("不支持的日期时间格式【" + dataFormat + "】");
                    }
                } else {
                    // 纯数字
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_FORMULA:
                return cell.getNumericCellValue();
        }

        return null;
    }

}
