package test;

public class testStore {
    /*    private static Logger logger = Logger.getLogger(mainTest.class.getName());

    public static void main(String[] args) {
        // 设定Excel文件所在路径
        String excelFileName = "./inputFiles/06店库存周转及订货量控制标准7.29.xlsx";
        // 读取Excel文件内容
        List<dataVO> readResult = excelReader.readExcel(excelFileName);
        // 写入数据到工作簿对象内
        Workbook workbook = excelWriter.exportData(readResult);
        // 以文件的形式输出工作簿对象
        FileOutputStream fileOut = null;
        try {
            String exportFilePath = "./outputFiles/writeExample1.xlsx";
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
    }*/
}
