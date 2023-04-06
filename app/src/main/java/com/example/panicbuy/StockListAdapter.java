package com.example.panicbuy;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by User on 3/14/2017.
 */

public class StockListAdapter extends ArrayAdapter<Stock> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {

        TextView my_id;
        TextView barcode;
        TextView description;
        TextView stockLevel;
        TextView toBuy;
    }

    /**
     * Default constructor for the StockListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public StockListAdapter(Context context, int resource, ArrayList<Stock> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String my_id = getItem(position).getMy_id();
        String barcode = getItem(position).getBarcode();
        String description = getItem(position).getDescription();
        String stocklevel = getItem(position).getStockLevel();
        String tobuy = getItem(position).getToBuy();

        //Create the stock object with the information
        Stock stock = new Stock(my_id, barcode, description, stocklevel, tobuy, "0", "","","");

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();

            holder.my_id = convertView.findViewById(R.id.textView_my_id);
            holder.barcode = convertView.findViewById(R.id.textView_barcode);
            holder.description = convertView.findViewById(R.id.textView_description);
            holder.stockLevel = convertView.findViewById(R.id.textView_stocklevel);
            holder.toBuy = convertView.findViewById(R.id.textView_toBuy);

            result = convertView;

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.my_id.setText(stock.getMy_id());
        holder.barcode.setText(stock.getBarcode());
        holder.description.setText(stock.getDescription());
        holder.stockLevel.setText(stock.getStockLevel());
        holder.toBuy.setText(stock.getToBuy());

        Activity activity = (Activity) mContext;
        View vC = activity.findViewById(R.id.checkBox2);
        boolean shopping = ((CheckBox)vC).isChecked();

        View v = (View) holder.my_id.getParent();
        if(shopping) {
            if (stock.getToBuy().equals("Y"))
                v.setBackgroundColor(0x5F00FF00);
            else
                v.setBackgroundColor(0xFFFFFFFF);
        }else    v.setBackgroundColor(0xFFFFFFFF);

        return convertView;
    }
}

























