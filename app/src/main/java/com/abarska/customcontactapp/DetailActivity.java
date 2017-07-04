package com.abarska.customcontactapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import static com.abarska.customcontactapp.BitmapUtils.getRoundedCornerBitmap;

/**
 * Created by Dell I5 on 28.06.2017.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DetailActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent.hasExtra(MainActivity.INTENT_KEY_FIRST_NAME)) {
            ((TextView) findViewById(R.id.tvNameValue)).setText(intent.getStringExtra(MainActivity.INTENT_KEY_FIRST_NAME));
        }
        if (intent.hasExtra(MainActivity.INTENT_KEY_SECOND_NAME)) {
            ((TextView) findViewById(R.id.tvSecondNameValue)).setText(intent.getStringExtra(MainActivity.INTENT_KEY_SECOND_NAME));
        }
        if (intent.hasExtra(MainActivity.INTENT_KEY_PASSWORD)) {
            ((TextView) findViewById(R.id.tvPasswordValue)).setText(intent.getStringExtra(MainActivity.INTENT_KEY_PASSWORD));
        }
        if (intent.hasExtra(MainActivity.INTENT_KEY_BIRTHDAY)) {
            ((TextView) findViewById(R.id.tvBirthdayValue)).setText(intent.getStringExtra(MainActivity.INTENT_KEY_BIRTHDAY));
        }
        if (intent.hasExtra(MainActivity.INTENT_KEY_PHONE)) {
            ((TextView) findViewById(R.id.tvTelValue)).setText(intent.getStringExtra(MainActivity.INTENT_KEY_PHONE));
        }
        if (intent.hasExtra(MainActivity.INTENT_KEY_EMAIL)) {
            ((TextView) findViewById(R.id.tvEmailValue)).setText(intent.getStringExtra(MainActivity.INTENT_KEY_EMAIL));
        }
        if (intent.hasExtra(MainActivity.INTENT_KEY_ADDRESS)) {
            ((TextView) findViewById(R.id.tvAddressValue)).setText(intent.getStringExtra(MainActivity.INTENT_KEY_ADDRESS));
        }
        if (intent.hasExtra(MainActivity.INTENT_KEY_LARGE_PICTURE)) {
            int roundedCornersValue = 50;
            Bitmap b = getRoundedCornerBitmap((Bitmap) intent.getParcelableExtra(MainActivity.INTENT_KEY_LARGE_PICTURE),
                    roundedCornersValue);
            ((ImageView) findViewById(R.id.ivBigPic)).setImageBitmap(b);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            if (!getIntent().hasExtra(MainActivity.INTENT_KEY_LIST_POSITION)) finish();
            int position = getIntent().getIntExtra(MainActivity.INTENT_KEY_LIST_POSITION, -1);
            MainActivity.setPositionToRemove(position);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
