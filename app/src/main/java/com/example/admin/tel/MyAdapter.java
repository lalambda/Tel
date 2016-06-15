package com.example.admin.tel;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yaoyafeng on 16/6/8.
 */
public class MyAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private int resource;

    public MyAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTV = (TextView) view.findViewById(R.id.list_item_name);
            viewHolder.numberTV = (TextView) view.findViewById(R.id.list_item_number);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        SpannableStringBuilder stringBuilder;
        int spanPosition = contact.getPosition();
        if (contact.isName()) {
            stringBuilder = new SpannableStringBuilder(contact.getName().toString());
            spanPosition = contact.getPosition();
            stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), spanPosition % 1000, spanPosition % 1000 + 1 + spanPosition / 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.nameTV.setText(stringBuilder);
            viewHolder.numberTV.setText(contact.getNumber());
        } else {
            stringBuilder = new SpannableStringBuilder(contact.getNumber().toString());
            stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), spanPosition % 1000, spanPosition % 1000 + 1 + spanPosition / 1000, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.nameTV.setText(contact.getName());
            viewHolder.numberTV.setText(stringBuilder);
        }
        return view;
    }

    class ViewHolder {
        TextView nameTV;
        TextView numberTV;
    }
}
