package com.mapas.sdm.seminariomapas;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by miquel on 9/03/15.
 */
public class CustomAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private HashMap<Marker,City> markers;

    public CustomAdapter(Context context) {
        this.context = context;
        markers = ((MainActivity)context).getMarkerCities();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file info_window_layout
        View v = ((MainActivity)context).getLayoutInflater().inflate(R.layout.layout_infowindow, null);

        // Getting the position from the marker
       City c =null;
        c = markers.get(marker);


        // Getting reference to the TextView to set latitude
        TextView tvcity = (TextView) v.findViewById(R.id.tv_city);

        // Getting reference to the TextView to set longitude
        ImageView  imcity = (ImageView) v.findViewById(R.id.iv_city);

        // Setting the latitude
        if(c!=null)
            tvcity.setText(c.getName());
        else
            tvcity.setText(marker.getTitle());

        // Setting the longitude
        if(c!=null)
            imcity.setImageResource(c.getIcon_id());
        else
            imcity.setImageResource(R.mipmap.ic_launcher);


        // Returning the view containing InfoWindow contents
        return v;

    }
}
