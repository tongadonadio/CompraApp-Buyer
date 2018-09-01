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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import compraapp.appbuyers.entities.Publication;
import compraapp.appbuyers.persistence.AdminPublications;
import compraapp.appbuyers.request.Utils;

public class Home extends AppCompatActivity {
    ListView productsList;
    String[][] publications;
    int[] imgs = {R.drawable.cube_green, R.drawable.peluca, R.drawable.bicicleta, R.drawable.auto};
    GoogleApiClient mGoogleApiClient;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            refreshList();

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshList();
                }
            });
        }
        catch (Exception e){
            showErrorMsg(e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPublicationMenu:
                Intent newPublication = new Intent(this, NewPublication.class);
                startActivityForResult(newPublication,1);
                return true;
            case R.id.myPurchassesMenu:
                Intent myPurchasses = new Intent(this, MyPurchases.class);
                startActivityForResult(myPurchasses,1);
                return true;
            case R.id.editProfileMenu:
                Intent editUser = new Intent(this, EditUser.class);
                startActivityForResult(editUser,1);
                return true;
            case R.id.logout:
                Utils.buyer_id = 0;
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Utils.buyer_id = 0;
                        Toast.makeText(getApplicationContext(),"Gracias por utilizar CompraApp",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, Home.class);
            startActivity(refresh);
            this.finish();
        }
    }

    private void refreshList(){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = Utils.SERVER_URL + "/publication?status=" + Utils.PUBLICATION_OPEN + "&idBuyer=" + Utils.buyer_id;
            StringRequest jsonObjReq = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            useResponse(response);

                            mSwipeRefreshLayout.setRefreshing(false);
                            ImageView imgError = findViewById(R.id.imgErrorCloud);
                            imgError.setVisibility(View.INVISIBLE);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    mSwipeRefreshLayout.setRefreshing(false);
                    errorConnection(error);
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

            mSwipeRefreshLayout.setRefreshing(false);
            showErrorMsg(e.getMessage());
        }
    }

    private void errorConnection(Exception e){
        showErrorMsg("onErrorResponse: " + e.getMessage());
        ImageView imgError = findViewById(R.id.imgErrorCloud);
        imgError.setVisibility(View.VISIBLE);
        refreshListFromDataBase();
    }

    private void refreshListFromDataBase(){
        Publication[] publicationsBd = AdminPublications.GetPublications(this);

        this.addPublicationsToListView(publicationsBd);

    }

    private void useResponse(String response){
        try {
            //JSONArray json = new JSONArray(response);
            Gson gson = new GsonBuilder().create();
            Publication[] publicationsObjects = gson.fromJson(response.toString(), Publication[].class);

            this.addPublicationsToListView(publicationsObjects);

            AdminPublications.RefreshPublications(this,publicationsObjects);

            refreshAdapter();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("Error request: " + e.getMessage());
        }
    }


    private void addPublicationsToListView(Publication[] publicationsObjects){

        imgs = new int[publicationsObjects.length];
        publications = new String[publicationsObjects.length][4];

        int i = 0;
        for (Publication publication: publicationsObjects) {
            publications[i][0] = publication.getId()+"";
            publications[i][1] = publication.getDescription();
            publications[i][2] = publication.getCountOffers()+"";
            publications[i][3] = publication.getDescriptionItem();

            imgs[i] = R.drawable.cube_green;
            i++;
        }

        refreshAdapter();
    }



    private void refreshAdapter(){
        productsList = (ListView) findViewById(R.id.lstVProducts);
        productsList.setAdapter(new AdaptadorListPublication(this, publications, imgs));
        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent productDetail = new Intent(view.getContext(), Offers.class);
                productDetail.putExtra("ID", publications[position][0]);
                productDetail.putExtra("TITLE", publications[position][1]);
                productDetail.putExtra("OFERTAS", publications[position][2]);
                productDetail.putExtra("DESCRIPTIONITEM", publications[position][3]);
                startActivity(productDetail);
            }
        });


       // productsList.setOn
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
