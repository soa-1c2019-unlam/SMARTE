package com.example.smarteapp2;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdaptar extends ArrayAdapter<Perfil> {

    private Context mContext;
    private int id;
    private List<Perfil> items ;

    public CustomListAdaptar(Context context, int textViewResourceId , List<Perfil> list )
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);

        if(items.get(position) != null )
        {
            text.setTextColor(Color.BLACK);
            text.setText(items.get(position).toString());
            text.setBackgroundColor(Color.WHITE);
            int color = Color.argb( 100, 210, 210, 210 );
            text.setBackgroundColor( color );

        }

        return mView;
    }

}