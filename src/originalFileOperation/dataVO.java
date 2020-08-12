package originalFileOperation;

public class dataVO {

    private String productCode; // 商品编码
    private String productName; // 品名
    private String averageDailySales; // 日均销量
    private String onHandInventory; // 现有库存量
    private String singleSidedshelfVolume; // 货架存放纵列量

    private int deliveryCycle = 3; // 配送周期
    private int dailyProductsDays = 10;//店日销存货天数
    private double avg, cur, oneShelf;//日均销量, 现有库存量, 架存放纵列量 的整数型

    //计算得到的
    private int shelfCount; // 商品陈列面位数
    private double guaranteedQuantity; // 单品不缺货必保货量
    private double guaranteedQuaOnShelf; // 单品货架陈列必保量
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

    public String getSingleSidedshelfVolume() {
        return singleSidedshelfVolume;
    }

    public void setSingleSidedshelfVolume(String singleSidedshelfVolume) {
        this.singleSidedshelfVolume = singleSidedshelfVolume;
        oneShelf = Double.valueOf(singleSidedshelfVolume);
    }

    public void init() {
        if (avg >= 10) {
            this.shelfCount = 6;
        } else if (avg >= 3 && avg < 10) {
            this.shelfCount = 4;
        } else if (avg >= 1 && avg < 3) {
            this.shelfCount = 2;
        } else {
            this.shelfCount = 1;
        }
        this.guaranteedQuaOnShelf = shelfCount * oneShelf;
        this.guaranteedQuantity = avg * (dailyProductsDays + deliveryCycle);
        this.amountOnDisplay = cur - guaranteedQuaOnShelf;
        this.lowerLimit = Math.max(guaranteedQuantity, guaranteedQuaOnShelf);
        this.upperLimit = Math.max(lowerLimit, avg * (20 + deliveryCycle));
        this.renewal = cur - lowerLimit;
        this.withdrawal = cur - upperLimit;
    }

    // 处于合理库存区间
    public boolean validInventory() {
        if (cur >= lowerLimit && cur <= upperLimit)
            return true;
        else
            return false;
    }

    // 商品陈列面位数
    public String getShelfCount() {
        return "" + shelfCount;
    }

    // 续订量
    public String getRenewal() {
        return "" + (int)Math.abs(renewal);
    }

    // 退仓量
    public String getWithdrawal() {
        return "" + (int)Math.abs(withdrawal);
    }

    // 堆头端架陈列量
    public String getAmountOnDisplay() {
        return "" + (int)amountOnDisplay;
    }

    public String getExceedStatus() {
        if (withdrawal > 0)
            return "退仓";
        else
            return null;
    }

    public String getBelowStatus() {
        if (renewal < 0)
            return "补货";
        else
            return null;
    }


/*    public void setAmountOnDisplay(int amountOnDisplay) {
        this.amountOnDisplay = cur - guaranteedQuaOnShelf;
    }*/

/*    public void setRenewal() {
        this.renewal = cur - lowerLimit;
    }*/

/*    public void setWithdrawal() {
        this.withdrawal = cur - upperLimit;
    }*/

/*    public void setLowerLimit() {
        this.lowerLimit = Math.max(guaranteedQuantity, guaranteedQuaOnShelf);
    }*/

/*    public void setUpperLimit() {
        this.upperLimit = Math.max(lowerLimit, avg * (20 + deliveryCycle));
    }*/

/*    public void setGuaranteedQuaOnShelf() {
        this.guaranteedQuaOnShelf = shelfCount * oneShelf;
    }*/

/*    public void setGuaranteedQuantity() {
        this.guaranteedQuantity = avg * (dailyProductsDays + deliveryCycle);
    }*/

/*    public void setShelfCount() {
        if (avg >= 10) {
            this.shelfCount = 6;
        } else if (avg >= 3 && avg < 10) {
            this.shelfCount = 4;
        } else if (avg >= 1 && avg < 3) {
            this.shelfCount = 2;
        } else {
            this.shelfCount = 1;
        }
    }*/

}
