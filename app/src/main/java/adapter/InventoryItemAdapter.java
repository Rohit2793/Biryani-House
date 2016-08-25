package adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hogoworld.biryanihouse.InventoryActivity;
import com.hogoworld.biryanihouse.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.InventoryItemDetails;

/**
 * Created by rohit on 7/27/16.
 */
public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.ViewHolder> {

    Context context;
    List<InventoryItemDetails> detailsList = new ArrayList<>();
    List<InventoryItemDetails> tempList = new ArrayList<>();
    HashMap<Integer, String> mapPosition = new HashMap<>();
    LayoutInflater inflater;
    float totalCost;
    float tempCost;
    int noOfItems = 0;

    public InventoryItemAdapter(Context context, List<InventoryItemDetails> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        tempList.addAll(detailsList);
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < detailsList.size(); i++) {
            mapPosition.put(i, "1");
        }
    }

    @Override
    public InventoryItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.single_menu_item_row, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final InventoryItemAdapter.ViewHolder holder, final int position) {

        final InventoryItemDetails details = detailsList.get(position);
        holder.txtItemName.setText(details.getMenuItemName());
        holder.txtItemCost.setText("UGX " + details.getMenuItemAmount());

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        holder.btnAddToCart.setVisibility(View.GONE);
                        holder.plusMinusLayout.setVisibility(View.VISIBLE);
                        final int pos = holder.getPosition();

                        String valu = mapPosition.get(pos);
                        int countValue = Integer.parseInt(valu);

                        holder.txtCountItem.setText("" + countValue);
                        detailsList.get(position).getMenuItemName();
                        noOfItems = noOfItems + 1;
                        tempCost = details.getMenuItemAmount();
                        totalCost = totalCost + tempCost;

                        InventoryActivity.showBottomCartView(totalCost, noOfItems);

                        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String valu = mapPosition.get(pos);
                                int countValue = Integer.parseInt(valu);

                                float costOfitem = detailsList.get(pos).getMenuItemAmount();
                                countValue = countValue + 1;
                                int countVal = Integer.parseInt(holder.txtCountItem.getText().toString());

                                double totalValue = calculateOnAdd(costOfitem, countVal);
                                holder.txtCountItem.setText("" + countValue);
                                mapPosition.put(pos, "" + countValue);

                                InventoryActivity.showBottomCartView(totalValue, noOfItems);


                            }
                        });

                        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String valu = mapPosition.get(pos);
                                int countValue = Integer.parseInt(valu);

                                if (countValue <= 1) {

                                    int txtCount = Integer.parseInt(holder.txtCountItem.getText().toString());

                                    if (txtCount == 1) {

                                        float costOfitem = detailsList.get(pos).getMenuItemAmount();
                                        countValue = countValue - 1;
                                        double totalValue = calculateOnSubtract(costOfitem, countValue);

                                        noOfItems = noOfItems - 1;
                                        if (countValue != 0) {
                                            mapPosition.put(pos, "" + countValue);
                                            holder.txtCountItem.setText("" + countValue);
                                        }

                                        InventoryActivity.showBottomCartView(totalValue, noOfItems);
                                    }

                                    holder.btnAddToCart.setVisibility(View.VISIBLE);
                                    holder.plusMinusLayout.setVisibility(View.GONE);
                                    InventoryActivity.hideBottomCartView();

                                } else {

                                    float costOfitem = detailsList.get(pos).getMenuItemAmount();
                                    countValue = countValue - 1;
                                    double totalValue = calculateOnSubtract(costOfitem, countValue);
                                    mapPosition.put(pos, "" + countValue);
                                    holder.txtCountItem.setText("" + countValue);

                                    InventoryActivity.showBottomCartView(totalValue, noOfItems);
                                }

                            }
                        });

                    }
                });


            }
        });

    }

    float calculateOnAdd(float costItem, int quantity) {

        float value = costItem * quantity;

        totalCost = totalCost + value;

        return totalCost;
    }

    float calculateOnSubtract(float costItem, int quantity) {

        if (quantity != 0) {
            float value = costItem * quantity;
            totalCost = totalCost - value;
        } else {
            float value = totalCost - costItem;
            if (value < 0) {
                totalCost = 0;
            } else {
                totalCost = value;
            }

        }
        return totalCost;
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName;
        TextView txtItemCost;
        ImageView VNVLogo;
        Button btnAddToCart;
        LinearLayout plusMinusLayout;
        Button btnPlus, btnMinus;
        TextView txtCountItem;

        public ViewHolder(View itemView) {
            super(itemView);

            txtItemName = (TextView) itemView.findViewById(R.id.txt_menu_item_name_inventory);
            txtItemCost = (TextView) itemView.findViewById(R.id.txt_menu_item_cost_inventory);
            VNVLogo = (ImageView) itemView.findViewById(R.id.v_nv_inventory_view_logo);
            btnAddToCart = (Button) itemView.findViewById(R.id.btn_add_to_cart_inventory);
            plusMinusLayout = (LinearLayout) itemView.findViewById(R.id.plus_minus_inventory_layout);
            btnPlus = (Button) itemView.findViewById(R.id.btn_plus_inventory);
            btnMinus = (Button) itemView.findViewById(R.id.btn_minus_inventory);
            txtCountItem = (TextView) itemView.findViewById(R.id.txt_count_value_inventory);

        }
    }
}
