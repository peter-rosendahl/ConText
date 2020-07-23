package dk.prmedia.context.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static dk.prmedia.context.data.SentenceContract.SentenceEntry;

/**
 * Created by Peter on 20-03-2017.
 */

public class SentenceDbHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG = SentenceDbHelper.class.getSimpleName();

    // Database name of the file
    private static final String DATABASE_NAME = "context.db";

    // Database version.
    private static final int DATABASE_VERSION = 1;

    public SentenceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL Statement to create the 'sentences' table
        String SQL_CREATE_DATABASE_TABLE = "CREATE TABLE " + SentenceEntry.TABLE_NAME + " ("
                + SentenceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SentenceEntry.COLUMN_DANISH_SENTENCE + " TEXT NOT NULL, "
                + SentenceEntry.COLUMN_PHONETIC + " TEXT, "
                + SentenceEntry.COLUMN_DEFAULT_SENTENCE + " TEXT NOT NULL, "
                + SentenceEntry.COLUMN_SENTENCE_CATEGORY + " INTEGER NOT NULL DEFAULT 0, "
                + SentenceEntry.COLUMN_AUDIO_RAW + " TEXT, "
                + SentenceEntry.COLUMN_NOTE + " TEXT)";

        // Execute the 'sentences' table creation
        db.execSQL(SQL_CREATE_DATABASE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
