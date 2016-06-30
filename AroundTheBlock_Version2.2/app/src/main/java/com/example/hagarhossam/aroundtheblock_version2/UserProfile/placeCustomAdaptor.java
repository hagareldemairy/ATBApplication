package com.example.hagarhossam.aroundtheblock_version2.UserProfile;

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
public class placeCustomAdaptor extends ArrayAdapter
{
    public placeCustomAdaptor(Context context, ArrayList<ArrayList<String>> list)
    {
        super(context, R.layout.place_custom_row, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater buckysInflator  = LayoutInflater.from(getContext());
        View customView = buckysInflator.inflate(R.layout.place_custom_row, parent, false);

        ArrayList<String> item = (ArrayList<String>) getItem(position);


        TextView placename = (TextView) customView.findViewById(R.id.placeName);
        TextView phonenumber = (TextView) customView.findViewById(R.id.phone_number);



        for(int i=0;i<item.size();i++)
        {
            System.out.println("yarab " +item.get(i));

            placename.setText(item.get(i)); //will iterate over array already defined
            phonenumber.setText(item.get(i+1));
            i+=2;

        }

        return customView;
    }
}
