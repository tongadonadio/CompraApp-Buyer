package compraapp.appbuyers.persistence;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import compraapp.appbuyers.entities.Publication;

/**
 * Created by pabluc on 30/05/2018.
 */

public class AdminPublications {

    public static final String TABLE = "publications";

    /* Nombres de columnas */
    public static final String ID = "id";
    public static final String PRICE = "price";
    public static final String DESCRIPTION = "description";
    public static final String ID_BUYER = "idBuyer";
    public static final String STATE = "state";
    public static final String DELIVERY_ITEM = "deliveryItem";
    public static final String DESCRIPTION_ITEM = "descriptionItem";
    public static final String PRICE_MIN_ITEM = "priceMinItem";
    public static final String PRICE_MAX_ITEM = "priceMaxItem";
    public static final String STATE_ITEM = "stateItem";
    public static final String COUNT_OFFERS = "countOffers";

    public static void Alta(Activity context, Publication publication) {
        try {
            AdminDataBase admin = new AdminDataBase(context, null, AdminDataBase.VersionCodeDB);

            SQLiteDatabase bd = admin.getWritableDatabase();

            ContentValues registro = new ContentValues();

            registro.put(ID, publication.getId());
            registro.put(PRICE, publication.getPrice());
            registro.put(DESCRIPTION, publication.getDescription());
            registro.put(ID_BUYER, publication.getIdBuyer());
            registro.put(STATE,publication.getState());
            registro.put(DELIVERY_ITEM, publication.getDeliveryItem());
            registro.put(DESCRIPTION_ITEM, publication.getDescriptionItem());
            registro.put(PRICE_MIN_ITEM, publication.getPriceMinItem());
            registro.put(PRICE_MAX_ITEM, publication.getPriceMaxItem());
            registro.put(STATE_ITEM, publication.getStateItem());
            registro.put(COUNT_OFFERS, publication.getCountOffers());

            // los inserto en la base de datos
            bd.insert(TABLE, null, registro);

            bd.close();
        }
        catch (Exception e){
            Toast.makeText(context, "Error al guardar en BD:" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public static void RefreshPublications(Activity context, Publication[] publications) {
        try {
            AdminDataBase admin = new AdminDataBase(context, null,  AdminDataBase.VersionCodeDB);

            SQLiteDatabase bd = admin.getWritableDatabase();

            bd.delete(TABLE,null,null);

            for (Publication p: publications) {
                ContentValues registro = new ContentValues();
                registro.put(ID, p.getId());
                registro.put(PRICE, p.getPrice());
                registro.put(DESCRIPTION, p.getDescription());
                registro.put(ID_BUYER, p.getIdBuyer());
                registro.put(STATE,p.getState());
                registro.put(DELIVERY_ITEM, p.getDeliveryItem());
                registro.put(DESCRIPTION_ITEM, p.getDescriptionItem());
                registro.put(PRICE_MIN_ITEM, p.getPriceMinItem());
                registro.put(PRICE_MAX_ITEM, p.getPriceMaxItem());
                registro.put(STATE_ITEM, p.getStateItem());
                registro.put(COUNT_OFFERS, p.getCountOffers());
                // los inserto en la base de datos
                bd.insert(TABLE, null, registro);
            }

            bd.close();

        }
        catch (Exception e){
                Toast.makeText(context, "Error al guardar en BD:" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
        }
    }


    public static Publication[] GetPublications(Activity context) {
        Publication[] publications = new Publication[]{};
        try {
            AdminDataBase admin = new AdminDataBase(context, null,  AdminDataBase.VersionCodeDB);

            SQLiteDatabase bd = admin.getReadableDatabase();

            Cursor c = bd.rawQuery("SELECT "+
                    ID +","+
                    PRICE +","+
                    DESCRIPTION +","+
                    ID_BUYER +","+
                    STATE +","+
                    DELIVERY_ITEM +","+
                    DESCRIPTION_ITEM +","+
                    PRICE_MIN_ITEM +","+
                    PRICE_MAX_ITEM +","+
                    STATE_ITEM + "," +
                    COUNT_OFFERS +
                    " FROM publications ", null);

            if (c.moveToFirst()) {
                int total = c.getCount();
                publications = new Publication[total];
                int i = 0;

                do {
                    Publication p = new Publication();
                    // Passing values
                    p.setId(Integer.parseInt(c.getString(0)));
                    p.setPrice(Integer.parseInt(c.getString(1)));
                    p.setDescription(c.getString(2));
                    p.setIdBuyer(Integer.parseInt(c.getString(3)));
                    p.setState(Integer.parseInt(c.getString(4)));
                    p.setDeliveryItem(Integer.parseInt(c.getString(5)));
                    p.setDescriptionItem(c.getString(6));
                    p.setPriceMinItem(Integer.parseInt(c.getString(7)));
                    p.setPriceMaxItem(Integer.parseInt(c.getString(8)));
                    p.setStateItem(Integer.parseInt(c.getString(9)));
                    p.setCountOffers(Integer.parseInt(c.getString(10)));

                    publications[i] = p;

                    i++;
                } while (c.moveToNext());
            }
            c.close();

            bd.close();
        }
        catch (Exception e){
            Toast.makeText(context, "Error al consultar BD:" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return publications;
    }
}
