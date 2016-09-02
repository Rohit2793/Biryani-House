package bean;

/**
 * Created by rohit on 7/27/16.
 */
public class InventoryItemDetails {

    String itemName, itemCode, itemIsRevenue, parentId;
    String itemPrice, itemPriceType, itemDefaultTaxRates;
    String itemId, itemHidden, itemGroupId, itemModifiedTime;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemIsRevenue() {
        return itemIsRevenue;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemPriceType() {
        return itemPriceType;
    }

    public String getItemDefaultTaxRates() {
        return itemDefaultTaxRates;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemHidden() {
        return itemHidden;
    }

    public String getItemGroupId() {
        return itemGroupId;
    }

    public String getItemModifiedTime() {
        return itemModifiedTime;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public void setItemIsRevenue(String itemIsRevenue) {
        this.itemIsRevenue = itemIsRevenue;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemPriceType(String itemPriceType) {
        this.itemPriceType = itemPriceType;
    }

    public void setItemDefaultTaxRates(String itemDefaultTaxRates) {
        this.itemDefaultTaxRates = itemDefaultTaxRates;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemHidden(String itemHidden) {
        this.itemHidden = itemHidden;
    }

    public void setItemGroupId(String itemGroupId) {
        this.itemGroupId = itemGroupId;
    }

    public void setItemModifiedTime(String itemModifiedTime) {
        this.itemModifiedTime = itemModifiedTime;
    }
}
