package compraapp.appbuyers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import compraapp.appbuyers.request.Utils;

public class ShowOffer extends AppCompatActivity {

    ImageView img;
    int id;
    TextView seller;
    TextView description;
    TextView price;
    TextView delivery;
    Button btnAceptOffer;
    Button btnRejectOffer;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);

        this.btnAceptOffer = (Button)findViewById(R.id.btnAceptOffer);
        this.btnRejectOffer = (Button)findViewById(R.id.btnRejectOffer);

        this.img = (ImageView)findViewById(R.id.imgProduct);
        this.seller = (TextView)findViewById(R.id.txtVSeller);
        this.description = (TextView)findViewById(R.id.txtVDescriptionProduct);
        this.price = (TextView)findViewById(R.id.txtVPrice);
        this.delivery = (TextView)findViewById(R.id.txtVDelivery);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        loadBundle(b);
        showOrHideButtons();
    }

    private void loadBundle(Bundle b){
        if(b != null){
            this.id = Integer.parseInt(b.getString("ID"));
            this.description.setText(b.getString("DESCRIPTION"));
            this.seller.setText(b.getString("SELLER"));
            this.price.setText("USD " + b.getString("PRICE"));
            this.delivery.setText(Utils.delivery[Integer.parseInt(b.getString("DELIVERY"))]);
            this.status = b.getInt("STATUS");

            String image = b.getString("IMAGEBASE64");

            try {
                if (image!= "null" && image.length() > 0) {
                    image = image.replace("data:image/jpeg;base64,","");
                    byte[] decodedString = Base64.decode(image, Base64.CRLF);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    img.setImageBitmap(decodedByte);
                }
            }
            catch (Exception e){
                img.setImageResource(R.drawable.cube_green);
                this.showErrorMsg(e.getMessage());
            }
        }
    }

    private void showOrHideButtons(){
        if(this.status==Utils.OFFER_OPEN) {
            btnAceptOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(v.getContext(), ShowOffer.class);
                    //startActivity(intent);
                    makeAceptRequest();
                }
            });

            btnRejectOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(v.getContext(), ShowOffer.class);
                    //startActivity(intent);
                    makeRejectRequest();
                }
            });
            this.btnAceptOffer.setVisibility(View.VISIBLE);
            this.btnRejectOffer.setVisibility(View.VISIBLE);
        }
        else {
            this.btnAceptOffer.setVisibility(View.INVISIBLE);
            this.btnRejectOffer.setVisibility(View.INVISIBLE);
        }
    }

    private void makeAceptRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> postParam= new HashMap<String, String>();
        /*postParam.put("Price", price.getText().toString());
        postParam.put("Description", description.getText().toString());
        postParam.put("IdBuyer",Utils.buyer_id + "");
        postParam.put("State",0+"");*/

        String url = Utils.SERVER_URL + "/offer/" + this.id + "?action=" + Utils.OFFER_ACTION_ACCEPTED;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        useResponseAccept(response);
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


    private void makeRejectRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> postParam= new HashMap<String, String>();
        /*postParam.put("Price", price.getText().toString());
        postParam.put("Description", description.getText().toString());
        postParam.put("IdBuyer",Utils.buyer_id + "");
        postParam.put("State",0+"");*/

        String url = Utils.SERVER_URL + "/offer/" + this.id + "?action=" + Utils.OFFER_ACTION_REJECTED;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        useResponseReject(response);
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


    private void useResponseAccept(JSONObject response){
        try {
            int state = response.getInt("State");
            if(state == Utils.OFFER_ACTION_ACCEPTED) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("ACTION", Utils.OFFER_ACTION_ACCEPTED);

                setResult(RESULT_OK,resultIntent);
                showOkMsg("Ok aceptada");

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


    private void useResponseReject(JSONObject response){
        try {
            int state = response.getInt("State");
            if(state == Utils.OFFER_ACTION_REJECTED) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("ACTION", Utils.OFFER_ACTION_REJECTED);

                showOkMsg("Oferta rechazada");
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
