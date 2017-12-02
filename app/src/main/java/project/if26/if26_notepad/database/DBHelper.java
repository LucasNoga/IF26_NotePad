package project.if26.if26_notepad.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*Cette classe va nous permettre de définir la table qui sera produite lors de l'instanciation de celle-ci.*/
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NotesManager"; // nom du fichier pour la base private static final
    private static final String TABLE_NOTE = "note";    //   nom   de   la   table
    private static final String ATTRIBUT_ID = "id";    // liste des   attributs
    private static final String ATTRIBUT_CONTENU = "contenu";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override  //on crée la table à partir de la requête
    public void onCreate(SQLiteDatabase db) {
        final String table_note_create = "CREATE TABLE " + TABLE_NOTE + " ("
                + ATTRIBUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ATTRIBUT_CONTENU + " TEXT NOT NULL)";
        db.execSQL(table_note_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}