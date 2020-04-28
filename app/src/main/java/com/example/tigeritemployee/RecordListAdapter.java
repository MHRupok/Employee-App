package com.example.tigeritemployee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> recordList;

    public RecordListAdapter(Context context, int layout, ArrayList<Model> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView textName,textGender,textAge;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder viewHolder = new ViewHolder();

        if(row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            viewHolder.textName = row.findViewById(R.id.txtName);
            viewHolder.textAge = row.findViewById(R.id.txtAge);
            viewHolder.textGender = row.findViewById(R.id.txtGender);
            viewHolder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder)row.getTag();
        }

        Model model = recordList.get(position);
        viewHolder.textName.setText(model.getName());
        viewHolder.textGender.setText(model.getGender());
        viewHolder.textAge.setText(model.getAge());


        byte [] recordImage = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage,0,recordImage.length);
        viewHolder.imageView.setImageBitmap(bitmap);

        return row;

    }
}

