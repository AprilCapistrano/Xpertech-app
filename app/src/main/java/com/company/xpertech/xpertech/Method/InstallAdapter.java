package com.company.xpertech.xpertech.Method;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.xpertech.xpertech.R;

import java.util.ArrayList;

/**
 * Created by Skylar Gail on 8/18/2018.
 */

public class InstallAdapter extends BaseAdapter {
    ArrayList<Sub_Manual> subManual = new ArrayList<Sub_Manual>();
    LayoutInflater inflater;

    public InstallAdapter(ArrayList<Sub_Manual> subManual, Context contex) {
        this.subManual = subManual;
        this.inflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subManual.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sub_Manual sub_manual = subManual.get(position);

        View view = inflater.inflate(R.layout.fragment_sub_install_image, parent, false);
        TextView name = (TextView) view.findViewById(R.id.sub_install_name);
        name.setText(sub_manual.getName());
        if(sub_manual.getImage() != 0){
            ImageView image = (ImageView) view.findViewById(R.id.sub_install_image);
            image.setImageResource(sub_manual.getImage());
        }
        return view;
    }
}
