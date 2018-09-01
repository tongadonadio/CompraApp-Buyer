package compraapp.appbuyers.request;

/**
 * Created by pabluc on 27/05/2018.
 */

public class Utils {
    //public static final String SERVER_URL = "http://192.168.0.104/CompraApp.Backend.Api/api";
    //public static final String SERVER_URL = "http://10.0.2.2:50083/api";
   // public static final String SERVER_URL = "http://172.29.2.187:4141/api";
    public static final String SERVER_URL = "http://172.29.1.8:50083/api";


    public static int buyer_id = 0;

    public static String delivery[] = {"Entrega", "Retirar"};
    public static String item_states[] = {"Nuevo", "Usado"};

    public static int OFFER_OPEN = 0;
    public static int OFFER_ACTION_REJECTED = 1;
    public static int OFFER_ACTION_ACCEPTED = 2;

    public static int PUBLICATION_OPEN = 0;
    public static int PUBLICATION_CLOSE_BY_ACEPTED_OFFER = 1;
    public static int PUBLICATION_CLOSE_BY_USER = 2;
/*OTRO CODIGOS*/
/*
    private void refreshList(){

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Utils.SERVER_URL + "/publication";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        useResponse(response);

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
*/
/*
    private void useResponse(JSONArray response){
        try {
            //JSONArray json = new JSONArray(response);
            imgs = new int[response.length()];
            publications = new String[response.length()][3];

            Gson gson = new Gson();

            Publication[] peliculasJSON = gson.fromJson(response.toString(), Publication[].class);

            for(int i=0;i<response.length();i++){
                JSONObject e = response.getJSONObject(i);
                int id = e.getInt("Id");
                publications[i][0] = id+"";
                publications[i][1] = e.getString("Description");
                publications[i][2] = "0";

                imgs[i] = R.drawable.computadora;
            }
            refreshAdapter();

        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMsg("Error json: " + e.getMessage());
        }
    }*/
}
