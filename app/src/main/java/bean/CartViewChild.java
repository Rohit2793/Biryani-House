package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rohit on 8/22/16.
 */
public class CartViewChild {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> childList = new ArrayList<>();
        childList.add("Rohit Bhardwaj");
        childList.add("rohit@gmail.com");
        childList.add("9963322563");
        childList.add("Quantum Towers,Malad west");

        expandableListDetail.put("Your Profile",childList);

        return expandableListDetail;
    }
}
