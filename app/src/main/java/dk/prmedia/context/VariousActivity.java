package dk.prmedia.context;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import dk.prmedia.context.data.DetailActivity;
import dk.prmedia.context.data.SentenceContract.SentenceEntry;

/**
 * Created by Peter on 02-03-2017.
 */

public class VariousActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    SentenceCursorAdapter mCursorAdapter;

    private static final int SENTENCE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentense_list);

        // Find the ListView which will be populated with the pet data
        ListView SentenceListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        SentenceListView.setEmptyView(emptyView);

        mCursorAdapter = new SentenceCursorAdapter(this, null, R.color.cat_vari);
        SentenceListView.setAdapter(mCursorAdapter);

        SentenceListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(VariousActivity.this, DetailActivity.class);

                Uri currentSentenceUri = ContentUris.withAppendedId(SentenceEntry.CONTENT_URI, id);

                intent.setData(currentSentenceUri);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(SENTENCE_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertPet() {

        // Create a ContentValues object where column names are the keys,
        // and the test attributes are the values.
        ContentValues values = new ContentValues();
        values.put(SentenceEntry.COLUMN_DANISH_SENTENCE, "Hej.");
        values.put(SentenceEntry.COLUMN_DEFAULT_SENTENCE, "Hello.");

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // Use the PetEntry.CONTENT_URI to indicate that we want to insert into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(SentenceEntry.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the tale we care about.
        String[] projection = {
                SentenceEntry._ID,
                SentenceEntry.COLUMN_DANISH_SENTENCE,
                SentenceEntry.COLUMN_PHONETIC,
                SentenceEntry.COLUMN_DEFAULT_SENTENCE
        };
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,       // Parent activity context
                SentenceEntry.CONTENT_URI,  // Provide content URI to query
                projection,                 // Columns to include in the resulting Cursor,
                "sCategory=?",              // selects from category
                new String[] {"4"},         // 4 = CATEGORY_VARIOUS
                null);                      // No sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update SentenceCursorAdapter with this new cursor containing updated sentence data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}