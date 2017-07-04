package com.abarska.customcontactapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Contact> {

    private static final int CONTACT_LOADER_ID = 852;

    private static int sPositionToRemove = -1;

    private ProgressBar mLoadingIndicator;
    private TextView mEmptyStateView;
    private ListView mListViewContacts;

    public static final String INTENT_KEY_FIRST_NAME = "key-first-name";
    public static final String INTENT_KEY_SECOND_NAME = "key-second-name";
    public static final String INTENT_KEY_PASSWORD = "key-password";
    public static final String INTENT_KEY_BIRTHDAY = "key-birthday";
    public static final String INTENT_KEY_PHONE = "key-phone";
    public static final String INTENT_KEY_EMAIL = "key-email";
    public static final String INTENT_KEY_ADDRESS = "key-address";
    public static final String INTENT_KEY_LARGE_PICTURE = "key-large-picture";
    public static final String INTENT_KEY_LIST_POSITION = "key_list_position";

    private ArrayList<Contact> mContacts;
    private ContactAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mContacts = new ArrayList<>();
        mAdapter = new ContactAdapter(this, mContacts);

        mEmptyStateView = (TextView) findViewById(R.id.tv_empty_state);
        mEmptyStateView.setText(getString(R.string.empty_state_string) + "\n" + getString(R.string.empty_state_advise));

        mListViewContacts = (ListView) findViewById(R.id.lv_contacts);
        mListViewContacts.setEmptyView(mEmptyStateView);
        mListViewContacts.setAdapter(mAdapter);

        mListViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Contact currentContact = mContacts.get(position);

                intent.putExtra(INTENT_KEY_FIRST_NAME, currentContact.getName());
                intent.putExtra(INTENT_KEY_SECOND_NAME, currentContact.getSurname());
                intent.putExtra(INTENT_KEY_PASSWORD, currentContact.getPassword());
                intent.putExtra(INTENT_KEY_BIRTHDAY, currentContact.getBirthday());
                intent.putExtra(INTENT_KEY_PHONE, currentContact.getTel());
                intent.putExtra(INTENT_KEY_EMAIL, currentContact.getEmail());
                intent.putExtra(INTENT_KEY_ADDRESS, currentContact.getAddress());
                intent.putExtra(INTENT_KEY_ADDRESS, currentContact.getAddress());
                intent.putExtra(INTENT_KEY_LARGE_PICTURE, currentContact.getLargePic());
                intent.putExtra(INTENT_KEY_LIST_POSITION, position);

                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected()) {
                    if (mContacts.isEmpty())
                        mEmptyStateView.setText(getString(R.string.no_internet_connection_advise));
                    else
                        Toast.makeText(MainActivity.this, getString(R.string.no_internet_connection_advise), Toast.LENGTH_SHORT).show();
                } else {
                    getSupportLoaderManager().initLoader(CONTACT_LOADER_ID, null, MainActivity.this);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sPositionToRemove != -1) {
            mAdapter.remove(mContacts.get(sPositionToRemove));
            sPositionToRemove = -1;
        }
    }

    @Override
    public Loader<Contact> onCreateLoader(int id, Bundle args) {
        mEmptyStateView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        return new ContactLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Contact> loader, Contact contact) {

        mLoadingIndicator.setVisibility(View.GONE);

        if (contact == null) {
            String noDataMessage = getString(R.string.no_data_from_server_state) + "\n" + getString(R.string.no_data_from_server_advise);
            Toast.makeText(this, noDataMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        mAdapter.add(contact);
        mListViewContacts.setSelection(mContacts.size() - 1);
        getSupportLoaderManager().destroyLoader(CONTACT_LOADER_ID);
    }

    @Override
    public void onLoaderReset(Loader<Contact> loader) {
    }

    public static void setPositionToRemove(int position) {
        sPositionToRemove = position;
    }
}
