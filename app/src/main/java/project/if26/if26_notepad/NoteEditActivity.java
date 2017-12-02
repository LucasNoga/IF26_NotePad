package project.if26.if26_notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Activite pour l'edition des notes
 */
public class NoteEditActivity extends AppCompatActivity {

    /**
     * La note a editer
     */
    private Note note;

    /**
     * la position de la note dans la liste
     */
    private int notePosition;

    /**
     * booleen pour savoir si c'est une nouvelle note
     */
    private boolean isNewNote;

    /**
     * booleen pour savoir si la note est supprimé
     */
    private boolean noteDeleted = false;

    /**
     * Zone d'edition sur la note
     */
    private EditText textEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);

        textEdit = (EditText) findViewById(R.id.et_note_edit);
        textEdit.setText("");

        CreateActionBar(); // Creation de la barre d'action

        detectionNote(); // Detecte si c'est une nouvelle note ou une note existante

        setOptionsNote();// ajoute les options a l'editetext
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Methode qui detecte si c'est une nouvelle note ou une note existante
     */
    private void detectionNote() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getSerializable("Note") != null) { //si on ouvre un note qui existe
                note = (Note) extras.getSerializable("Note");//on recupere la note
                notePosition = extras.getInt("NotePosition");//on recupere la position de la note
                isNewNote = false;
            }
        }
        else {
            isNewNote = true;
            note = new Note();
        }
    }

    /**
     * Creation de l'actionBar(vue en haut de l'activite principal)
     */
    private void CreateActionBar() {
        Context context = getApplicationContext();
        String titre = context.getString(R.string.app_edit);
        int couleurTitre = ContextCompat.getColor(context, R.color.actionbar_title_color);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(Html.fromHtml("<font color='" + couleurTitre + "'>" + titre + "</font>"));
    }
    /**
     * Methode qui gere le cas ou la note existe et a un contenu
     */
    private void setOptionsNote() {
        String contenu = note.getContenu();
        textEdit.setText(contenu);
        textEdit.setSelection(textEdit.getText().toString().length()); //place le curseur sur le dernier caractere
        textEdit.setLinksClickable(true);//TODO a voir
        textEdit.setAutoLinkMask(Linkify.WEB_URLS);//TODO a voir
        textEdit.setMovementMethod(LinkMovementMethod.getInstance());//TODO a voir
        Linkify.addLinks(textEdit, Linkify.WEB_URLS);//TODO a voir
    }
    /**
     * methode qui gere la sauvegarde et/ou l'ajout des notes
     */
    public void finish() {
        if(isNewNote && noteDeleted)//si c'est une nouvelle note et qu'on la supprime
            super.finish();
        if(isNewNote)//si c'est une nouvelle note on l'ajoute
            addNote();
        else if(noteDeleted)//si on la supprime
            super.finish();
        else //si c'est une note re-editer
            modifierNote();//sinon on l'a modifie seulement
        super.finish();
    }

    /**
     * Methode qui ajoute la note dans l'adapter
     */
    private void addNote() {
        if (textEdit.getText().toString().equals("")){// si la note est vide
            Intent intent = new Intent();
            intent.putExtra("Note", "vide");
            setResult(RESULT_OK, intent);
            super.finish();
        }
        else{
            note.setContenu(textEdit.getText().toString()); // on recupere le contenu de la note
            MainActivity.notes.add(note);// ajout de la note
            MainActivity.noteAdapter.notifyDataSetChanged();//on met a jour les donnees
        }
    }

    /**
     * modifierNote est la note qui met a jour une note deja existante
     */
    private void modifierNote() {
        note.setContenu(textEdit.getText().toString()); // on recupere le contenu de la note
        MainActivity.notes.get(notePosition).setContenu(note.getContenu()); //modifie le contenu de la note existante
        MainActivity.noteAdapter.notifyDataSetChanged();//on met a jour les donnees
    }

    /**
     * Methode qui supprime la note de l'adapter
     */
    private void deleteNote() {
        noteDeleted = true;
        if(isNewNote) // si c'est une nouvelle note
            this.finish();
        else{
            MainActivity.notes.remove(notePosition);
            MainActivity.refreshNotes();
            Intent intent = new Intent();
            intent.putExtra("Note", "supprimé");
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://revenir a l'accueil
                onBackPressed();
                break;
            case R.id.deleteItem://note supprime
                deleteNote();
                break;
            case R.id.revertChanges://annuler changement
                super.finish();
                break;
            default:
                return false;
        }
        return true;
    }
}
