package br.edu.ifspsaocarlos.agenda.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda.db";
    static final String DATABASE_TABLE = "contatos";
    static final String KEY_ID = "id";
    static final String KEY_NAME = "nome";
    static final String KEY_FONE = "fone";
    static final String KEY_EMAIL = "email";
    static final String KEY_FAVORITE = "favorite";
    static final String KEY_BIRTHDAY = "birthday";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, " +
            KEY_FAVORITE + " INTEGER, " +
            KEY_BIRTHDAY + " TEXT, " +
            KEY_EMAIL + " TEXT);";

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            final String sql = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + KEY_FAVORITE + " integer NOT NULL DEFAULT 0";
            database.execSQL(sql);
        }
        if (oldVersion < 3) {
            final String sql = "ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + KEY_BIRTHDAY + " text";
            database.execSQL(sql);
        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}

