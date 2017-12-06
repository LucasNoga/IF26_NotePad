package project.if26.if26_notepad;

import java.io.Serializable;

/**
 * Classe representant les donnees de la Note
 */
public class Note implements Serializable{

    /**
     * contenu contient le contenu de la note a edité
     */
    private String contenu;

    /**
     * Constructeur pour initailiser une nouvelle note
     */
    public Note() {}

    /**
     * Constructeur pour editer une note
     * @param content contenu de la note
     */
    public Note(String content) {
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
}
