package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hogoworld.biryanihouse.InventoryActivity;
import com.hogoworld.biryanihouse.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import bean.MenuHomeDetails;

/**
 * Created by rohit on 7/18/16.
 */
public class MenuHomeAdapter extends RecyclerView.Adapter<MenuHomeAdapter.ViewHolder> {

    Context context;
    List<MenuHomeDetails> homeDetailsList = new ArrayList<>();
    LayoutInflater inflater;
    int categoryId;

    public MenuHomeAdapter(Context context, List<MenuHomeDetails> homeDetailsList1) {
        this.context = context;
        this.homeDetailsList = homeDetailsList1;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.menu_home_items_row, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final MenuHomeDetails details = homeDetailsList.get(position);
        holder.tv.setText(details.getMenuName());
        ImageLoader imgLoader = ImageLoader.getInstance();
        imgLoader.loadImage(details.getBackgroundImg(), new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                holder.rl.setBackgroundDrawable(new BitmapDrawable(loadedImage));
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeDetailsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl;
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

            rl = (RelativeLayout) itemView.findViewById(R.id.category_menu_item_layout);
            tv = (TextView) itemView.findViewById(R.id.txtview_menu_home);
        }
    }
}
