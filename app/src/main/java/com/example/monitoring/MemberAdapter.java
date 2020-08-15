package com.example.monitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by User on 3/14/2017.
 */

public class MemberAdapter extends ArrayAdapter<Member> {

    private static final String TAG = "MemberListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView query;
        TextView temp;
        TextView turb;
        TextView ph;
    }

    /**
     * Default constructor for the MemberListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public MemberAdapter(Context context, int resource, ArrayList<Member> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the Members information
        String query = getItem(position).getQuery();
        String temp = getItem(position).getTemp();
        String turb = getItem(position).getTurb();
        String ph = getItem(position).getPh();

        //Create the Member object with the information
        Member Member = new Member(query,temp,turb,ph);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.query = (TextView) convertView.findViewById(R.id.textView1);
            holder.temp = (TextView) convertView.findViewById(R.id.textView2);
            holder.turb = (TextView) convertView.findViewById(R.id.textView3);
            holder.ph = (TextView) convertView.findViewById(R.id.textView4);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.query.setText("Date: "+Member.getQuery());
        holder.temp.setText("Temp: "+Member.getTemp());
        holder.turb.setText("Turb: "+Member.getTurb());
        holder.ph.setText("  PH: "+Member.getPh());


        return convertView;
    }
}