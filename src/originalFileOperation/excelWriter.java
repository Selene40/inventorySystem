package originalFileOperation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.*;

public class excelWriter {
    /**
     * 生成Excel并写入数据信息
     * @param dataList 数据列表
     * @return 写入数据后的工作簿对象
     */
    public static Workbook exportData(List<dataVO> dataList){
        // 生成xlsx的Excel
        Workbook workbook = new SXSSFWorkbook();

        // 如需生成xls的Excel，请使用下面的工作簿对象，注意后续输出时文件后缀名也需更改为xls
        //Workbook workbook = new HSSFWorkbook();

        // 生成Sheet表，写入第一行的列头
        List<String> cellHeadOne = new ArrayList<>();
        cellHeadOne.add("商品编码");
        cellHeadOne.add("品名");
        cellHeadOne.add("单品货架排面数");
        Sheet sheetOne = buildDataSheet(workbook, cellHeadOne);
        workbook.setSheetName(0, "单品货架排面数报表");
        //构建每行的数据内容
        int rowNum = 1;
        for (Iterator<dataVO> it = dataList.iterator(); it.hasNext(); ) {
            dataVO data = it.next();
            if (data == null) {
                continue;
            }
            //输出行数据
            Row row = sheetOne.createRow(rowNum++);
            convertDataToRowOne(data, row);
        }

        // 生成Sheet表，写入第一行的列头
        List<String> cellHeadFive = new ArrayList<>();
        cellHeadFive.add("商品编码");
        cellHeadFive.add("品名");
        cellHeadFive.add("单品货架纵列数");
        Sheet sheetFive = buildDataSheet(workbook, cellHeadFive);
        workbook.setSheetName(1, "单品货架纵列数报表");
        //构建每行的数据内容
        int rowNum5 = 1;
        for (Iterator<dataVO> it = dataList.iterator(); it.hasNext(); ) {
            dataVO data = it.next();
            if (data == null) {
                continue;
            }
            //输出行数据
            Row row = sheetFive.createRow(rowNum5++);
            convertDataToRowFive(data, row);
        }

        // 生成Sheet表，写入第一行的列头
        List<String> cellHeadTwo = new ArrayList<>();
        cellHeadTwo.add("商品编码");
        cellHeadTwo.add("品名");
        cellHeadTwo.add("超过库存上限");
        cellHeadTwo.add("超过库存上限预警");
        Sheet sheetTwo = buildDataSheet(workbook, cellHeadTwo);
        workbook.setSheetName(2, "退仓量报表");
        //构建每行的数据内容
        int rowNum2 = 1;
        for (Iterator<dataVO> it = dataList.iterator(); it.hasNext(); ) {
            dataVO data = it.next();
            if (data == null) {
                continue;
            }
            // 是否超过库存上限预警
            if (data.getExceedStatus() == null) continue;
            //输出行数据
            Row row = sheetTwo.createRow(rowNum2++);
            convertDataToRowTwo(data, row);
        }

        // 生成Sheet表，写入第一行的列头
        List<String> cellHeadThree = new ArrayList<>();
        cellHeadThree.add("商品编码");
        cellHeadThree.add("品名");
        cellHeadThree.add("低于库存下限补货量");
        cellHeadThree.add("低于库存下限补货");
        Sheet sheetThree = buildDataSheet(workbook, cellHeadThree);
        workbook.setSheetName(3, "续订量报表");
        //构建每行的数据内容
        int rowNum3 = 1;
        for (Iterator<dataVO> it = dataList.iterator(); it.hasNext(); ) {
            dataVO data = it.next();
            if (data == null) {
                continue;
            }
            // 是否低于库存下限补货
            if (data.getBelowStatus() == null) continue;
            //输出行数据
            Row row = sheetThree.createRow(rowNum3++);
            convertDataToRowThree(data, row);
        }

        // 生成Sheet表，写入第一行的列头
        List<String> cellHeadFour = new ArrayList<>();
        cellHeadFour.add("商品编码");
        cellHeadFour.add("品名");
        cellHeadFour.add("堆头端架陈列量");
        Sheet sheetFour = buildDataSheet(workbook, cellHeadFour);
        workbook.setSheetName(4, "堆头端架陈列量报表");
        //构建每行的数据内容
        int rowNum4 = 1;
        for (Iterator<dataVO> it = dataList.iterator(); it.hasNext(); ) {
            dataVO data = it.next();
            if (data == null) {
                continue;
            }
            // 是否合理库存
            if (Double.valueOf(data.getAverageDailySales()) >= 2 && data.validInventory()) {
                //输出行数据
                Row row = sheetFour.createRow(rowNum4++);
                convertDataToRowFour(data, row);
            }
        }

        return workbook;
    }

    /**
     * 生成sheet表，并写入第一行数据（列头）
     * @param workbook 工作簿对象
     * @return 已经写入列头的Sheet
     */
    private static Sheet buildDataSheet(Workbook workbook, List<String> CELL_HEADS) {
        Sheet sheet = workbook.createSheet();
        // 设置列头宽度
        for (int i=0; i<CELL_HEADS.size(); i++) {
            sheet.setColumnWidth(i, 4000);
        }
        // 设置默认行高
        sheet.setDefaultRowHeight((short) 400);
        // 构建头单元格样式
        CellStyle cellStyle = buildHeadCellStyle(sheet.getWorkbook());
        // 写入第一行各列的数据
        Row head = sheet.createRow(0);
        for (int i = 0; i < CELL_HEADS.size(); i++) {
            Cell cell = head.createCell(i);
            cell.setCellValue(CELL_HEADS.get(i));
            cell.setCellStyle(cellStyle);
        }
        return sheet;
    }

    /**
     * 设置第一行列头的样式
     * @param workbook 工作簿对象
     * @return 单元格样式对象
     */
    private static CellStyle buildHeadCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //对齐方式设置
        style.setAlignment(HorizontalAlignment.CENTER);
        //边框颜色和宽度设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 下边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边框
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边框
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边框
        //设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //粗体字设置
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 单品货架排面数报表
     * @param data 源数据
     * @param row 行对象
     * @return
     */
    private static void convertDataToRowOne(dataVO data, Row row){
        int cellNum = 0;
        Cell cell;
        // 商品编码
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductCode() ? "" : data.getProductCode());
        // 品名
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductName() ? "" : data.getProductName());
        // 单品货架排面数
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getShelfCount() ? "" : data.getShelfCount());
    }

    /**
     * 单品货架纵列数报表
     * @param data 源数据
     * @param row 行对象
     * @return
     */
    private static void convertDataToRowFive(dataVO data, Row row){
        int cellNum = 0;
        Cell cell;
        // 商品编码
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductCode() ? "" : data.getProductCode());
        // 品名
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductName() ? "" : data.getProductName());
        // 单品货架纵列数
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getSingleSidedshelfVolume() ? "" : data.getSingleSidedshelfVolume());
    }

    /**
     * 库存量上限计算退仓量
     * @param data 源数据
     * @param row 行对象
     * @return
     */
    private static void convertDataToRowTwo(dataVO data, Row row){
        int cellNum = 0;
        Cell cell;
        // 商品编码
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductCode() ? "" : data.getProductCode());
        // 品名
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductName() ? "" : data.getProductName());
        // 超过库存上限量
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getWithdrawal() ? "" : data.getWithdrawal());
        // 超过库存上限预警
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getExceedStatus() ? "" : data.getExceedStatus());
    }

    /**
     * 库存量下限计算续订量
     * @param data 源数据
     * @param row 行对象
     * @return
     */
    private static void convertDataToRowThree(dataVO data, Row row){
        int cellNum = 0;
        Cell cell;
        // 商品编码
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductCode() ? "" : data.getProductCode());
        // 品名
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductName() ? "" : data.getProductName());
        // 低于库存下限补货量
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getRenewal() ? "" : data.getRenewal());
        // 低于库存下限补货
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getBelowStatus() ? "" : data.getBelowStatus());
    }

    /**
     * 堆头端架陈列量
     * @param data 源数据
     * @param row 行对象
     * @return
     */
    private static void convertDataToRowFour(dataVO data, Row row){
        int cellNum = 0;
        Cell cell;
        // 商品编码
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductCode() ? "" : data.getProductCode());
        // 品名
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductName() ? "" : data.getProductName());
        // 堆头端架陈列量
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getAmountOnDisplay() ? "" : data.getAmountOnDisplay());
    }
}
