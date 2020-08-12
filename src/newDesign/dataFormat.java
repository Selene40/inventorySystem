package newDesign;

public class dataFormat {

    private String productCode; // 商品编码
    private String productName; // 品名
    private String deliveryCycle; // 配送周期
    private String averageDailySales; // 日均销量
    private String shelfCount; // 商品陈列面位数
    private String singleSidedshelfVolume; // 货架纵深单面陈列量
    private String onHandInventory; // 现有库存量

    private double day, low, up, cur, avg, twentySale, shelves, productOnSignleShelf;

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

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
        day = Double.valueOf(deliveryCycle);
    }

    public String getAverageDailySales() {
        return averageDailySales;
    }

    public void setAverageDailySales(String averageDailySales) {
        this.averageDailySales = averageDailySales;
        avg = Double.valueOf(averageDailySales);
        twentySale = avg * 20;
    }

    public String getShelfCount() {
        return productCode;
    }

    public void setShelfCount(String shelfCount) {
        this.shelfCount = shelfCount;
        shelves = Double.valueOf(shelfCount);
    }

    public String getSingleSidedshelfVolume() {
        return singleSidedshelfVolume;
    }

    public void setSingleSidedshelfVolume(String singleSidedshelfVolume) {
        this.singleSidedshelfVolume = singleSidedshelfVolume;
        productOnSignleShelf = Double.valueOf(singleSidedshelfVolume);
    }

    public double getLowerLimit() {
        low = shelves * productOnSignleShelf + day * avg;
        return low;
    }

    public double getUpperLimit() {
        up = Math.max(low, twentySale);
        return up;
    }

    public String getOnHandInventory() {
        return onHandInventory;
    }

    public void setOnHandInventory(String onHandInventory) {
        this.onHandInventory = onHandInventory;
        cur = Double.valueOf(onHandInventory);
    }

    public String getMerchandiseDisplayArea() {
        if (avg >= 10) {
            return "6";
        } else if (avg >= 3 && avg < 10) {
            return "4";
        } else if (avg >= 1 && avg < 3) {
            return "2";
        } else {
            return "1";
        }
    }

    public String getExceedNum() {
        getLowerLimit();
        getUpperLimit();
        return " " + (int)(up - cur);
    }

    public String getExceedStatus() {
        getLowerLimit();
        getUpperLimit();
        if (up - cur >= 0) return null;//不需要退仓
        return "退仓";
    }

    public String getBelowNum() {
        getLowerLimit();
        getUpperLimit();
        return " " + (int)(low - cur);
    }

    public String getBelowStatus() {
        getLowerLimit();
        getUpperLimit();
        if (low - cur < 1) return null;//不需要加订
        return "加订";
    }




}
