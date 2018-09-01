package compraapp.appbuyers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import compraapp.appbuyers.request.Utils;


public class Login extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            updateUI(account);
        } catch (ApiException e) {
            showErrorMsg("Ocurrió un error al loguearse: " + e.getStatusCode());

        }
    }

    private void updateUI(@android.support.annotation.Nullable GoogleSignInAccount account) {
        if (account != null) {
            String email = account.getEmail();
            String name = account.getDisplayName();

            getUserId(email, name);
        }
    }

    private void postUser(String email, String name) {
        final int[] userId = {-1};
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Email", email);
        postParam.put("Name", name);

        String url = Utils.SERVER_URL + "/Buyer";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.buyer_id = getIdFromUser(response);
                        IrAHome();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMsg(error.getMessage());
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

    private void getUserId(final String email, final String name) {
        final int[] userId = {-1};
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Utils.SERVER_URL + "/Buyer/GetByEmail/" + email + "/";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userId[0] = getIdFromUser(response);
                        if (userId[0] == -1) {
                            postUser(email, name);
                        }
                        else {
                            Utils.buyer_id = userId[0];
                            IrAHome();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                postUser(email, name);
                //showErrorMsg(error.getMessage());
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

    private int getIdFromUser(JSONObject response){
        try {
            int userId = -1;

            userId = response.getInt("Id");

            return userId;
        }
        catch (JSONException ex) {
            return -1;
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void IrAHome(){
        if (Utils.buyer_id != 0) {
            Intent home = new Intent(Login.this, Home.class);
            startActivity(home);
        } else {
            showErrorMsg("Debe loguearse para iniciar la aplicación.");
        }
    }
}
