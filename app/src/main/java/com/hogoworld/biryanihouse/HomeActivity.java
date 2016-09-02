package com.hogoworld.biryanihouse;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    FloatingToolbar floatingToolbar;
    Toolbar toolbar;
    FloatingActionButton fab;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    DrawerLayout drawer;
    View headerView;
    TextView txt_header_username;
    TextView txt_header_useremail;
    ImageView img_user_pic;
    Button btnBHheader, btnCOSHeader;
    ImageView bhImg, cosImg;
    boolean isBHPressed = false, isCOSPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        floatingToolbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);
        floatingToolbar.attachFab(fab);
        floatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem menuItem) {

                Toast.makeText(HomeActivity.this, "" + menuItem, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClick(MenuItem menuItem) {

            }
        });

        bhImg = (ImageView) findViewById(R.id.imageView_bh_main_home);
        cosImg = (ImageView) findViewById(R.id.imageView_cos_main_home);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);

        txt_header_username = (TextView) headerView.findViewById(R.id.txt_header_name);
        txt_header_useremail = (TextView) headerView.findViewById(R.id.txt_header_email);
        img_user_pic = (ImageView) headerView.findViewById(R.id.circle_img_view);
        btnBHheader = (Button) headerView.findViewById(R.id.btn_bh_header);
        btnCOSHeader = (Button) headerView.findViewById(R.id.btn_cos_header);

        btnBHheader.setOnClickListener(this);
        btnCOSHeader.setOnClickListener(this);
        bhImg.setOnClickListener(this);
        cosImg.setOnClickListener(this);

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888).displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(HomeActivity.this).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(5 * 1024 * 1024).defaultDisplayImageOptions(options).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();

        ImageLoader.getInstance().init(configuration);

    }

    @Override
    public void onClick(View v) {

        int itemId = v.getId();

        switch (itemId) {

            case R.id.btn_bh_header:
                btnBHheader.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                btnCOSHeader.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                isBHPressed = true;
                isCOSPressed = false;
                break;

            case R.id.btn_cos_header:
                btnBHheader.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btnCOSHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                isCOSPressed = true;
                isBHPressed = false;
                break;

            case R.id.imageView_bh_main_home:
                Toast.makeText(HomeActivity.this, "Biryani house", Toast.LENGTH_SHORT).show();
                Intent bh_intent = new Intent(HomeActivity.this, MenuActivity.class);
                bh_intent.putExtra("whichMenu", "BH");
                startActivity(bh_intent);
                finish();
                break;

            case R.id.imageView_cos_main_home:
                Toast.makeText(HomeActivity.this, "Cafe one six", Toast.LENGTH_SHORT).show();
                Intent cos_intent = new Intent(HomeActivity.this, MenuActivity.class);
                cos_intent.putExtra("whichMenu", "COS");
                startActivity(cos_intent);
                finish();
                break;

        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

            Toast.makeText(HomeActivity.this, "Please select Biryani House or Cafe one six first", Toast.LENGTH_LONG).show();

            if (isBHPressed) {
                Toast.makeText(HomeActivity.this, "Biryani house", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                intent.putExtra("whichMenu", "BH");
                startActivity(intent);
                finish();

            } else if (isCOSPressed) {

                Toast.makeText(HomeActivity.this, "Cafe one six", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                intent.putExtra("whichMenu", "COS");
                startActivity(intent);
                finish();

            }


        } else if (id == R.id.nav_offer) {

            Toast.makeText(HomeActivity.this, "Offers", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_book_table) {

            Toast.makeText(HomeActivity.this, "Book Table", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_history) {

            Toast.makeText(HomeActivity.this, "History", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_cashback) {

            Toast.makeText(HomeActivity.this, "CashBack", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_invite) {

            Toast.makeText(HomeActivity.this, "Invite", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_reviews) {

            Toast.makeText(HomeActivity.this, "Reviews", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_account) {

            Toast.makeText(HomeActivity.this, "Account", Toast.LENGTH_SHORT).show();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_navigation) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }
}
