package compraapp.appbuyers.persistence;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import compraapp.appbuyers.entities.Offer;

/**
 * Created by pabluc on 30/05/2018.
 */

public class AdminOffers {

    public static final String TABLE = "offers";

    /* Nombres de columnas */
    public static final String ID = "id";
    public static final String ID_SELLER = "idSeller";
    public static final String ID_PUBLICATION = "idPublication";
    public static final String STATE = "state";
    public static final String PRICE_ITEM = "priceItem";
    public static final String DELIVERY_ITEM = "deliveryItem";
    public static final String DESCRIPTION_ITEM = "descriptionItem";
    public static final String STATE_ITEM = "stateItem";
    public static final String DELIVERY_ZONE_ITEM = "deliveryZoneItem";
    public static final String PHOTE_ITEM = "photoItem";

    public static void RefreshOfferss(Activity context, Offer[] offers) {
        try {
            AdminDataBase admin = new AdminDataBase(context, null,  AdminDataBase.VersionCodeDB);

            SQLiteDatabase bd = admin.getWritableDatabase();

            bd.delete(TABLE,null,null);

            for (Offer o: offers) {
                ContentValues registro = new ContentValues();
                registro.put(ID, o.getId());
                registro.put(ID_SELLER, o.getIdSeller());
                registro.put(ID_PUBLICATION, o.getIdPublication());
                registro.put(STATE, o.getState());
                registro.put(PRICE_ITEM, o.getPriceItem());
                registro.put(DELIVERY_ITEM, o.getDeliveryItem());
                registro.put(DESCRIPTION_ITEM, o.getDescriptionItem());
                registro.put(STATE_ITEM, o.getStateItem());
                registro.put(DELIVERY_ZONE_ITEM, o.getDeliveryZoneItem());
                registro.put(PHOTE_ITEM, o.getPhotoItem());

                // los inserto en la base de datos
                //bd.ins(TABLE, null, registro);
            }

            bd.close();

        }
        catch (Exception e){
                Toast.makeText(context, "Error al guardar en BD:" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
        }
    }


    public static Offer[] GetOffers(Activity context) {
        Offer[] offers = new Offer[]{};
        try {
            AdminDataBase admin = new AdminDataBase(context, null,  AdminDataBase.VersionCodeDB);

            SQLiteDatabase bd = admin.getReadableDatabase();

            Cursor c = bd.rawQuery("SELECT "+
                    ID +","+
                    ID_SELLER +","+
                    ID_PUBLICATION +","+
                    STATE +","+
                    PRICE_ITEM +","+
                    DELIVERY_ITEM +","+
                    DESCRIPTION_ITEM +","+
                    STATE_ITEM +","+
                    DELIVERY_ZONE_ITEM +","+
                    PHOTE_ITEM +
                    " FROM " + TABLE, null);

            if (c.moveToFirst()) {
                int total = c.getCount();
                offers = new Offer[total];
                int i = 0;

                do {
                    Offer o = new Offer();
                    // Passing values
                    o.setId(Integer.parseInt(c.getString(0)));

                    offers[i] = o;

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
        return offers;
    }
}
