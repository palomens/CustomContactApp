package com.abarska.customcontactapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.abarska.customcontactapp.BitmapUtils.getRoundedCornerBitmap;

/**
 * Created by Dell I5 on 28.06.2017.
 */

class ContactAdapter extends ArrayAdapter<Contact> {

    private final int ROUNDED_CORNERS_VALUE_PIXELS = 10;

    ContactAdapter(@NonNull Context context, @NonNull List<Contact> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
        }

        Contact currentContact = getItem(position);

        TextView tvContactName = (TextView) convertView.findViewById(R.id.tvContact);
        tvContactName.setText(currentContact.getName() + " " + currentContact.getSurname());

        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
        Bitmap thumbnaiPic = getRoundedCornerBitmap(currentContact.getThumbnailPic(), ROUNDED_CORNERS_VALUE_PIXELS);
        ivAvatar.setImageBitmap(thumbnaiPic);

        return convertView;
    }
}
