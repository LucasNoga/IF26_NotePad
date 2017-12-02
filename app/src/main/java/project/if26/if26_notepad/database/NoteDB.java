package project.if26.if26_notepad.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import project.if26.if26_notepad.Note;


/*
DatabaseModule va permettre de gérer la table note de la BD
l'insert, le delete, l'update et le select des note dans la BD et de faire des requêtes pour récupérer les notes contenu dans la BD.
*/
public class NoteDB{

    //private static final int VERSION_BDD = 1;
    //private static final String NOM_BDD = "IF26_TP5.db";

    private static final String TABLE_NOTE = "note";    //   nom   de   la   table
    private static final String ATTRIBUT_ID = "id";    // liste des   attributs
    private static final String ATTRIBUT_CONTENU = "contenu";

    //objet necessaire pour avoir acces en lecture ou en ecriture a la base de donnees complete
    private SQLiteDatabase db;

    //represente notre base de données avec la table note
    private DBHelper helper;

    private final Context context;

    public NoteDB(Context context){
        this.context = context;
    }

    //on ouvre la BDD en écriture
    private void open(){
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    //on ferme l'accès à la BDD
    private void close(){
        helper.close();
    }

    //retourne toutes les notes
    public ArrayList<Note> getAllNotes() {
        open();
        ArrayList<Note> noteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        int colId, colContenu, id;
        String contenu;
        int count = 0;

        if (cursor.moveToFirst()) {
            colId = cursor.getColumnIndex(ATTRIBUT_ID);
            colContenu = cursor.getColumnIndex(ATTRIBUT_CONTENU);

            do {
                id = cursor.getInt(colId);
                contenu = cursor.getString(colContenu);
                Note note = new Note(id, contenu);
                noteList.add(note);
                count++;
            } while (cursor.moveToNext());
            Log.i("The number of elements", count + "");
            cursor.close();
            close();
            // return contact list
        }
        return noteList;
    }

    /**
     * Methode qui insert toutes les notes de l'activite
     */
    public void insertAllNotes(List<Note> notes) {
        open();
        ContentValues values;
        for (Note note : notes){
            values = new ContentValues();
            values.put(ATTRIBUT_CONTENU, note.getContenu());
            db.insert(TABLE_NOTE, ATTRIBUT_ID, values);//on insère l'objet dans la BDD via le ContentValues
        }
    }

    /**
     * Methode qui permet de supprimer tous les tuples de la tables
     */
    public void deleteAllNotes(){
        open();
        db.delete(TABLE_NOTE, null, null);// supprime tous les tuples de la base
    }

    /**
     * Methode qui recreer la table au cas ou
     */
    public void createTable(){
        open();
        final String table_note_create = "CREATE TABLE " + TABLE_NOTE + " ("
                + ATTRIBUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ATTRIBUT_CONTENU + " TEXT NOT NULL)";
        db.execSQL(table_note_create);
    }

    /**
     * Methode qui supprime la table au cas ou
     */
    public void deleteTable(){
        open();
        String req = "DROP TABLE IF EXISTS "+ TABLE_NOTE;
        db.execSQL(req);
    }
}
