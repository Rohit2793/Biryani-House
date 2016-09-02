package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hogoworld.biryanihouse.CartViewActivity;
import com.hogoworld.biryanihouse.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.CartViewDetails;
import database.AddToCartDB;

/**
 * Created by rohit on 8/16/16.
 */
public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.ViewHolder> {

    Context context;
    List<CartViewDetails> detailsList = new ArrayList<>();
    List<CartViewDetails> tempList = new ArrayList<>();
    List<CartViewDetails> datalist = new ArrayList<>();
    HashMap<Integer, String> mapPosition = new HashMap<>();
    LayoutInflater inflater;
    float overallCost;
    double tempCost;

    AddToCartDB addToCartDB;

    public CartViewAdapter(Context context, List<CartViewDetails> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        inflater = LayoutInflater.from(context);
        tempList.addAll(detailsList);
        addToCartDB = new AddToCartDB(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cart_view_single_item_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final int pos = holder.getPosition();

        if (detailsList.size() > 0) {

            CartViewDetails data;

            data = detailsList.get(position);

            float costOfitem = data.getMenuItemAmount();
            int count = Integer.parseInt(data.getItemCount());

            Log.d("Adapter", " Cost of item plus: " + costOfitem + " name : " + data.getMenuItemName() + " count : " + count);

            for (int i = 0; i < detailsList.size(); i++) {
                mapPosition.put(i, "" + count);
            }

//            double totalValue = calculateOnAdd(costOfitem, count);

//            CartViewActivity.showTotalCost(totalValue);
            holder.txtItemName.setText(data.getMenuItemName());
            holder.txtCountItem.setText("" + data.getItemCount());

        }

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valu = mapPosition.get(pos);
                int countValue = Integer.parseInt(valu);

                float costOfitem = detailsList.get(pos).getMenuItemAmount();

                Log.d("Adapter", " Cost of item plus: " + costOfitem);

//                holder.txtItemCost.setText("$ " + costOfitem);

                Log.d("Adapter", " Count of item plus: " + countValue);
                countValue = countValue + 1;

                Log.d("Adapter", " Count of item plus plus: " + countValue);

                mapPosition.put(pos, "" + countValue);

                double totalValue = calculateOnAdd(costOfitem, 1);

                CartViewActivity.showTotalCost(totalValue);

                holder.txtCountItem.setText("" + countValue);
//                holder.txtQuantity.setText("" + countValue);
//                holder.txtTotalCost.setText("$ " + dformat.format(totalValue));


            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String valu = mapPosition.get(pos);
                int countValue = Integer.parseInt(valu);
                Log.d("Adapter", "Count Value : " + countValue);
                if (countValue <= 1) {

                    int txtCount = Integer.parseInt(holder.txtCountItem.getText().toString());
                    Log.d("Adapter", "Txt count : " + txtCount);
                    if (txtCount == 1) {

                        Log.d("Adapter", "in txt count loop");
                        float costOfitem = detailsList.get(pos).getMenuItemAmount();
//                        holder.txtItemCost.setText("" + costOfitem);
                        countValue = countValue - 1;

                        double totalValue = calculateOnSubtract(costOfitem, 1);

                        CartViewActivity.showTotalCost(totalValue);
//                        holder.txtTotalCost.setText("$ " + dformat.format(totalValue));

                        if (countValue != 0) {
                            mapPosition.put(pos, "" + countValue);
                            holder.txtCountItem.setText("" + countValue);
//                            holder.txtQuantity.setText("" + countValue);
                        }

                    }

                    detailsList.remove(position);
                    notifyDataSetChanged();

                } else {

                    float costOfitem = detailsList.get(pos).getMenuItemAmount();
                    countValue = countValue - 1;

                    double totalValue = calculateOnSubtract(costOfitem, 1);

                    CartViewActivity.showTotalCost(totalValue);
                    mapPosition.put(pos, "" + countValue);

                    holder.txtCountItem.setText("" + countValue);
//                    holder.txtQuantity.setText("" + countValue);
//                    holder.txtTotalCost.setText("" + dformat.format(totalValue));
                }

            }
        });


    }

    float calculateOnAdd(float costItem, int quantity) {

        float value = costItem * quantity;

        overallCost = overallCost + value;

        return overallCost;
    }

    float calculateOnSubtract(float costItem, int quantity) {

        if (quantity != 0) {
            float value = costItem * quantity;
            overallCost = overallCost - value;

        } else {
            float value = overallCost - costItem;

            if (value < 0) {
                overallCost = 0;

            } else {
                overallCost = value;

            }

        }

        return overallCost;
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtTotalCost;
        TextView txtItemCost, txtQuantity;
        ImageView VNVLogo;
        LinearLayout plusMinusLayout;
        Button btnPlus, btnMinus;
        TextView txtCountItem;

        public ViewHolder(View itemView) {
            super(itemView);

            txtItemName = (TextView) itemView.findViewById(R.id.txt_pizza_item_name_cartview);
//            txtItemCost = (TextView) itemView.findViewById(R.id.txt_cost_peritem_cartview);
//            txtTotalCost = (TextView) itemView.findViewById(R.id.txt_total_cost_item_cartview);
//            txtQuantity = (TextView) itemView.findViewById(R.id.txt_quantity_item_cartview);

            VNVLogo = (ImageView) itemView.findViewById(R.id.img_V_NV_icon_cartview);
            plusMinusLayout = (LinearLayout) itemView.findViewById(R.id.minus_plus_layout_cartview);
            btnPlus = (Button) itemView.findViewById(R.id.btn_plus_cartview);
            btnMinus = (Button) itemView.findViewById(R.id.btn_minus_cartview);
            txtCountItem = (TextView) itemView.findViewById(R.id.txt_count_value_cartview);

        }
    }
}
