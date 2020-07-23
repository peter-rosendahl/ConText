package dk.prmedia.context.data;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import dk.prmedia.context.R;
import dk.prmedia.context.data.SentenceContract.SentenceEntry;

public class NewSentenceActivity extends AppCompatActivity {

    private EditText mDanishEdit;
    private EditText mPhoneticEdit;
    private EditText mDefaultEdit;
    private EditText mAudioEdit;
    private Spinner mCategorySpinner;
    private int mCategory = SentenceEntry.CATEGORY_LOCAL_AUTHORITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mDanishEdit = (EditText) findViewById(R.id.edit_danish);
        mPhoneticEdit = (EditText) findViewById(R.id.edit_phonetic);
        mDefaultEdit = (EditText) findViewById(R.id.edit_default);
        mAudioEdit = (EditText) findViewById(R.id.edit_audio);
        mCategorySpinner = (Spinner) findViewById(R.id.spinner_category);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_category_options, android.R.layout.simple_spinner_item);

        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Local Authority")) {
                        mCategory = SentenceEntry.CATEGORY_LOCAL_AUTHORITY;
                    } else if (selection.equals("Library")) {
                        mCategory = SentenceEntry.CATEGORY_LIBRARY;
                    } else if (selection.equals("Grocery Store")) {
                        mCategory = SentenceEntry.CATEGORY_GROCERY_STORE;
                    } else if (selection.equals("Directions")) {
                        mCategory = SentenceEntry.CATEGORY_DIRECTIONS;
                    } else if (selection.equals("Various (helping sentences)")) {
                        mCategory = SentenceEntry.CATEGORY_VARIOUS;
                    } else if (selection.equals("Introduction")) {
                        mCategory = SentenceEntry.CATEGORY_INTRO;
                    } else {
                        mCategory = SentenceEntry.CATEGORY_LOCAL_AUTHORITY;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = SentenceEntry.CATEGORY_LOCAL_AUTHORITY;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_new.xml
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    private void insertSentence() {

        String danString = mDanishEdit.getText().toString().trim();
        String phoString = mPhoneticEdit.getText().toString().trim();
        String defString = mDefaultEdit.getText().toString().trim();
        String audioInput = mAudioEdit.getText().toString().trim();
        int audioRaw = getResources().getIdentifier("raw/"+audioInput, null, getPackageName());

        ContentValues values = new ContentValues();
        values.put(SentenceEntry.COLUMN_DANISH_SENTENCE, danString);
        values.put(SentenceEntry.COLUMN_PHONETIC, phoString);
        values.put(SentenceEntry.COLUMN_DEFAULT_SENTENCE, defString);
        values.put(SentenceEntry.COLUMN_SENTENCE_CATEGORY, mCategory);
        values.put(SentenceEntry.COLUMN_AUDIO_RAW, audioRaw);

        Uri newUri = getContentResolver().insert(SentenceEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_sentence_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_insert_sentence_success), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_save:
                insertSentence();
                finish();
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
