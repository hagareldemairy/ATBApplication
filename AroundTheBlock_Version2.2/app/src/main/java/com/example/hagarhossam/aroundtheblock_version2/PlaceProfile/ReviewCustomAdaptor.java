package com.example.hagarhossam.aroundtheblock_version2.PlaceProfile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hagarhossam.aroundtheblock_version2.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewCustomAdaptor extends ArrayAdapter
{
    public ReviewCustomAdaptor(Context context, ArrayList<ArrayList<String>> list)
    {
        super(context, R.layout.custom_row, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater buckysInflator  = LayoutInflater.from(getContext());
        View customView = buckysInflator.inflate(R.layout.custom_row, parent, false);

        ArrayList<String> item = (ArrayList<String>) getItem(position);


        TextView emailText = (TextView) customView.findViewById(R.id.emailText);
        TextView dateText = (TextView) customView.findViewById(R.id.dateText);
        TextView reviewText = (TextView) customView.findViewById(R.id.reviewText);
        RatingBar place_rating= (RatingBar)customView.findViewById(R.id.place_rating);


        for(int i=0;i<item.size();i++)
        {
            System.out.println("yarab " + item.get(i));

            emailText.setText(item.get(i)); //will iterate over array already defined
            dateText.setText(item.get(i+1));
            reviewText.setText(item.get(i + 2));
            place_rating.setRating(Float.parseFloat(item.get(i + 3)));
            i+=3;

        }


        return customView;
    }
}
