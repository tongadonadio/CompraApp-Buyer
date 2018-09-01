package compraapp.appbuyers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class NewPublication extends AppCompatActivity {

    ImageView img;
    EditText title;
    EditText description;
    EditText price;
    Spinner stateItem;
    Spinner delivery;
    EditText min;
    EditText max;

    Button btnAddPublication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);

        this.btnAddPublication = (Button)findViewById(R.id.btnSavePublication);

        this.img = (ImageView)findViewById(R.id.imgProduct);
        this.title = (EditText)findViewById(R.id.editTitleNewPublication);
        this.description = (EditText)findViewById(R.id.editDescriptionItem);
        this.price = (EditText)findViewById(R.id.editPrice);
        this.stateItem = (Spinner) findViewById(R.id.spinnerStateItemNewPublication);
        this.delivery = (Spinner) findViewById(R.id.spinnerDeliveryNewPublication);
        this.min = (EditText) findViewById(R.id.editMinNewPublication);
        this.max = (EditText) findViewById(R.id.editMaxNewPublication);

        btnAddPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonObjRequest();
            }
        });

        loadSpinnerDelivery();
        loadStateItemDelivery();
    }

    private void loadSpinnerDelivery(){
        Spinner dropdown = findViewById(R.id.spinnerDeliveryNewPublication);
        String[] items = Utils.delivery;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }
    private void loadStateItemDelivery(){
        Spinner dropdown = findViewById(R.id.spinnerStateItemNewPublication);
        String[] items = Utils.item_states;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }


    private void useResponse(JSONObject response){
        try {
            int id = response.getInt("Id");
            if(id>0) {

                setResult(RESULT_OK, null);
                showOkMsg("Ok");
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


    private void makeJsonObjRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("Price", price.getText().toString());
        postParam.put("Description", title.getText().toString());
        postParam.put("DescriptionItem", description.getText().toString());
        postParam.put("IdBuyer",Utils.buyer_id + "");
        postParam.put("Buyer_Id",Utils.buyer_id + "");
        postParam.put("StateItem",stateItem.getSelectedItemPosition()+"");
        postParam.put("DeliveryItem",delivery.getSelectedItemPosition()+"");
        postParam.put("PriceMinItem",min.getText()+"");
        postParam.put("PriceMaxItem",max.getText()+"");
        postParam.put("State","0");

        String url = Utils.SERVER_URL + "/publication";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        useResponse(response);
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
}
