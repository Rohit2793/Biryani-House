package bean;

/**
 * Created by rohit on 9/2/16.
 */
public class CartViewDetails {

    String menuItemName;
    int menuItemVegNVLogo;
    float menuItemAmount, totalItemAmount;
    String itemCount, itemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
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

    public float getTotalItemAmount() {
        return totalItemAmount;
    }

    public void setTotalItemAmount(float totalItemAmount) {
        this.totalItemAmount = totalItemAmount;
    }

    public float getMenuItemAmount() {
        return menuItemAmount;
    }

    public void setMenuItemAmount(float menuItemAmount) {
        this.menuItemAmount = menuItemAmount;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

}
