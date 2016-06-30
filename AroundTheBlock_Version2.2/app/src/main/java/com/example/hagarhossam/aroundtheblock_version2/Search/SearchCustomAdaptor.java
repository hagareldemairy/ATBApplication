package com.example.hagarhossam.aroundtheblock_version2.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hagarhossam.aroundtheblock_version2.R;

import java.util.ArrayList;

/**
 * Created by Hagar Hossam on 15-Jun-16.
 */
public class SearchCustomAdaptor extends ArrayAdapter
{
    public SearchCustomAdaptor(Context context, ArrayList<ArrayList<String>> list)
    {
        super(context, R.layout.search_custom_row, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater buckysInflator  = LayoutInflater.from(getContext());
        View customView = buckysInflator.inflate(R.layout.search_custom_row, parent, false);

        ArrayList<String> item = (ArrayList<String>) getItem(position);


        TextView placename = (TextView) customView.findViewById(R.id.placeName);



        for(int i=0;i<item.size();i++)
        {
            System.out.println("yarab " +item.get(i));

            placename.setText(item.get(i)); //will iterate over array already defined
            i+=2;

        }

        return customView;
    }
}
