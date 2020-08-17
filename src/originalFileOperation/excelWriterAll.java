package originalFileOperation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import java.util.*;

public class excelWriterAll {

    /**
     * 生成Excel并写入数据信息
     * @param dataList 数据列表
     * @return 写入数据后的工作簿对象
     */
    public static Workbook exportData(List<dataVO> dataList) {
        // 生成xlsx的Excel
        Workbook workbook = new SXSSFWorkbook();

        // 如需生成xls的Excel，请使用下面的工作簿对象，注意后续输出时文件后缀名也需更改为xls
        //Workbook workbook = new HSSFWorkbook();

        // 生成Sheet表，写入第一行的列头
        List<String> cellHead = new ArrayList<>();
        cellHead.add("商品编码");
        cellHead.add("品名");
        cellHead.add("日均销量");
        cellHead.add("现有库存量");
        cellHead.add("单品货架排面数");
        cellHead.add("单品货架纵列数");
        cellHead.add("低库下限补货量");
        cellHead.add("超库上限退仓量");
        cellHead.add("畅销品堆端可陈列量");
        Sheet sheet = buildDataSheet(workbook, cellHead);
        workbook.setSheetName(0, "综合报表");
        //构建每行的数据内容
        int rowNum = 1;
        for (Iterator<dataVO> it = dataList.iterator(); it.hasNext(); ) {
            dataVO data = it.next();
            if (data == null) {
                continue;
            }
            //输出行数据
            Row row = sheet.createRow(rowNum++);
            convertDataToRow(data, row);
        }
        return workbook;
    }

    /**
     * 综合报表
     * @param data 源数据
     * @param row 行对象
     * @return
     */
    private static void convertDataToRow(dataVO data, Row row){
        int cellNum = 0;
        Cell cell;
        // 商品编码
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductCode() ? "" : data.getProductCode());
        // 品名
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getProductName() ? "" : data.getProductName());
        //日均销量
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getAverageDailySales()? "" : data.getAverageDailySales());
        //现有库存量
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getOnHandInventory()? "" : data.getOnHandInventory());
        //单品货架排面数
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getShelfCount() ? "" : data.getShelfCount());
        //单品货架纵列数
        cell = row.createCell(cellNum++);
        cell.setCellValue(null == data.getSingleSidedshelfVolume()? "" : data.getSingleSidedshelfVolume());
        //低库下限补货量
        cell = row.createCell(cellNum++);
        if(data.getBelowStatus() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(null == data.getRenewal()? "" : data.getRenewal());
        }
        //超库上限退仓量
        cell = row.createCell(cellNum++);
        if(data.getExceedStatus() == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(null == data.getWithdrawal()? "" : data.getWithdrawal());
        }
        //畅销品堆端可陈列量
        cell = row.createCell(cellNum++);
        if (Double.valueOf(data.getAverageDailySales()) >= 2 && data.validInventory()) {
            cell.setCellValue(null == data.getAmountOnDisplay() ? "" : data.getAmountOnDisplay());
        } else {
            cell.setCellValue("");
        }
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
}
