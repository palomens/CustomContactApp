package com.abarska.customcontactapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Dell I5 on 30.06.2017.
 */

class ContactLoader extends AsyncTaskLoader<Contact> {

    private static final String BASE_NEW_CONTACT_REQUEST_URL = "https://randomuser.me/";

    ContactLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Contact loadInBackground() {
        return QueryUtils.fetchNewContactInfo(getContext(), BASE_NEW_CONTACT_REQUEST_URL);
    }
}
