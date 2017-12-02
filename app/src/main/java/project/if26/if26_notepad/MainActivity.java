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
import java.util.List;

import project.if26.if26_notepad.database.NoteDB;

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

        chargerNote();


        noteAdapter = new NoteAdapter(notes, this);
        noteRecyclerView.setAdapter(noteAdapter);

        refreshNotes();//on regarde si il y a des notes au depart

        fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setBackgroundColor(Color.GRAY);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteAdapter.createNote(); //on ouvre la note qu'on vient de creer
            }
        });
    }

    // TODO a commenter // on recupere la liste des notes
    private void chargerNote() {
        notes = app.recupereNotesDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        refreshNotes();
        super.onResume();
    }

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
        String version = appVersion.getText().toString();
        String s = getResources().getString(R.string.app_name) + " Version " + version;
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

    @Override
    public void finish() {
        Log.i("quitter", "elle est quitter");
        //TODO c'est ici qu'on sauvegarder les donnees
        super.finish();
    }

    /**
     * TODO A voir dans l'autre projet
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
                this.finish();
                break;

            case R.id.nav_preferences:
                //TODO faire l'activite preferences si on a le temps
                Snackbar.make(findViewById(R.id.content_main_layout), "Lancer une activite pour les preferences", Snackbar.LENGTH_SHORT).show();
                break;

        }
        return true;
    }
}
