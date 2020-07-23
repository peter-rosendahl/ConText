package dk.prmedia.context.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import dk.prmedia.context.data.SentenceContract.SentenceEntry;

/**
 * Created by Peter on 25-03-2017.
 */

public class SentenceProvider extends ContentProvider {

    // Tag for log messages
    public static final String LOG_TAG = SentenceProvider.class.getSimpleName();

    // Database helper object
    public SentenceDbHelper mDbHelper;

    // Initialize the provider and the database helper object.
    @Override
    public boolean onCreate() {
        mDbHelper = new SentenceDbHelper(getContext());

        return true;
    }

    // Perform the query for the given URI. Use the given projection, selection, arguments, and sort order.
    @Override
    public Cursor query(Uri uri,String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database.
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch(match) {
            case SENTENCE:
                cursor = database.query(SentenceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SENTENCE_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://dk.prmedia.context/sentence/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = SentenceEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the sentence table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(SentenceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    // Insert new data into the provider with the given ContentValues.
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch(match) {
            case SENTENCE:
                return insertSentence(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    // Insert new data into the provider with the given ContentValues.
    private Uri insertSentence(Uri uri, ContentValues values) {

        String danishSentence = values.getAsString(SentenceEntry.COLUMN_DANISH_SENTENCE);
        if (danishSentence == null) {
            throw new IllegalArgumentException("Must write a danish version of your sentence.");
        }

        String defaultSentence = values.getAsString(SentenceEntry.COLUMN_DEFAULT_SENTENCE);
        if (defaultSentence == null) {
            throw new IllegalArgumentException("Must write a local version of the danish sentence.");
        }

        // Get writable database.
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new sentence with the given values.
        long id = database.insert(SentenceEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert new row for " + uri);
            return null;
        }

        // Notify all listeners that the ata has changed for the pet content URI
        // the second parameter (null) is an optional content observer parameter.
        // uri: content://com.example.android.pets/pets
        getContext().getContentResolver().notifyChange(uri, null);


        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it.
        return ContentUris.withAppendedId(uri, id);
    }

    // Updates the data at the given selection and selection arguments, with the new ContentValues.
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SENTENCE:
                return update(uri, contentValues, selection, selectionArgs);
            case SENTENCE_ID:
                // For the SENTENCE_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = SentenceEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                return updateSentence(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update sentences in the database with the given content values.
     * Return the number of rows that were succesfully updated.
     */
    private int updateSentence(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(SentenceEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


    // Delete the data at the given selection and selection arguments.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    // Returns the MIME type of data for the content URI.
    @Override
    public String getType(Uri uri) {
        return null;
    }

    // URI matcher code for the content URI for the sentence table.
    private static final int SENTENCE = 100;

    // URI matcher code for the content URI for a single sentence in the sentence database.
    private static final int SENTENCE_ID = 101;
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        /**
         * The calls to addUri() go here for all of the content URI patterns that the provider
         * should recognize. All paths added to the UriMatcher have a corresponding code to return
         * when a match is found.
         */
        sUriMatcher.addURI(SentenceContract.CONTENT_AUTHORITY, SentenceContract.PATH_SENTENCES, SENTENCE);
        sUriMatcher.addURI(SentenceContract.CONTENT_AUTHORITY, SentenceContract.PATH_SENTENCES + "/#", SENTENCE_ID);
    }
}
