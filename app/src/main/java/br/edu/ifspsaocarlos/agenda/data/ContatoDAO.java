package br.edu.ifspsaocarlos.agenda.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agenda.model.Contato;

import java.util.ArrayList;
import java.util.List;


public class ContatoDAO {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public ContatoDAO(Context context) {
        this.dbHelper = new SQLiteHelper(context);
    }

    public List<Contato> buscaTodosContatos() {

        this.database = this.dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[]{
                SQLiteHelper.KEY_ID,
                SQLiteHelper.KEY_NAME,
                SQLiteHelper.KEY_FONE,
                SQLiteHelper.KEY_EMAIL,
                SQLiteHelper.KEY_FAVORITE,
                SQLiteHelper.KEY_BIRTHDAY
        };

        cursor = this.database.query(SQLiteHelper.DATABASE_TABLE, cols, null, null,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext()) {

            Contato contato = new Contato();

            contato.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ID))));
            contato.setNome(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_NAME)));
            contato.setFone(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_FONE)));
            contato.setEmail(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_EMAIL)));
            contato.setBirthday(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_BIRTHDAY)));

            if (cursor.getInt(cursor.getColumnIndex(SQLiteHelper.KEY_FAVORITE)) == 1) contato.setFavorite(1);
            else contato.setFavorite(0);
            contatos.add(contato);

        }

        cursor.close();
        this.database.close();
        return contatos;

    }

    public List<Contato> buscaContato(String pesquisa) {

        this.database = this.dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[]{
                SQLiteHelper.KEY_ID,
                SQLiteHelper.KEY_NAME,
                SQLiteHelper.KEY_FONE,
                SQLiteHelper.KEY_EMAIL,
                SQLiteHelper.KEY_FAVORITE,
                SQLiteHelper.KEY_BIRTHDAY
        };

        String where = SQLiteHelper.KEY_NAME + " LIKE ?" + " OR " + SQLiteHelper.KEY_EMAIL + " LIKE ?";
        String[] argWhere = new String[]{"%" + pesquisa + "%", "%" + pesquisa + "%"};

        cursor = this.database.query(SQLiteHelper.DATABASE_TABLE, cols, where, argWhere,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext()) {

            Contato contato = new Contato();

            contato.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ID))));
            contato.setNome(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_NAME)));
            contato.setFone(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_FONE)));
            contato.setEmail(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_EMAIL)));
            contato.setBirthday(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_BIRTHDAY)));

            if (cursor.getInt(cursor.getColumnIndex(SQLiteHelper.KEY_FAVORITE)) == 1) contato.setFavorite(1);
            else contato.setFavorite(0);
            contatos.add(contato);

        }

        cursor.close();
        this.database.close();
        return contatos;

    }

    public List<Contato> showContactFavorites() {

        this.database = this.dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[]{
                SQLiteHelper.KEY_ID,
                SQLiteHelper.KEY_NAME,
                SQLiteHelper.KEY_FONE,
                SQLiteHelper.KEY_EMAIL,
                SQLiteHelper.KEY_FAVORITE,
                SQLiteHelper.KEY_BIRTHDAY
        };

        String where = SQLiteHelper.KEY_FAVORITE + " = ?";
        String[] argWhere = new String[]{"1"};

        cursor = this.database.query(SQLiteHelper.DATABASE_TABLE, cols, where, argWhere, null, null, SQLiteHelper.KEY_NAME);


        while (cursor.moveToNext()) {

            Contato contato = new Contato();
            contato.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ID))));
            contato.setNome(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_NAME)));
            contato.setFone(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_FONE)));
            contato.setEmail(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_EMAIL)));
            contato.setBirthday(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_BIRTHDAY)));

            if (cursor.getInt(cursor.getColumnIndex(SQLiteHelper.KEY_FAVORITE)) == 1) contato.setFavorite(1);
            else contato.setFavorite(0);
            contatos.add(contato);

        }

        cursor.close();
        this.database.close();
        return contatos;

    }

    public void salvaContato(Contato c) {

        this.database = this.dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_FAVORITE, c.getFavorite());
        values.put(SQLiteHelper.KEY_BIRTHDAY, c.getBirthday());

        if (c.getId() > 0) this.database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "=" + c.getId(), null);
        else this.database.insert(SQLiteHelper.DATABASE_TABLE, null, values);

        this.database.close();

    }

    public void auditingFavorites(Contato c) {

        this.database = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_FAVORITE, c.getFavorite());

        if (c.getId() > 0) this.database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "="  + c.getId(), null);
        this.database.close();

    }


    public void removeContact(Contato c) {

        this.database = this.dbHelper.getWritableDatabase();
        this.database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "=" + c.getId(), null);
        this.database.close();

    }
}
