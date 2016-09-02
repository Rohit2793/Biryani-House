package com.hogoworld.biryanihouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.CartViewAdapter;
import adapter.CustomExpandableAdapter;
import bean.CartViewChild;
import bean.CartViewDetails;
import database.AddToCartDB;


/**
 * Created by rohit on 8/18/16.
 */
public class CartViewActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> listTitle;
    HashMap<String, List<String>> expandableChildList;
    RecyclerView mRecycler;
    CartViewAdapter mainAdapter;
    List<CartViewDetails> listDetails = new ArrayList<>();
    AddToCartDB addToCartDB;
    LinearLayout subTotalLayout;
    TextView txtProceed;
    public static TextView txtSubTotalAmount;
    EditText editCookingInsturctions;
    AQuery aq;
    String merchantId, merchantToken;
    String totalAmount;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartview);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        addToCartDB = new AddToCartDB(CartViewActivity.this);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle("Your Cart");

        mtoolbar.setNavigationIcon(R.drawable.ic_back_dark);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CartViewActivity.this, InventoryActivity.class));
                finish();

            }
        });
        mRecycler = (RecyclerView) findViewById(R.id.recycler_cartview);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_cartview);
        expandableChildList = CartViewChild.getData();
        listTitle = new ArrayList<>(expandableChildList.keySet());
        txtProceed = (TextView) findViewById(R.id.txt_proceed_payment_cartview);
        subTotalLayout = (LinearLayout) findViewById(R.id.subtotal_layout_cartview);
        txtSubTotalAmount = (TextView) findViewById(R.id.txt_sub_total_amount_cartview);
        editCookingInsturctions = (EditText) findViewById(R.id.edit_special_instructions_cartview);


        expandableListAdapter = new CustomExpandableAdapter(CartViewActivity.this, listTitle, expandableChildList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                Toast.makeText(CartViewActivity.this,
                        listTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(CartViewActivity.this,
                        listTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CartViewActivity.this, CheckoutActivity.class));
                finish();

            }
        });

        listDetails = addToCartDB.displayAllItems();

        for (int i = 0; i < listDetails.size(); i++) {

            Log.d("Database", "Item : " + listDetails.get(i).getMenuItemName());
        }

        if (listDetails.size() > 0) {

            mainAdapter = new CartViewAdapter(CartViewActivity.this, listDetails);
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.setAdapter(mainAdapter);

        }

    }

    public static void showTotalCost(double totalCost) {

        DecimalFormat dformat = new DecimalFormat("0.00");
        txtSubTotalAmount.setText("$ " + dformat.format(totalCost));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(CartViewActivity.this, InventoryActivity.class));
        finish();
    }
}
