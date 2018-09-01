package compraapp.appbuyers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import compraapp.appbuyers.request.Utils;

public class AdaptadorListOffer extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    String[][] offers;
    int[] imgs;

    public AdaptadorListOffer(Context context, String[][] data, int[] imgs){
        this.context = context;
        this.offers = data;
        this.imgs = imgs;

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.list_view_element_offer, null);

        TextView txtSeller = (TextView)view.findViewById(R.id.txtVOSeller);
        TextView txtProduct = (TextView)view.findViewById(R.id.txtProduct);
        TextView txtPrice = (TextView)view.findViewById(R.id.txtPrice);
        TextView txtDeliveryItem = (TextView)view.findViewById(R.id.txtDeliveryItem);
        ImageView imgPreView = (ImageView)view.findViewById(R.id.imgPreview);

        txtProduct.setText(this.offers[i][1]);
        txtSeller.setText(this.offers[i][6]);
        txtPrice.setText("USD " + this.offers[i][3]);
        //
        txtDeliveryItem.setText(Utils.delivery[Integer.parseInt(this.offers[i][7])]);
        //imgPreView.setImageResource(this.imgs[i]);
        try {
            if (this.offers[i][8]!= "null" && this.offers[i][8].length() > 0) {
                //decode base64 string to image
                //byte[] imageBytes = Base64.decode(this.offers[i][8], Base64.DEFAULT);
                //Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                this.offers[i][8] = this.offers[i][8].replace("data:image/jpeg;base64,","");
                byte[] decodedString = Base64.decode(this.offers[i][8], Base64.CRLF);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgPreView.setImageBitmap(decodedByte);
            }
        }
        catch (Exception e){
            imgPreView.setImageResource(R.drawable.cube_green);
            this.showErrorMsg(e.getMessage());
        }

        return view;
    }

    private void showErrorMsg(String msg){
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;

        //Toast toast = Toast.makeText(context, text, duration);
        //toast.show();
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
