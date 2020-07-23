package dk.prmedia.context.data;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dk.prmedia.context.R;
import dk.prmedia.context.data.SentenceContract.SentenceEntry;

/**
 * Created by Peter on 28-03-2017.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EXISTING_SENTENCE_LOADER = 0;

    private Uri mCurrentSentenceUri;

    // Text input for notes
    private EditText mNotesText;

    // TextViews for other data
    private TextView mDanishText;
    private TextView mDefaultText;
    private TextView mPhoneticText;
    private int audioId = R.raw.color_gray;
    private ImageView mSoundImage;

    private MediaPlayer mMediaPlayer;

    // AudioManager object
    private AudioManager mAudioManager;


    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {

                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // We gain focus and can resume playback
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // We've lost audio focus and therefore stop the playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Use getIntent() and getData() to get the associated URI
        Intent intent = getIntent();
        mCurrentSentenceUri = intent.getData();

        if (mCurrentSentenceUri == null) {
            setTitle("Add a sentence");
        } else {
            setTitle("Sentence Details");
            // Initialize the loader to read the sentence data from the database
            getLoaderManager().initLoader(EXISTING_SENTENCE_LOADER, null, this);
        }

        mDanishText = (TextView) findViewById(R.id.detail_danish);
        mDefaultText = (TextView) findViewById(R.id.detail_default);
        mPhoneticText = (TextView) findViewById(R.id.detail_phonetic);
        mNotesText = (EditText) findViewById(R.id.note_edit_text);
        mSoundImage = (ImageView) findViewById(R.id.sound_btn);


        // Create and setup the AudioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mSoundImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    mMediaPlayer = MediaPlayer.create(DetailActivity.this, audioId);

                    //Start the audio file
                    mMediaPlayer.start();

                    //Setup a listener on the media player, so that we can stop and release the media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    //This listener gets triggered when the MediaPlayer has completed playing the audio file.
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // if he media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null){
            // Regardless of the current state of the media player, release its resources because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
        }
    };

    // Saves if a note has been made in a sentence
    private void saveNote() {
        // Read from input field
        String noteString = mNotesText.getText().toString();

        // Create a ContentValues Object where column name is the key
        // and the note attribute from the editor is the value.
        ContentValues values = new ContentValues();
        values.put(SentenceEntry.COLUMN_NOTE, noteString);

        if (mCurrentSentenceUri == null) {
            Uri newUri = getContentResolver().insert(SentenceEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "error on creating sentence", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sentence saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentSentenceUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, "Error on updating note", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                SentenceEntry._ID,
                SentenceEntry.COLUMN_DANISH_SENTENCE,
                SentenceEntry.COLUMN_PHONETIC,
                SentenceEntry.COLUMN_DEFAULT_SENTENCE,
                SentenceEntry.COLUMN_AUDIO_RAW,
                SentenceEntry.COLUMN_NOTE };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                mCurrentSentenceUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            int danishColumnIndex = cursor.getColumnIndex(SentenceEntry.COLUMN_DANISH_SENTENCE);
            int phoneticColumnIndex = cursor.getColumnIndex(SentenceEntry.COLUMN_PHONETIC);
            int defaultColumnIndex = cursor.getColumnIndex(SentenceEntry.COLUMN_DEFAULT_SENTENCE);
            int audioColumnIndex = cursor.getColumnIndex(SentenceEntry.COLUMN_AUDIO_RAW);
            int noteColumnIndex = cursor.getColumnIndex(SentenceEntry.COLUMN_NOTE);

            // Extract out the value from the Cursor for the given column index
            String sDanish = cursor.getString(danishColumnIndex);
            String sDefault = cursor.getString(defaultColumnIndex);
            String sPhonetic = cursor.getString(phoneticColumnIndex);
            String sAudio = cursor.getString(audioColumnIndex);
            String sNote = cursor.getString(noteColumnIndex);

            // Place the text to the TextViews
            mDanishText.setText(sDanish);
            mDefaultText.setText(sDefault);
            mPhoneticText.setText(sPhonetic);
            mNotesText.setText(sNote);
            audioId = Integer.parseInt(sAudio);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mDanishText.setText("");
        mDefaultText.setText("");
        mPhoneticText.setText("");
        mNotesText.setText("");
    }
}
