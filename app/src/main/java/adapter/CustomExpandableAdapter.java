package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.hogoworld.biryanihouse.R;

import java.util.HashMap;
import java.util.List;

import bean.CartViewChild;

/**
 * Created by rohit on 8/22/16.
 */
public class CustomExpandableAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> childList;
    HashMap<String, List<String>> expandableListDetail;
    LayoutInflater inflater;

    public CustomExpandableAdapter(Context context, List<String> childList, HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.childList = childList;
        this.expandableListDetail = expandableListDetail;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getGroupCount() {
        return this.childList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return this.expandableListDetail.get(this.childList.get(groupPosition)).size();
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.childList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.childList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String listTitle = (String) getGroup(groupPosition);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.cartview_userdetails_parent_item, parent, false);

            TextView txtParent = (TextView) convertView.findViewById(R.id.txt_userdetails_parent_cartview);
            txtParent.setText(listTitle);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String expandedListName = (String) getChild(groupPosition, 0);
        String expandedListEmail = (String) getChild(groupPosition, 1);
        String expandedListPhone = (String) getChild(groupPosition, 2);
        String expandedListAddress = (String) getChild(groupPosition, 3);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.cartview_child_item, parent, false);

            EditText editName = (EditText) convertView.findViewById(R.id.edit_full_name_cartview);
            EditText editEmail = (EditText) convertView.findViewById(R.id.edit_email_cartview);
            EditText editPhone = (EditText) convertView.findViewById(R.id.edit_phone_cartview);
            EditText editAddress = (EditText) convertView.findViewById(R.id.edit_delivery_location_cartview);

            editName.setText(expandedListName);
            editEmail.setText(expandedListEmail);
            editPhone.setText(expandedListPhone);
            editAddress.setText(expandedListAddress);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
