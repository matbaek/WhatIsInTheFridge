package dk.offlines.whatisinthefridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "product";
    private static final int VERSION = 1;
    private static final String COL0 = "ID";
    private static final String COL1 = "name";
    private static final String COL2 = "expire";
    private static final String COL3 = "added";
    private static final String COL4 = "type";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String expire, String added, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, name);
        contentValues.put(COL2, expire);
        contentValues.put(COL3, added);
        contentValues.put(COL4, type);

        Log.d(TAG, "addData: Adding " + name + ", " + expire + ", " + added + ", " + type + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1) return false;
        return true;
    }

    public boolean removeData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, COL0 + "=" + id, null) > 0;
    }

    public Cursor getData(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "";
        if(type == "fridge") {
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL4 + "='fridge' ORDER BY " + COL2 + " ASC";
        } else if(type == "freezer") {
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL4 + "='freezer' ORDER BY " + COL1 + " ASC";
        }
        Cursor data = db.rawQuery(query, null);

        return data;
    }
}
