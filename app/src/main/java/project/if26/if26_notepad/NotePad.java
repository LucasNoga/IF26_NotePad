package project.if26.if26_notepad;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import project.if26.if26_notepad.database.NoteDB;

/**
 * Classe representant l'application
 */
public class NotePad extends Application {

    NoteDB db;// represente la BD

    @Override
    public void onCreate() {
        super.onCreate();

        db = new NoteDB(this);
        //Log.i("dtabase", this.databaseList().toString());
        //Log.i("namedtabase", this.getDatabasePath("IF26_TP5.db").getAbsolutePath().toString());
    }

    /**
     * Meyhode qui recupere les notes contenues dans la BD
     */
    public ArrayList<Note> chargerNote() {
        return db.getAllNotes();
    }

    /**
     * Methode qui permet de sauvegarder les notes dans la BD
     */
    public void sauvegarderNote() {
        db.deleteAllNotes(); // on supprime d'abord les notes pour inserer les nouvelles
        db.insertAllNotes(MainActivity.notes);//on donne les notes du main en parametre
    }

    public void setContenuPressePapier(CharSequence string) {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text label", string);
        manager.setPrimaryClip(clipData);
    }
}
