package com.hogoworld.biryanihouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.InventoryItemAdapter;
import bean.InventoryItemDetails;
import utility.UserVariables;

public class InventoryActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    List<InventoryItemDetails> listDetails;
    RecyclerView recycler;
    private static RelativeLayout bottomCartLayout;
    private static ImageView imgCart;
    private static TextView txtNoItemsAdded, txtPlusTaxes;
    private static TextView txtCostOFItemAdded, txtViewCart;
    InventoryItemAdapter adapter;
    AQuery aq;
    String URL;
    JSONObject jobj;
    JSONArray jarray;
    String name;
    String parentId;
    int id;
    String itemId;
    String intentName, menuCategory;
    int count = 0;
    String cost;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        aq = new AQuery(getApplicationContext());
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);


        Intent intent = getIntent();
        itemId = intent.getStringExtra("itemId");
        intentName = intent.getStringExtra("itemName");
        menuCategory = intent.getStringExtra("whichMenu");
        getMenuData(menuCategory, itemId);
        Log.d("Inventory", "Item : " + itemId + intentName);


        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(intentName);

        mtoolbar.setNavigationIcon(R.drawable.ic_back_dark);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InventoryActivity.this, MenuActivity.class);
                intent.putExtra("whichMenu", menuCategory);
                startActivity(intent);
                finish();

            }
        });

        bottomCartLayout = (RelativeLayout) findViewById(R.id.inventory_add_to_cart_bottom_layout);
        imgCart = (ImageView) findViewById(R.id.img_shopping_cart_bottom_inventory);
        txtNoItemsAdded = (TextView) findViewById(R.id.txt_no_items_added_inventory);
        txtCostOFItemAdded = (TextView) findViewById(R.id.txt_cost_added_inventory);
        txtPlusTaxes = (TextView) findViewById(R.id.txt_plus_taxes_inventory);
        txtViewCart = (TextView) findViewById(R.id.txt_view_cart_inventory);

        recycler = (RecyclerView) findViewById(R.id.recycler_inventory_list);

        txtViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InventoryActivity.this, CartViewActivity.class);
                intent.putExtra("whichMenu", menuCategory);
                startActivity(intent);
                finish();

            }
        });
    }

    private void getMenuData(final String menuCategory, final String itemId) {

        if (menuCategory.equalsIgnoreCase("BH")) {
            URL = UserVariables.KEY_BASE_URL + "bh_menu_item/" + itemId;
        } else if (menuCategory.equalsIgnoreCase("COS")) {
            URL = UserVariables.KEY_BASE_URL + "cf_menu_item/" + itemId;
        }

        aq.ajax(URL, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);

                try {

                    if (object.getString("response").equalsIgnoreCase("success")) {

                        jarray = object.getJSONArray("userdata");
                        Log.d("Array", "Length : " + jarray.length());
                        listDetails = new ArrayList<>();
                        count = jarray.length();
                        for (int i = 0; i < jarray.length(); i++) {

                            jobj = jarray.getJSONObject(i);
                            parentId = jobj.getString("parent_id");
                            id = jobj.getInt("id");
                            name = jobj.getString("name");
                            cost = jobj.getString("cost");

                            String[] separated = cost.split(",");
                            cost = separated[0] + separated[1];

                            Log.d("Inventory", "Cost after splitting : " + cost);
                            String finalCost = cost;

                            InventoryItemDetails itemDetails = new InventoryItemDetails();
                            itemDetails.setParentId(parentId);
                            itemDetails.setItemId(itemId);
                            itemDetails.setItemName(name);
                            itemDetails.setItemPrice(finalCost);
                            listDetails.add(itemDetails);

                            if (!listDetails.isEmpty()) {

                                adapter = new InventoryItemAdapter(InventoryActivity.this, listDetails);
                                recycler.setAdapter(adapter);
                                recycler.setLayoutManager(new LinearLayoutManager(InventoryActivity.this));

                            } else {

                                Toast.makeText(InventoryActivity.this, "List is empty", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void showBottomCartView(int costItem, int items) {

        bottomCartLayout.setVisibility(View.VISIBLE);
        DecimalFormat dformat = new DecimalFormat("0.00");
        txtCostOFItemAdded.setText("UGX " + dformat.format(costItem));
        txtNoItemsAdded.setText(items + " item(s) added");
    }

    public static void hideBottomCartView() {

        String cost = txtCostOFItemAdded.getText().toString();
        Log.d("Cost", "In activity : " + cost);
        if (cost.equalsIgnoreCase("UGX 0.00")) {
            bottomCartLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(InventoryActivity.this, MenuActivity.class);
        intent.putExtra("whichMenu", menuCategory);
        startActivity(intent);
        finish();
    }

    //    //////For RecyclerView////
//    public static interface ClickListener {
//
//        public void onClick(View view, int position);
//
//        public void onLongClick(View view, int position);
//
//    }
//
//    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
//
//        GestureDetector detector;
//        ClickListener clickListener;
//
//        public RecyclerTouchListener(Context context, RecyclerView view, final ClickListener listener) {
//
//            this.clickListener = listener;
//            detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public void onLongPress(MotionEvent e) {
//                    super.onLongPress(e);
//
//                    View child = recycler.findChildViewUnder(e.getX(), e.getY());
//                    if (child != null && clickListener != null) {
//
//                        clickListener.onLongClick(child, recycler.getChildAdapterPosition(child));
//
//                    }
//
//                }
//            });
//
//        }
//
//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            View child = rv.findChildViewUnder(e.getX(), e.getY());
//            if (child != null && clickListener != null && detector.onTouchEvent(e)) {
//                clickListener.onClick(child, rv.getChildAdapterPosition(child));
//            }
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//        }
//
//        @Override
//        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//        }
//    }


}
