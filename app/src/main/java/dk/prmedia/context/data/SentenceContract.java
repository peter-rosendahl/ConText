package dk.prmedia.context.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Peter on 20-03-2017.
 */

public class SentenceContract {

    // To prevent someone from accidentally instantiating the contract class, give it an empty constructor.
    private SentenceContract() {}

    // 2nd part of the content provider URI pattern
    public static final String CONTENT_AUTHORITY = "dk.prmedia.context";

    // The 1st part of the URI pattern + the 2nd part
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // The 3rd part of the content provider URI pattern
    public static final String PATH_SENTENCES = "sentence";

    // Inner class that defines constant values for the sentences database table.
    // Each entry in the table represents a single sentence.
    public static final class SentenceEntry implements BaseColumns {

        // Name of the table for sentences.
        public static final String TABLE_NAME = "sentences";

        // Uri string that contains "content://dk.prmedia.context/sentences"
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SENTENCES);

        // Unique ID number for the sentence
        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_DANISH_SENTENCE = "sDanish";

        public static final String COLUMN_PHONETIC = "sPhonetic";

        public static final String COLUMN_DEFAULT_SENTENCE = "sDefault";

        public static final String COLUMN_SENTENCE_CATEGORY = "sCategory";

        public static final String COLUMN_AUDIO_RAW = "sAudioFile";

        public static final String COLUMN_NOTE = "sNote";

        // Spinner category items
        public static final int CATEGORY_LOCAL_AUTHORITY = 0;
        public static final int CATEGORY_LIBRARY = 1;
        public static final int CATEGORY_GROCERY_STORE = 2;
        public static final int CATEGORY_DIRECTIONS = 3;
        public static final int CATEGORY_VARIOUS = 4;
        public static final int CATEGORY_INTRO = 5;

        // Returns whether ot not the given category is Local Autority, Library, Grocery store, Directions, Various or Introduction.
        public static boolean isValidCategory(int category) {
            if (category == CATEGORY_LOCAL_AUTHORITY ||
                    category == CATEGORY_LIBRARY ||
                    category == CATEGORY_GROCERY_STORE ||
                    category == CATEGORY_DIRECTIONS ||
                    category == CATEGORY_VARIOUS ||
                    category == CATEGORY_INTRO) {
                return true;
            }
            return false;
        }

    }
}
