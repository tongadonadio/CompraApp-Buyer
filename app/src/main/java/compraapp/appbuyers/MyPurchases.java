package compraapp.appbuyers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import compraapp.appbuyers.entities.Offer;
import compraapp.appbuyers.request.Utils;

public class MyPurchases extends AppCompatActivity {
    ListView offersList;
    String[][] offers;
    int[] imgs;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchasses);
        refreshList();


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipPurchases);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
    }


    private void refreshList(){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = Utils.SERVER_URL + "/offer?status=" + Utils.OFFER_ACTION_ACCEPTED + "&idBuyer=" + Utils.buyer_id;
            StringRequest jsonObjReq = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            useResponseGetList(response);
                            mSwipeRefreshLayout.setRefreshing(false);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showErrorMsg("onErrorResponse: " + error.getMessage());
                    mSwipeRefreshLayout.setRefreshing(false);
                        /*refreshListFromDataBase();*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            queue.add(jsonObjReq);
        }
        catch (Exception e)
        {
            showErrorMsg(e.getMessage());
            mSwipeRefreshLayout.setRefreshing(false);

        }
    }

    private void useResponseGetList(String response){
        try {
            //JSONArray json = new JSONArray(response);
            Gson gson = new GsonBuilder().create();
            Offer[] objects = gson.fromJson(response.toString(), Offer[].class);

            imgs = new int[objects.length];
            offers = new String[objects.length][10];

            int i = 0;
            for (Offer offer: objects) {
                offers[i][0] = offer.getId()+"";
                offers[i][1] = offer.getDescriptionItem();
                offers[i][2] = offer.getIdSeller()+"";
                offers[i][3] = offer.getPriceItem()+"";
                offers[i][4] = offer.getDeliveryItem()+"";
                offers[i][5] = offer.getSeller().getId()+"";
                offers[i][6] = offer.getSeller().getName()+"";
                offers[i][7] = offer.getDeliveryItem()+"";
                offers[i][8] = offer.getPhotoItem()+"";
                offers[i][9] = offer.getState()+"";

                imgs[i] = R.drawable.cube_green;
                i++;
            }

            refreshAdapter();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("Error request: " + e.getMessage());
        }
    }


    private void refreshAdapter(){
        offersList = (ListView) findViewById(R.id.lstVOffersMyPurchasses);
        offersList.setAdapter(new AdaptadorListPurchasse(this, offers, imgs));
        offersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productDetail = new Intent(view.getContext(), ShowPurchase.class);
                productDetail.putExtra("ID", offers[position][0]);
                productDetail.putExtra("DESCRIPTION", offers[position][1]);
                productDetail.putExtra("SELLER", offers[position][6]);
                productDetail.putExtra("PRICE", offers[position][3]);
                productDetail.putExtra("DELIVERY", offers[position][4]);
                productDetail.putExtra("IMAGEBASE64", offers[position][8]);
                productDetail.putExtra("STATUS", offers[position][9]);

                productDetail.putExtra("IMG", imgs[position]);
                startActivityForResult(productDetail,1);
            }
        });
    }


    private void showErrorMsg(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void showOkMsg(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
