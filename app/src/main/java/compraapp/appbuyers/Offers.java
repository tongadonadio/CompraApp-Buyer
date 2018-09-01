package compraapp.appbuyers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import compraapp.appbuyers.entities.Offer;
import compraapp.appbuyers.request.Utils;

public class Offers extends AppCompatActivity {
    ListView offersList;
    TextView txtSubTitle;
    int idPublication;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String[][] offers;

    int[] imgs = {R.drawable.computadora, R.drawable.computadora, R.drawable.computadora, R.drawable.computadora};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        this.txtSubTitle = (TextView) findViewById(R.id.txtSubTitle);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            this.idPublication = Integer.parseInt(b.getString("ID"));
            this.txtSubTitle.setText(b.getString("TITLE"));
            refreshList();
        } else{
            showErrorMsg("Error al abrir ofertas. Falta ID de publicación");
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipOffers);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_offers, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finishPublicationMenu:
                makeFinishPublicationRequest();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void makeFinishPublicationRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> postParam= new HashMap<String, String>();

        String url = Utils.SERVER_URL + "/publication/" + this.idPublication + "?action=" + Utils.PUBLICATION_CLOSE_BY_USER;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        useResponseFinishPublication(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMsg(error.getMessage());
            }
        })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(jsonObjReq);

    }

    private void useResponseFinishPublication(JSONObject response){
        try {
            int state = response.getInt("State");
            if(state == Utils.PUBLICATION_CLOSE_BY_USER) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("ACTION", Utils.PUBLICATION_CLOSE_BY_USER);

                setResult(RESULT_OK,resultIntent);
                showOkMsg("Publicación finalizada");

                Intent intent = new Intent(this.getBaseContext(), Home.class);
                startActivity(intent);

                finish();
            }
            else{
                showErrorMsg("Error al guardar en el servidor.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMsg("Error json: " + e.getMessage());
        }
    }



    private void refreshList(){

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Utils.SERVER_URL + "/publication/" + this.idPublication + "/offer";
        StringRequest jsonObjReq = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        useResponse(response);
                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMsg("onErrorResponse: " + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonObjReq);

    }

    private void useResponse(String response){
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
        offersList = (ListView) findViewById(R.id.lstVOffers);
        offersList.setAdapter(new AdaptadorListOffer(this, offers, imgs));
        offersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productDetail = new Intent(view.getContext(), ShowOffer.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(data.getStringExtra("ACTION").compareTo(Utils.OFFER_ACTION_ACCEPTED+"") == 0)
                this.finish();
            else
                this.refreshList();
        }
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
