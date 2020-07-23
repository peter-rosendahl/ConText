package dk.prmedia.context;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dk.prmedia.context.data.SentenceContract;

/**
 * Created by Peter on 27-03-2017.
 */

public class SentenceCursorAdapter extends CursorAdapter {

    // Resource ID for the background color
    private int mColorResourceId;

    // Constructs a new CursorAdapter
    public SentenceCursorAdapter(Context context, Cursor cursor, int colorResourceId) {
        super(context, cursor, 0);
        mColorResourceId = colorResourceId;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    // Makes a new blank list item view. No data i set or bound yet.
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find the relevant Views.
        // TextView sentenceIdTv = (TextView) view.findViewById(R.id.sentence_id);
        // Find the Danish sentence TextView, and set the text from the current sentence.
        TextView danishSentenceTv = (TextView) view.findViewById(R.id.danish_sentence);
        // Find the phonetic example TextView, and set the text from the curent sentence.
        // TextView phoneticTv = (TextView) view.findViewById(R.id.phonetic_sentence);
        // Find the default sentence TextView, and set the text from the current sentence.
        TextView defaultSentenceTv = (TextView) view.findViewById(R.id.default_sentence);
        // Find the audio play ImageView, and set a click listener to it
        ImageView nextView = (ImageView) view.findViewById(R.id.li_next);

        // Find the columns of sentence attributes that we're interested in.
        int danishColumnIndex = cursor.getColumnIndex(SentenceContract.SentenceEntry.COLUMN_DANISH_SENTENCE);
        int defaultColumnInde = cursor.getColumnIndex(SentenceContract.SentenceEntry.COLUMN_DEFAULT_SENTENCE);

        // Read the sentence attributes from the cursor for the current sentence.
        String danishString = cursor.getString(danishColumnIndex);
        String defaultString = cursor.getString(defaultColumnInde);

        // Update the TextView with the attributes for the current sentence.
        danishSentenceTv.setText(danishString);
        defaultSentenceTv.setText(defaultString);

        View textContainer = view.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(context, mColorResourceId);
        textContainer.setBackgroundColor(color);
        nextView.setBackgroundColor(color);
    }

}
