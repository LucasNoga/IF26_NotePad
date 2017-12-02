package project.if26.if26_notepad.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.if26.if26_notepad.Note;


/*
DatabaseModule va permettre de gérer la table note de la BD
l'insert, le delete, l'update et le select des note dans la BD et de faire des requêtes pour récupérer les notes contenu dans la BD.
*/
public class NoteDB{

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "IF26_TP5.db";

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
    public NoteDB open(){
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
        return this;
    }

    //on ferme l'accès à la BDD
    public void close(){
        helper.close();
    }

    //Insertion d'un tuple dans la base de donnees (fonctionne comme une HashMap)
    public void insertNote(Note note) {
        open();
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        //values.put(ATTRIBUT_ID, note.getID());
        values.put(ATTRIBUT_CONTENU, note.getContenu());
        db.insert(TABLE_NOTE, ATTRIBUT_ID, values);//on insère l'objet dans la BDD via le ContentValues
        close();
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


    public void deleteAllNotes(){
        open();
        //TODO A faire une requete pour suppirmer tous les tuples
        close();
    }

    public void createTable(){
        open();
        final String table_note_create = "CREATE TABLE " + TABLE_NOTE + " ("
                + ATTRIBUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ATTRIBUT_CONTENU + " TEXT NOT NULL)";
        db.execSQL(table_note_create);
        close();
    }

    public void deleteTable(){
        open();
        String req = "DROP TABLE IF EXISTS "+ TABLE_NOTE;
        db.execSQL(req);
    }
}
