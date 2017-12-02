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
        Log.i("dtabase", this.databaseList().toString());
        Log.i("namedtabase", this.getDatabasePath("IF26_TP5.db").getAbsolutePath().toString());

        //TODO l'idee c'est que pour sauvegarder dans la base on supprime toutes les entrees de la base et on insere les nouvelles
        //db.createTable();
        //db.deleteTable();
        db = new NoteDB(this);
        //testBD();

    }

    //TODO a faire et a commenter methode qui retourne la liste des notes pour l'adapter
    public ArrayList<Note> recupereNotesDB() {
        return db.getAllNotes();
    }

    public CharSequence getContenuPressePapier() {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip;
        if(manager.hasPrimaryClip()) {
            clip = manager.getPrimaryClip();
            String textToPaste = clip.getItemAt(0).coerceToText(this).toString();
            if (TextUtils.isEmpty(textToPaste)) return null;
            return textToPaste;
        }
        return null;
    }

    public void setContenuPressePapier(CharSequence string) {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text label", string);
        manager.setPrimaryClip(clipData);
    }

}
