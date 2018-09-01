package compraapp.appbuyers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorListPublication extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    String[][] products;
    int[] imgs;

    public AdaptadorListPublication(Context context, String[][] data, int[] imgs){
        this.context = context;
        this.products = data;
        this.imgs = imgs;

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.list_view_element_publication, null);

        TextView txtTitle = (TextView)view.findViewById(R.id.textViewTitlePublicationRow);
        TextView txtDescriptionItem = (TextView)view.findViewById(R.id.txtProduct);
        TextView txtCountOffers = (TextView)view.findViewById(R.id.txtPrice);
        ImageView imgPreView = (ImageView)view.findViewById(R.id.imgPreview);

        txtTitle.setText(this.products[i][1]);
        txtCountOffers.setText("Ofertas: " + this.products[i][2]);
        txtDescriptionItem.setText(this.products[i][3]);
        imgPreView.setImageResource(this.imgs[i]);

        return view;
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
