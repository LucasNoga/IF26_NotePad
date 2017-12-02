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

/**
 * Classe representant les donnees de la Note
 */
public class Note implements Serializable{

    /**
     * contenu contient le contenu de la note a edité
     */
    private String contenu;

    /**
     * id pour la base de donnees
     */
    private long id;

    /**
     * Constructeur pour initailiser une nouvelle note
     */
    public Note() {}

    /**
     * Constructeur pour editer une note
     * @param content contenu de la note
     */
    Note(String content) {
        if (content == null)
            setContenu("");
        else
            setContenu(content);
    }

    /**
     * constructeur pour la BD
     * @param id id pour la bd
     * @param content contenu de la note
     */
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


    /**
     * methode qui copie la note selectionné
     * @param application l'application NotePad
     */
    public void copierPressePapier(NotePad application) {
        application.setContenuPressePapier(contenu);
    }

    /**
     * //TODO voir l'utilité
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
