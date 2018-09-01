package compraapp.appbuyers.persistence;

/**
 * Created by pabluc on 30/05/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminDataBase  extends SQLiteOpenHelper {

    public static int VersionCodeDB = 2;

    public static String NAME_DATABASE = "CompraapAppBuyer.db";

    public AdminDataBase(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, NAME_DATABASE, factory, version);
        //context.deleteDatabase(NAME_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table publications(" +
                "id integer primary key," +
                "price integer, " +
                "description text, " +
                "idBuyer integer," +
                "state integer," +
                "deliveryItem integer," +
                "descriptionItem text," +
                "priceMinItem integer," +
                "priceMaxItem integer," +
                "stateItem integer," +
                "countOffers integer" +
                ")");

        db.execSQL("create table buyers(" +
                "id integer primary key," +
                "email text, " +
                "name text, " +
                "address text," +
                "phone text," +
                "notifications integer" +
                ")");

        db.execSQL("create table sellers(" +
                "id integer primary key," +
                "email text, " +
                "name text, " +
                "address text," +
                "phone text," +
                "notifications integer" +
                ")");

        db.execSQL("create table offers(" +
                "id integer primary key," +
                "idSeller integer, " +
                "idPublication integer, " +
                "state integer," +
                "priceItem integer," +
                "deliveryItem integer," +
                "descriptionItem text," +
                "stateItem integer," +
                "deliveryZoneItem text," +
                "photoItem text" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version1, int version2) {

        //db.execSQL("drop table if exists usuario");
        //db.execSQL("create table usuario(dni integer primary key, nombre text, ciudad text, numero integer)");
    }
}