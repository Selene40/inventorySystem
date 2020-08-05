package originalFileOperation;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


public class multiStore {
    private static Logger logger = Logger.getLogger(mainTest.class.getName());
    public static void operation(String input, String output) {
        // 设定Excel文件所在路径
        String excelFileName = "./inputFiles/" + input;
        // 读取Excel文件内容
        List<dataVO> readResult = excelReader.readExcel(excelFileName);
        // 写入数据到工作簿对象内
        Workbook workbook = excelWriter.exportData(readResult);
        // 以文件的形式输出工作簿对象
        FileOutputStream fileOut = null;
        try {
            String exportFilePath = "./outputFiles/" + output;
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
