package originalFileOperation;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;

public class excelReader {
    private static Logger logger = Logger.getLogger(excelReader.class.getName()); // 日志打印类
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     * @param inputStream 读取文件的输入流
     * @param fileType 文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    /**
     * 读取Excel文件内容
     * @param fileName 要读取的Excel文件所在路径
     * @return 读取结果列表，读取失败时返回null
     */
    public static List<dataVO> readExcel(String fileName) {

        Workbook workbook = null;
        FileInputStream inputStream = null;
        try {
            // 获取Excel后缀名
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel文件
            File excelFile = new File(fileName);
            if (!excelFile.exists()) {
                logger.warning("指定的Excel文件不存在！");
                return null;
            }
            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);
            // 读取excel中的数据
            List<dataVO> resultDataList = parseExcel(workbook);

            return resultDataList;
        } catch (Exception e) {
            logger.warning("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.warning("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
    }

    /**
     * 解析Excel数据
     * @param workbook Excel工作簿对象
     * @return 解析结果
     */
    private static List<dataVO> parseExcel(Workbook workbook) {
        List<dataVO> resultDataList = new ArrayList<>();
        // 按照sheet个数，依次解析sheet
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }
            // 获取第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
                logger.warning("解析Excel失败，在第一行没有读取到任何数据！");
            }
            // 解析每一行的数据，构造数据对象
            int rowStart = firstRowNum + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (null == row) {
                    continue;
                }
                dataVO resultData = convertRowToData(row);
                /*  if (rowNum == rowStart + 1) {
                    System.out.println(resultData.getProductName());
                    System.out.println(resultData.getProductCode());
                    System.out.println(resultData.getAverageDailySales());
                    System.out.println(resultData.getOnHandInventory());
                    System.out.println(resultData.getSingleSidedshelfVolume());
                }*/
                if (null == resultData) {
                    logger.warning("第 " + row.getRowNum() + "行数据不合法，已忽略！");
                    continue;
                }
                resultDataList.add(resultData);
            }
        }
        return resultDataList;
    }

    /**
     * 提取每一行中需要的数据，构造成为一个结果数据对象
     * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
     * @param row 行数据
     * @return 解析后的行数据对象，行数据错误时返回null
     */
    private static dataVO convertRowToData(Row row) {
        dataVO resultData = new dataVO();
        Cell cell;

        // 获取编码
        cell = row.getCell(0) == null ? row.createCell(0): row.getCell(0);
        String name = checkType(cell);
        resultData.setProductCode(name);
        // 获取品名
        cell = row.getCell(1) == null ? row.createCell(1): row.getCell(1);
        String pCode = checkType(cell);
        resultData.setProductName(pCode);
        // 获取日均销量
        cell = row.getCell(2) == null ? row.createCell(2): row.getCell(2);
        String averDSales = checkType(cell);
        resultData.setAverageDailySales(averDSales);
        // 现有库存量
        cell = row.getCell(3) == null ? row.createCell(3): row.getCell(3);
        String curStock = checkType(cell);
        resultData.setOnHandInventory(curStock);
        // 单品属性
        //cell = row.getCell(4) == null ? row.createCell(4): row.getCell(4);
        if (row.getCell(4) == null) {
            resultData.setItemCategory("*");
        } else {
            cell = row.getCell(4);
            String attribute = checkType(cell);
            resultData.setItemCategory(attribute);
        }
        //单品底部尺寸
        cell = row.getCell(5) == null ? row.createCell(5): row.getCell(5);
        String size = checkType(cell);
        resultData.setBottomSize(size);

        resultData.init();

        return resultData;
    }

    public static String checkType(Cell cell) {
        if (cell == null) {
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                returnValue = "" + cell.getNumericCellValue();

/*                Double doubleValue = cell.getNumericCellValue();
                returnValue = "" + Math.floor(doubleValue);*/
/*                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("##.##");
                returnValue = df.format(doubleValue);*/
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case BLANK:     // 空值
                break;
            case FORMULA:   // 公式
                returnValue = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return returnValue;
    }
}
