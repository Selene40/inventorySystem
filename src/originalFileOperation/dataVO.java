package originalFileOperation;

public class dataVO {

    private String productCode; // 商品编码
    private String productName; // 品名
    private String averageDailySales; // 日均销量
    private String onHandInventory; // 现有库存量
    private String itemAttribute; // 单品属性
    private String bottomSize;//单品底部尺寸

    private int deliveryCycle = 3; // 配送周期
    private int dailyProductsDays = 10;//店日销存货天数
    private double avg, cur, size;//日均销量, 现有库存量, 单品底部尺寸 的整数型; 架存放纵列量(oneShelf)

    //计算得到的
    private int shelfCount; // 商品货架陈列排面数
    private int singleSidedshelfVolume;// 货架存放纵列量
    private double guaranteedQuantity; // 单品不缺货必保货量
    private double guaranteedQuaOnShelf; // 货架陈列基础量
    private double lowerLimit; // 库存量存货下限
    private double upperLimit; // 库存量存货上限
    private double renewal; // 续订量
    private double withdrawal; // 退仓量
    private double amountOnDisplay; // 堆头端架陈列量


    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAverageDailySales() {
        return averageDailySales;
    }

    public void setAverageDailySales(String averageDailySales) {
        this.averageDailySales = averageDailySales;
        avg = Double.valueOf(averageDailySales);
    }

    public String getOnHandInventory() {
        return onHandInventory;
    }

    public void setOnHandInventory(String onHandInventory) {
        this.onHandInventory = onHandInventory;
        cur = Double.valueOf(onHandInventory);
    }

    public String getItemCategory() {
        return itemAttribute;
    }

    public void setItemCategory(String itemAttribute) {
        this.itemAttribute = itemAttribute;
    }

    public String getBottomSize() {
        return bottomSize;
    }

    public void setBottomSize(String bottomSize) {
        this.bottomSize = bottomSize;
        size = Double.valueOf(bottomSize);
    }

    public void init() {
        int tempByItemAttribute = 0;
        if (itemAttribute.equals("必售")) {
            tempByItemAttribute = 6;
        } else if (itemAttribute.equals("流行") || itemAttribute.equals("季节")) {
            tempByItemAttribute = 4;
        } else if (itemAttribute.equals("结构") || itemAttribute.equals("特产")) {
            tempByItemAttribute = 2;
        } else if (itemAttribute.equals("普通")) {
            tempByItemAttribute = 1;
        }

        int tempByDailySales = 0;
        if (avg >= 10) {
            tempByDailySales = 6;
        } else if (avg >= 3 && avg < 10) {
            tempByDailySales = 4;
        } else if (avg >= 1 && avg < 3) {
            tempByDailySales = 2;
        } else {
            tempByDailySales = 1;
        }

        this.shelfCount = Math.max(tempByDailySales, tempByItemAttribute);


        if (shelfCount > 4 && size > 10) {
            this.shelfCount /= 2;
        }

        if (itemAttribute.equals("禁采")) {
            this.shelfCount = 0;
        }

        if (avg >= 10) {
            this.singleSidedshelfVolume = 4;
        } else if (avg >= 3 && avg < 10) {
            this.singleSidedshelfVolume = 3;
        } else {
            this.singleSidedshelfVolume = 2;
        }

        if (itemAttribute.equals("禁采")) {
            this.singleSidedshelfVolume = 0;
        }

        this.guaranteedQuantity = avg * (dailyProductsDays + deliveryCycle);
        this.guaranteedQuaOnShelf = shelfCount * singleSidedshelfVolume;
        this.lowerLimit = Math.max(guaranteedQuantity, guaranteedQuaOnShelf);
        this.upperLimit = Math.max(lowerLimit, avg * (20 + deliveryCycle));
        this.renewal = cur - lowerLimit;
        this.withdrawal = cur - upperLimit;
        this.amountOnDisplay = cur - guaranteedQuaOnShelf;
    }

    // 处于合理库存区间, 现有库存量超过货架陈列量的
    public boolean validInventory() {
        if (cur > guaranteedQuaOnShelf)
            return true;
        else
            return false;
    }

    // 商品陈列面位数
    public String getShelfCount() {
        return "" + shelfCount;
    }

    // 单品货架纵列数
    public String getSingleSidedshelfVolume() {
        return "" + singleSidedshelfVolume;
    }

    // 续订量
    public String getRenewal() {
        return "" + Math.round(Math.abs(renewal));
    }

    // 退仓量
    public String getWithdrawal() {
        return "" + Math.round(Math.abs(withdrawal));
    }

    // 堆头端架陈列量
    public String getAmountOnDisplay() {
        return "" + Math.round(amountOnDisplay);
    }

    public String getExceedStatus() {
        if (Math.round(withdrawal) >= 1)
            return "退仓";
        else
            return null;
    }

    public String getBelowStatus() {
        if (Math.round(renewal) <= -1)
            return "补货";
        else
            return null;
    }
}