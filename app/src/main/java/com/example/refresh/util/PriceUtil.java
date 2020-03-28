package com.example.refresh.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceUtil {

    public static void getItemPrice(final VolleyCallback callback, Context c, String itemName, String quantity) {
        Log.d("API autocomplete start", itemName);


        RequestQueue queue = Volley.newRequestQueue(c);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.spoonacular.com")
                .appendPath("food")
                .appendPath("ingredients")
                .appendPath("autocomplete")
                .appendQueryParameter("apiKey", "d6da8e8d7bb44480a6ecdb3640bce9d6")
                .appendQueryParameter("query", itemName)
                .appendQueryParameter("number", "1")
                .appendQueryParameter("metaInformation", "true");
        String url = builder.build().toString();

        Log.d("API autocomplete url", url);

        Response.Listener<String> responseListener = response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("API autocomplete", jsonObject.getString("name") + jsonObject.getString("id"));
                    String id = jsonObject.getString("id");

                    getPrice(result -> {
                        callback.onSuccess(result);
                        Log.d("MSRP", result);
                    }, c, id, quantity);


                }
            } catch (JSONException e) {
                Log.e("Autocomplete Request Failed", e.getMessage() != null ? e.getMessage() : "");
            }
        };

        Response.ErrorListener errorListener = error -> Log.d("API autocomplete error", error.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener);

        queue.add(stringRequest);

        Log.d("API autocomplete end", itemName);

    }


    private static void getPrice(final VolleyCallback callback, Context c, String id, String quantity) {
        Log.d("API ingredients start", id);

        RequestQueue queue = Volley.newRequestQueue(c);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.spoonacular.com")
                .appendPath("food")
                .appendPath("ingredients")
                .appendPath(id)
                .appendPath("information")
                .appendQueryParameter("apiKey", "d6da8e8d7bb44480a6ecdb3640bce9d6")
                .appendQueryParameter("amount", quantity);
        String url = builder.build().toString();

        Log.d("API ingredients url", url);

        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("API ingredients", jsonObject.getString("name") + jsonObject.getJSONObject("estimatedCost").getString("value"));
                int price = (int) Math.round(jsonObject.getJSONObject("estimatedCost").getInt("value") * 1.35);
                NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
                String sPrice = n.format(price / 100.0);
                callback.onSuccess(sPrice);

            } catch (JSONException e) {
                Log.e("Ingredient Info Request Failed", e.getMessage() != null ? e.getMessage() : "");
            }
        };

        Response.ErrorListener errorListener = error -> Log.d("API ingredients error", error.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener);

        queue.add(stringRequest);

        Log.d("API ingredients end", id);

    }
}
