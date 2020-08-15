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

public class Billadapter  extends ArrayAdapter<Member> {
    private static final String TAG = "ConsumptionListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView query;
        TextView con;

    }

    /**
     * Default constructor for the MemberListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public Billadapter(Context context, int resource, ArrayList<Member> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the Members information
        String query = getItem(position).getQuery();
        String con = getItem(position).getConsum();


        //Create the Member object with the information
        Member Member = new Member(query,con);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        Billadapter.ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new Billadapter.ViewHolder();
            holder.query = (TextView) convertView.findViewById(R.id.list2Text);
            holder.con = (TextView) convertView.findViewById(R.id.list2Text1);


            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (Billadapter.ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.query.setText("Date: "+Member.getQuery());
        holder.con.setText("Total Cost: Rs "+(Double.valueOf(Member.getConsum())*0.56));



        return convertView;
    }
}
