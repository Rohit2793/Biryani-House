package com.hogoworld.biryanihouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.CustomExpandableAdapter;
import bean.CartViewChild;


/**
 * Created by rohit on 8/18/16.
 */
public class CartViewActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> listTitle;
    HashMap<String, List<String>> expandableChildList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartview);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle("Your Cart");

        mtoolbar.setNavigationIcon(R.drawable.ic_back_dark);
//        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(CartViewActivity.this, InventoryActivity.class));
//                finish();
//
//            }
//        });

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView_cartview);
        expandableChildList = CartViewChild.getData();
        listTitle = new ArrayList<>(expandableChildList.keySet());

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

//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                Toast.makeText(
//                        CartViewActivity.this,
//                        listTitle.get(groupPosition)
//                                + " -> "
//                                + expandableChildList.get(
//                                listTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
//                return false;
//            }
//        });

    }

}
