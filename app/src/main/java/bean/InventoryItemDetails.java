package bean;

/**
 * Created by rohit on 7/27/16.
 */
public class InventoryItemDetails {

    String menuItemName;
    int menuItemVegNVLogo;
    float menuItemAmount;
    int parentId, itemId;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public int getMenuItemVegNVLogo() {
        return menuItemVegNVLogo;
    }

    public void setMenuItemVegNVLogo(int menuItemVegNVLogo) {
        this.menuItemVegNVLogo = menuItemVegNVLogo;
    }

    public float getMenuItemAmount() {
        return menuItemAmount;
    }

    public void setMenuItemAmount(float menuItemAmount) {
        this.menuItemAmount = menuItemAmount;
    }
}
