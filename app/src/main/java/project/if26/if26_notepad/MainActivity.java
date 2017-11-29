package project.if26.if26_notepad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Activite qui possede la liste des notes
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * app represente l'application
     */
    public static NotePad app;

    /**
     * notes contient la liste des notes
     */
    public static ArrayList<Note> notes = new ArrayList<>();

    /**
     * noteAdapter est l'adpater gerant les notes
     */
    static NoteAdapter noteAdapter;

    /**
     * noteRecyclerView Vue contenant l'adapter de note
     */
    private RecyclerView noteRecyclerView;

    /**
     * twListeVide represente le TextView si la liste de note est vide
     */
    static TextView twListeVide;

    FloatingActionButton fab;

    private DrawerLayout dwLayout;

    /**
     * /** Création de l'activité principale
     * @param savedInstanceState le Bundle de recuperation
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //on recupere l'application
        app = (NotePad) this.getApplication();

        twListeVide = (TextView) findViewById(R.id.tw_liste_vide);

        //Creation du drawer
        createDrawer();

        //Creation de la barre d'action
        createActionBar();

        createRecyclerView();

        //initialiseData(); //TODO a enlever

        noteAdapter = new NoteAdapter(notes, this);
        noteRecyclerView.setAdapter(noteAdapter);

        refreshNotes();//on regarde si il y a des notes au depart
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/* Handling incoming intent */
        Intent intent = getIntent();
        String type = intent.getType();
        String action = intent.getAction();

        if (type != null && (Intent.ACTION_VIEW.equals(action) || Intent.ACTION_SEND.equals(action)) ) {

            if (type.startsWith("text/")) {

                try {
                    // Tentative d'ouverture du flux de données
                    InputStream attachment = getContentResolver().openInputStream(getIntent().getData());
                    BufferedReader r = new BufferedReader(new InputStreamReader(attachment));
                    // Lecture du contenu
                    String line;
                    StringBuffer result = new StringBuffer();
                    while ((line = r.readLine()) != null) {
                        Log.i("contenu", line);
                        result.append(line + "\n");
                    }
                    // openNote(new Note(gestionNotes, result.toString()));

                } catch (Exception e) {//erreur lors de la lecture
                    Snackbar.make(findViewById(R.id.content_main_layout), "Une erreur s'est produite lors de la lecture du fichier", Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setBackgroundColor(Color.GRAY);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteAdapter.createNote(); //on ouvre la note qu'on vient de creer
                //TODO on ajoute une nouvelle note et on l'edit
            }
        });
    }

    @Override
    protected void onResume() {
        refreshNotes();
        super.onResume();
    }

    /**TODO regarder ce que fait onPause*/
    @Override
    public void onPause() {
        refreshNotes();
        super.onPause();
    }


    /**
     * Permet d'afficher le message liste vide si pas de note ou de supprimer ce message
     */
    public static void refreshNotes() {
        if (noteAdapter.getItemCount()==0)
            twListeVide.setVisibility(View.VISIBLE);
        else
            twListeVide.setVisibility(View.GONE);
    }

    /**
     * Creation du drawer a gauche de l'activite principale
     */
    private void createDrawer() {
        dwLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert mNavigationView != null;
        mNavigationView.setNavigationItemSelectedListener(this);

        // header navigation drawer
        View headerView = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        TextView appVersion = (TextView) headerView.findViewById(R.id.version);
        String s = getResources().getString(R.string.app_name) + " v version";
        appVersion.setText(s);

    }

    /**
     * Creation de l'actionBar(vue en haut de l'activite principal)
     */
    private void createActionBar() {
        Context context = getApplicationContext();
        int couleurTitre = ContextCompat.getColor(context, R.color.actionbar_title_color);
        String titre = context.getString(R.string.app_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        //actionBar.setTitle(titre);
        actionBar.setTitle(Html.fromHtml("<font color='" + couleurTitre + "'>" + titre + "</font>"));
    }

    /**
     * Mise en place des parametres pour le recyclerView
     */
    private void createRecyclerView(){
        this.noteRecyclerView = (RecyclerView) findViewById(R.id.rv);
        int nbColonne = 3;//on defini le nombre de colonne possible pour nos cardviews
        GridLayoutManager glm = new GridLayoutManager(this, nbColonne);
        noteRecyclerView.setLayoutManager(glm);
    }

    /**
     * Recuperation de la note modifié
     * @param requestCode le code de la requete
     * @param resultCode le code de la reponse
     * @param intent l'intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            Bundle data = intent.getExtras();
            switch (requestCode) {
                case NoteAdapter.NOTE_EDIT: //quand la note est fini d'editer
                    if(data.getString("Note").equals("vide")){//si la note est vide
                        displaySnackbar("vide");
                    }
                    if(data.getString("Note").equals("supprimé")) {//si on veut supprimer la note
                        displaySnackbar("supprimé");
                    }
                    break;
            }
        }
    }

    /**
     * displaySnackbar affiche les erreurs entre l'edition des notes et le mainActivity
     *  @param string la requete a affiché le code de la requete
     */
    private void displaySnackbar(String string) {
        switch (string){
            case "vide":
                String color = "#6495ED";
                String message = "Vous avez creer une note vide\nVoulez-vous la re-recréer ";
                String html = "<font color='" + color + "'>" + message + "</font>";
                Snackbar.make(findViewById(R.id.content_main_layout), Html.fromHtml(html), Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.noteAdapter.createNote();//on demande de recreer une note
                            }
                        })
                        .setActionTextColor(Color.RED)
                        .show();
                break;
            case "supprimé":
                color = "#6495ED";
                message = "Note supprimé";
                html = "<font color='" + color + "'>" + message + "</font>";
                Snackbar.make(findViewById(R.id.content_main_layout), Html.fromHtml(html), Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Methode qui affiche la boite de dialog a propos
     */
    private void showAboutDialog(){
        String title = getString(R.string.about);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(getString(R.string.about_message));
        dialog.setNeutralButton("OK", null);
        dialog.show();
    }

    //TODO a commenter
    private void exit() {
        super.finish();
    }

    //TODO methode de test a enlever a la fin
    private void initialiseData() {
        notes = new ArrayList<>();
        notes.add(new Note("Salut"));
        notes.add(new Note("Salut2"));
        notes.add(new Note("Salut3"));
        notes.add(new Note("Salut"));
        notes.add(new Note("Salut2"));
        notes.add(new Note("Salut3"));
        notes.add(new Note("Salut"));
        notes.add(new Note("Salut2"));
        notes.add(new Note("Salut3"));
    }

    /**
     * TODO voir si il faut enlever
     */
    private void chargerNote() {
        //TextView tvEmpty = (TextView) findViewById(R.id.tw_empty);
        //ViewGroup parent = (ViewGroup) findViewById(R.id.tile_container);
        //noteTiles.clear();
        //noinspection ConstantConditions
        //parent.removeAllViews();
        //selectedNote = null;
        // if (gestionNotes.isEmpty()) {
        //    tvEmpty.setVisibility(View.VISIBLE);
        //    return;
        //}
        //noinspection ConstantConditions
        //tvEmpty.setVisibility(View.GONE);
        //LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //List<Note> notes = gestionNotes.getAllNotes();

        for (Note note : notes) {
            //addTile(note, parent, inflater, null);
        }
    }

    /**
     * TODO A VOIR SI IL FAUT L'enlever
     */
    private void importerPressePapier() {
        try {
            //note = gestionNotes.newFromClipboard(application);
        } catch (NullPointerException e) {
            Snackbar.make(findViewById(R.id.content_main_layout), "Le presse-papier est vide ", Snackbar.LENGTH_SHORT).show();
        }
        //loadNotes();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        dwLayout.closeDrawers();
        switch (menuItem.getItemId()) {

            case R.id.nav_add_note:// ajoute une nouvelle note
                fab.callOnClick();
                Snackbar.make(findViewById(R.id.content_main_layout), "Coucou", Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.nav_help:
                showAboutDialog();
                break;

            case R.id.nav_exit:
                exit();
                break;

            case R.id.nav_preferences:
                //TODO faire l'activite preferences
                Snackbar.make(findViewById(R.id.content_main_layout), "Lancer une activite pour les preferences", Snackbar.LENGTH_SHORT).show();
                break;

        }
        return true;
    }
}
