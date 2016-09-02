package com.hogoworld.biryanihouse;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MenuHomeAdapter;
import bean.MenuHomeDetails;
import utility.UserVariables;

public class MenuActivity extends AppCompatActivity {

    Toolbar mtoolbar;

    MenuHomeAdapter adapter;
    RecyclerView recyclerView;
    List<MenuHomeDetails> detailsList;
    AQuery aq;
    String URL;
    JSONObject jobj;
    JSONArray jarray;
    String name;
    String image;
    String id;
    int count = 0;
    int categoryId;
    View progressView;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        aq = new AQuery(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_menu);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
        mtoolbar.setTitle("Menu");
        mtoolbar.setNavigationIcon(R.drawable.ic_back_dark);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });

        progressView = findViewById(R.id.progress_layout_menu);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_menu);
        textView = (TextView) findViewById(R.id.txt_progress_menu);

        Intent intent = getIntent();
        final String btnTxt = intent.getStringExtra("whichMenu");
        postRespectiveMenu(btnTxt);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(MenuActivity.this, "" + view.toString(), Toast.LENGTH_SHORT).show();
                MenuHomeDetails details = detailsList.get(position);
                String itemId = details.getId();
                String itemName = details.getMenuName();

                Intent intent = new Intent(MenuActivity.this, InventoryActivity.class);
                intent.putExtra("itemId", itemId);
                intent.putExtra("itemName", itemName);
                intent.putExtra("whichMenu", btnTxt);
                startActivity(intent);
                finish();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void postRespectiveMenu(String whichMenu) {

        if (whichMenu.equalsIgnoreCase("BH")) {
            URL = UserVariables.KEY_BASE_URL + "bh_menu";
        } else if (whichMenu.equalsIgnoreCase("COS")) {
            URL = UserVariables.KEY_BASE_URL + "cf_menu";
        }

        recyclerView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);

        aq.ajax(URL, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);

                try {

                    if (object.getString("response").equalsIgnoreCase("success")) {

                        jarray = object.getJSONArray("userdata");
                        Log.d("Array", "Length : " + jarray.length());
                        detailsList = new ArrayList<>();
                        count = jarray.length();
                        for (int i = 0; i < jarray.length(); i++) {

                            jobj = jarray.getJSONObject(i);
                            id = jobj.getString("id");
                            name = jobj.getString("name");
                            image = jobj.getString("img");
                            MenuHomeDetails details = new MenuHomeDetails();
                            details.setId(id);
                            details.setMenuName(name);
                            details.setBackgroundImg(image);
                            detailsList.add(details);

                            Log.d("Menu", "Menu details : " + id + name);

                        }

                        if (!detailsList.isEmpty()) {

                            adapter = new MenuHomeAdapter(MenuActivity.this, detailsList);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));

                            recyclerView.setVisibility(View.VISIBLE);
                            progressView.setVisibility(View.GONE);

                        } else {

                            Toast.makeText(MenuActivity.this, "List is empty", Toast.LENGTH_SHORT).show();

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    //////For RecyclerView////
    public static interface ClickListener {

        public void onClick(View view, int position);

        public void onLongClick(View view, int position);

    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        GestureDetector detector;
        ClickListener clickListener;

        public RecyclerTouchListener(Context context, RecyclerView view, final ClickListener listener) {

            this.clickListener = listener;
            detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {

                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));

                    }

                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && detector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MenuActivity.this, HomeActivity.class));
        finish();
    }
}
