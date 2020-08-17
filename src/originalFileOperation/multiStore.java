package originalFileOperation;

import org.apache.poi.ss.usermodel.Workbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class multiStore {
    private static Logger logger = Logger.getLogger(mainTest.class.getName());
    public static void operation(String input) {
        // 设定Excel文件所在路径
        String excelFileName = "./inputFiles/" + input;
        // 读取Excel文件内容
        List<dataVO> readResult = excelReader.readExcel(excelFileName);
        // 写入数据到工作簿对象内
        Workbook workbookPart = excelWriter.exportData(readResult);
        Workbook workbookAll = excelWriterAll.exportData(readResult);
        // 以文件的形式输出单一样式的文件
        FileOutputStream fileOutPart = null;
        String exportFilePathPart = "./outputFiles/" + input.substring(0, input.length() - 5)+ "_单一报表.xlsx";
        outputFile(fileOutPart, exportFilePathPart, workbookPart);
        // 以文件的形式输出合并样式的文件
        FileOutputStream fileOutAll = null;
        String exportFilePathAll = "./outputFiles/" + input.substring(0, input.length() - 5)+ "_合并报表.xlsx";
        outputFile(fileOutAll, exportFilePathAll, workbookAll);
    }

    public static void outputFile(FileOutputStream fileOut, String input, Workbook workbook) {
        try {
            String exportFilePath = input;
            File exportFile = new File(exportFilePath);
            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }
            fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
            logger.warning("输出Excel时发生错误，错误原因：" + e.getMessage());
        } finally {
            try {
                if (null != fileOut) {
                    fileOut.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
                logger.warning("关闭输出流时发生错误，错误原因：" + e.getMessage());
            }
        }
    }
}
