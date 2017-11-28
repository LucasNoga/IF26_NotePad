package project.if26.if26_notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**Adapter pour les Note (Cardview)*/
class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public static final int NOTE_EDIT = 1;

    private List<Note> mNotes;

    private Context context;

    NoteAdapter(List<Note> notes, Context context) {
        mNotes = notes;
        this.context = context;
    }

    /**
     * méthode appelée lorsque NoteViewHolder a besoin d'être initialiser.
     * Nous définissions le layout utilisé pour chaque élément du RecyclerView,
     * puis on "inflate" le layout en utilisant le LayoutInflater.
     * La vue est alors passé en paramètres au constructeur NoteViewHolder
     */
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_note, parent, false);
        return new NoteViewHolder(view);
    }

    /**
     * méthode qui va spécifier le contenu de chaque élément de notre RecyclerView
     * Dans notre cas on va simplement définir le contenu de notre Cardview
     */
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
        holder.tw_contenuNote.setText(mNotes.get(position).getContenu());
    }

    /**
     * Retourne le nombre de notes creer
     */
    public int getItemCount() {
        return this.mNotes.size();
    }

    /**
     * Recupere la note selectionner
     */
    public Note getItem(int position) {
        return mNotes.get(position);
    }

    /**
     * Methode qui lance un Intent pour editer une note deja existante
     */
    public void openNote(int position) {
        Note note = getItem(position); // on recupere la note
        Intent intent = new Intent(context, NoteEditActivity.class);
        intent.putExtra("Note", note);
        intent.putExtra("NotePosition", position);
        ((Activity) context).startActivityForResult(intent, NOTE_EDIT);
    }

    /**
     * methode qui creer une nouvelle note et nous dirige vers l'activite d'edition
     */
    public void createNote(){
        Intent intent = new Intent(context, NoteEditActivity.class);
        ((Activity) context).startActivityForResult(intent, NOTE_EDIT);
    }

    /**
     * Methode qui ouvre le menu contextuel pour une note avec ses choix
     * @param decision la decision choisi parmi les 4 proposé
     * @param position la position de la note qui a ete selectionné
     */
    private void choixMenuContextuel(int decision, int position){
        Note note = getItem(position);
        switch (decision){
            case 0://copier le contenu
                copierContenu(note);
                break;
            case 1://partager la note
                partagerNote(note);
                break;
            case 2://dupliquer la note
                dupliquerNote(note);
                break;
            case 3://supprimer
                delNote(position);
                break;
        }
    }

    /**
     * Methode qui copie le contenu de la note dans le pressePapier
     * @param note la note dont on doit copier le contenu
     */
    private void copierContenu(Note note) {
        note.copierPressePapier(MainActivity.app);
    }

    /**
     * Methode qui permet de partager la note avec les applications possibles
     * @param note la note a partager
     */
    private void partagerNote(Note note) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(android.content.Intent.EXTRA_TITLE, context.getString(R.string.shareEntityName));
        share.putExtra(android.content.Intent.EXTRA_TEXT, note.getContenu());
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(share, context.getString(R.string.sharePromptText)));
    }

    /**
     * Methode qui permet de duplique la note dans la liste
     * @param note la note a dupliquer
     */
    private void dupliquerNote(Note note){
        mNotes.add(new Note(note.getContenu()));
        this.notifyDataSetChanged();//on refresh la liste
    }

    /**
     * Methode qui supprime un note de la liste
     * @param position position de la note a supprimer
     */
    private void delNote(int position){
        mNotes.remove(position);
        notifyItemRemoved(position);
        MainActivity.refreshNotes();//on refreshe le nombre de note
    }

    /**
     * Classe representant un item du recyclerView
     */
    class NoteViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView tw_contenuNote;

        private NoteViewHolder(final View itemview) {
            super(itemview);
            cardView = itemview.findViewById(R.id.card_note);
            tw_contenuNote = itemview.findViewById(R.id.contenu_note);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //Lancer l'edition note avec une methode OpenNote dans le NoteAdapter
                    openNote(getAdapterPosition());

                }

            });
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final String[] choice = context.getResources().getStringArray(R.array.menuContextuel);//on recupere le tableau

                    builder.setTitle(context.getResources().getString(R.string.titleMenuContextuel))
                            .setCancelable(false)
                            .setItems(choice, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int choice) {
                                    choixMenuContextuel(choice, getAdapterPosition());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //On met rien c'est juste pour enlever la boite de dialgo
                                }
                            }).show();

                    return false;
                }
            });
        }
    }
}