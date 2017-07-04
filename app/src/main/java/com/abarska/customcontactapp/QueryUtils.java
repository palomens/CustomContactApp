package com.abarska.customcontactapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dell I5 on 28.06.2017.
 */

class QueryUtils {

    private QueryUtils() {
    }

    private static final String LOG_TAG = "QueryUtils";

    static Contact fetchNewContactInfo(Context context, String stringUrl) {

        if (TextUtils.isEmpty(stringUrl)) return null;

        URL queryUrl = buildUrl(context, stringUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(queryUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        if (TextUtils.isEmpty(jsonResponse)) return null;

        return extractContactFromJson(jsonResponse);
    }

    private static Contact extractContactFromJson(String json) {

        if (TextUtils.isEmpty(json)) return null;

        Contact currentContact = null;

        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray resultsArray = rootObject.getJSONArray("results");
            JSONObject contactObject = resultsArray.getJSONObject(0);

            JSONObject nameObject = contactObject.getJSONObject("name");

            String firstName = toTitleCase(nameObject.getString("first")).trim();
            String lastName = toTitleCase(nameObject.getString("last")).trim();

            JSONObject loginObject = contactObject.getJSONObject("login");
            String password = loginObject.getString("password");

            String birthday = contactObject.getString("dob");
            int separatorIndex = birthday.indexOf(" ");
            birthday = birthday.substring(0, separatorIndex);

            String phone = contactObject.getString("phone");
            String email = contactObject.getString("email");

            JSONObject addressObject = contactObject.getJSONObject("location");
            String address = toTitleCase(addressObject.getString("street")) + "\n"
                    + toTitleCase(addressObject.getString("city")) + "\n"
                    + toTitleCase(addressObject.getString("state")) + "\n"
                    + addressObject.getString("postcode");

            JSONObject pictureObject = contactObject.getJSONObject("picture");

            String stringLargePicUrl = pictureObject.getString("large");
            String stringThumbnailPicUrl = pictureObject.getString("thumbnail");

            Bitmap largePic = getBitmapPicture(stringLargePicUrl);
            Bitmap thumbnailPic = getBitmapPicture(stringThumbnailPicUrl);

            currentContact = new Contact(firstName, lastName, password, birthday, phone, email, address, largePic, thumbnailPic);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Unable to parse JSON: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to close input stream: " + e.getMessage());
        }

        return currentContact;
    }

    private static Bitmap getBitmapPicture(String stringPicUrl) throws IOException {

        Bitmap resultBitmap = null;
        if (stringPicUrl == null) return resultBitmap;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        URL queryUrl = null;

        try {
            queryUrl = new URL(stringPicUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException", e);
        }

        try {
            urlConnection = (HttpURLConnection) queryUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout((int) TimeUnit.SECONDS.toMillis(10));
            urlConnection.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(15));
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                resultBitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Unexpected response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException caught: " + e.getMessage());

        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return resultBitmap;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout((int) TimeUnit.SECONDS.toMillis(10));
            urlConnection.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(15));
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Unexpected response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException caught: " + e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    private static URL buildUrl(Context context, String baseUrl) {

        //URL value should be https://randomuser.me/api?format=json&inc=name,location,email,login,dob,phone,picture

        Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
        String fieldsToInclude = getArrayValuesFromResources(context);

        builder.appendPath("api")
                .appendQueryParameter(context.getString(R.string.key_query_param_format), context.getString(R.string.value_query_param_format))
                .appendQueryParameter(context.getString(R.string.key_query_param_included_fields), fieldsToInclude);

        URL queryUrl = null;

        try {
            queryUrl = new URL(builder.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException", e);
        }

        return queryUrl;
    }

    private static String getArrayValuesFromResources(Context context) {
        String[] includedFields = context.getResources().getStringArray(R.array.array_included_fields);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < includedFields.length; i++) {
            if (i != includedFields.length - 1) {
                sb.append(includedFields[i]).append(",");
            } else {
                sb.append(includedFields[i]);
            }
        }
        return sb.toString();
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        if (inputStream == null) return null;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line = reader.readLine();
        StringBuilder output = new StringBuilder();

        while (line != null) {
            output.append(line);
            line = reader.readLine();
        }
        return output.toString();
    }

    private static String toTitleCase(String input) {
        String[] stringArray = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s : stringArray) {
            sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
        }
        return sb.toString();
    }
}
