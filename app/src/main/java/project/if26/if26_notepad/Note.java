package project.if26.if26_notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

//TODO a commenter class atributs et methodes
public class Note implements Serializable{

    /**
     * contenu contient le contenu de la note a edité
     */
    private String contenu;

    private long id;

    /**
     * Constructeur pour initailiser une nouvelle note
     */
    public Note() {}

    Note(String content) {
        if (content == null)
            setContenu("");
        else
            setContenu(content);
    }

    public Note(int id, String content) {
        this.id = id;
        if (content == null)
            setContenu("");
        else
            setContenu(content);
    }

    /**Getter pour recuperer le contenu d'une note
     * @return le contenu de la note
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Setter pour modifier le contenu d'une note
     * @param contenu le nouveau contenu de la note
     */
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public long getID(){
        return this.id;
    }

    public void setID(long id){
        this.id = id;
    }

    /**
     * methode qui copie la note selectionné
     * @param application l'application NotePad
     */
    public void copierPressePapier(NotePad application) {
        application.setContenuPressePapier(contenu);
    }

    /**
     * //TODO a commenter
     * @param noteManagement
     * @param application
     * @return

    public static Note newFromClipboard(NoteManagement noteManagement, NotePad application) {
    CharSequence string = application.getClipboardString();
    if (string == null) return null;
    return new Note(noteManagement, string);
    }
     */
}
