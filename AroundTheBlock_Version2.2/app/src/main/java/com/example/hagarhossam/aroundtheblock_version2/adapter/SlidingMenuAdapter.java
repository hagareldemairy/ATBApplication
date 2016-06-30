package com.example.hagarhossam.aroundtheblock_version2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hagarhossam.aroundtheblock_version2.R;
import com.example.hagarhossam.aroundtheblock_version2.model.categorySlideMenu;


import java.util.List;

/**
 * Created by Hagar Hossam on 21-Mar-16.
 */
public class SlidingMenuAdapter extends BaseAdapter {

    private Context context;
    private List<categorySlideMenu> firstCategory;


    public SlidingMenuAdapter(Context context, List<categorySlideMenu> firstCategory) {
        this.context = context;
        this.firstCategory = firstCategory;
    }


    @Override
    public int getCount() {
        return firstCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return firstCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v= View.inflate(context, R.layout.category_sliding_menu, null);
        //ImageView img = (ImageView)v.findViewById(R.id.category_image);
        TextView tv = (TextView)v.findViewById(R.id.category_title);
        categorySlideMenu category = firstCategory.get(position);
        //img.setImageResource(category.getImageId());
        tv.setText(category.getTitle());
        return v;
    }
}
