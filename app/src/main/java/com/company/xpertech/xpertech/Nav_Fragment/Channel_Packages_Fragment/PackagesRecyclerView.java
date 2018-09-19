package com.company.xpertech.xpertech.Nav_Fragment.Channel_Packages_Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.company.xpertech.xpertech.Method.Packages;
import com.company.xpertech.xpertech.R;

import java.util.List;

/**
 * Process the data to be displayed on the list.
 * This method was calle din Packages Fragment
 *
 * Refer on the link for more information about recycler view:
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

public class PackagesRecyclerView extends RecyclerView.Adapter<PackagesRecyclerView.ViewHolder> implements ChannelFragment.OnFragmentInteractionListener{

    private final List<Packages> mPackages;
    private final PackagesFragment.OnListFragmentInteractionListener mListener;
    private Context ctx;
    FragmentActivity activity;

    public PackagesRecyclerView(List<Packages> mPackages, PackagesFragment.OnListFragmentInteractionListener listener) {
        this.mPackages = mPackages;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx = parent.getContext())
                .inflate(R.layout.fragment_packages, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PackagesRecyclerView.ViewHolder holder, final int position) {
        holder.mTitleView.setText(mPackages.get(position).getTitle());
        holder.mChannelView.setText(mPackages.get(position).getNumOfChannel());
        holder.mButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = (FragmentActivity) ctx;
                Bundle bundle = new Bundle();
                bundle.putInt("package",(position+1));
                ChannelFragment channelf = new ChannelFragment();
                channelf.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, channelf).addToBackStack("tag").commit();

                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPackages.size();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mTitleView;
        public final TextView mChannelView;
        public final Button mButtonView;
        public Packages mItem;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTitleView = (TextView) itemView.findViewById(R.id.package_title);
            mChannelView = (TextView) itemView.findViewById(R.id.package_channels);
            mButtonView = (Button) itemView.findViewById(R.id.btn_learnmore);
        }
    }
}
