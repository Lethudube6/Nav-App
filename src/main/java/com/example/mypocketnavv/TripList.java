package com.example.mypocketnavv;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TripList extends ArrayAdapter<TripHandler> {

    private final Activity context;
    private final List<TripHandler> tripList;

    public TripList(Activity context,List<TripHandler>tripList){
        super(context,R.layout.trip_list_layout,tripList);
        this.context=context;
        this.tripList=tripList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= context.getLayoutInflater();

        View listViewItem=layoutInflater.inflate(R.layout.trip_list_layout,null,true);
        TextView datetime= listViewItem.findViewById(R.id.tv_datetime);
        TextView depAddress1= listViewItem.findViewById(R.id.tv_depAddress1);
        TextView depAddress2= listViewItem.findViewById(R.id.tv_depAddress3);
        TextView desAddress1= listViewItem.findViewById(R.id.tv_desAddress1);
        TextView desAddress2= listViewItem.findViewById(R.id.tv_desAddress2);

        TripHandler th=tripList.get(position);
        datetime.setText(th.getDate_of_trip());
        depAddress1.setText("Departure Address: "+th.getDeparture_address1());
        depAddress2.setText("Full Address: "+th.getDeparture_address2());
        desAddress1.setText("Destination Address: "+th.getDestination_address1());
        desAddress2.setText("Full Address:"+th.getDestination_address2());

        return listViewItem;
    }
}
