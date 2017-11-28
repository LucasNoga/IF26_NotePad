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


class Note implements Serializable{

    //private static final int BUFFER_SIZE = 512;

    /**
     * contenu contient le contenu de la note a edité
     */
    private String contenu;


    /**
     * Constructeur pour initailiser une nouvelle note
     */
    Note() {
        this.contenu = "";
    }


    /**
     * Constructeur pour les notes a modifier
     * @param content le contenu de la note
     */
    Note(String content) {
        if (content == null)
            setContenu("");
        else
            setContenu(content);
    }

    /**Getter pour recuperer le contenu d'une note
     * @return le contenu de la note
     */
    String getContenu() {
        return contenu;
    }

    /**
     * Setter pour modifier le contenu d'une note
     * @param contenu le nouveau contenu de la note
     */
    void setContenu(String contenu) {
        this.contenu = contenu;
    }

    /**
     void share(Context context) {
     Intent share = new Intent(android.content.Intent.ACTION_SEND);
     share.setType("text/plain");
     share.putExtra(android.content.Intent.EXTRA_TITLE, context.getString(R.string.shareEntityName));
     share.putExtra(android.content.Intent.EXTRA_TEXT, contenu);
     share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     context.startActivity(Intent.createChooser(share, context.getString(R.string.sharePromptText)));
     }*/

    /** Sauvegarde le contenu de la note dans le fichier filename
     void sauvegarderNote() {
     if (TextUtils.isEmpty(fileName)) {//si la note n'a pas de fichier
     generateFilename();
     }
     try{
     FileOutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE);
     byte[] buffer = contenu.getBytes();

     file.write(buffer);
     file.flush();
     file.close();
     }catch(IOException e){
     e.printStackTrace();
     }
     }*/

    /**genere un nom de fichier pour l'attribut filename
     private void generateFilename() {
     int id = -1;
     String name;
     do{
     name = "note_"+id+".txt";
     id++;
     }while(context.getFileStreamPath(name).exists());
     fileName = name;
     }*/

    /*TODO a voir pour la sauvegarde
    public static Note newFromFile(Context context, String filename)
            throws IOException {

        FileInputStream inputFileStream = context.openFileInput(filename);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;

        while ((len = inputFileStream.read(buffer)) > 0) {
            String line = new String(buffer, 0, len);
            stringBuilder.append(line);

            buffer = new byte[Note.BUFFER_SIZE];
        }

        Note n = new Note(gestionNotes, stringBuilder.toString().trim());
        n.fileName = filename;

        inputFileStream.close();

        return n;
    }
*/

/*TODO a voir
    private String getStart() {
        String s = contenu.trim();
        int end = Math.min(100, s.length());
        int nlPos = s.indexOf('\n');
        if (nlPos > 0) {
            end = Math.min(end, nlPos);
        }
        return s.substring(0, end);
    }

*/


    public boolean findChanges(String newVersion) {
        return (contenu.equals(newVersion));
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
