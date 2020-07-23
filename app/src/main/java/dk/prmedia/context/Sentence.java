package dk.prmedia.context;

/**
 * Created by Peter on 02-03-2017.
 */

public class Sentence {

    private int mSentenceId;

    // The Danish sentence
    private String mDanishSentence;

    private String mPhonetic;

    // The Default sentence
    private String mDefaultSentence;

    // The audio resource id
    private int mAudioResourceId;

    // Creates the object
    public Sentence(int sentenceId, String danishSentence, String defaultSentence) {
        mSentenceId = sentenceId;
        mDanishSentence = danishSentence;
        mDefaultSentence = defaultSentence;
    }

    // Creates the object
    public Sentence(int sentenceId, String danishSentence, String phonetic, String defaultSentence) {
        mSentenceId = sentenceId;
        mDanishSentence = danishSentence;
        mPhonetic = phonetic;
        mDefaultSentence = defaultSentence;
    }

    // Creates the object
    public Sentence(int sentenceId, String danishSentence, String phonetic, String defaultSentence, int audioResourceId) {
        mSentenceId = sentenceId;
        mDanishSentence = danishSentence;
        mPhonetic = phonetic;
        mDefaultSentence = defaultSentence;
        mAudioResourceId = audioResourceId;
    }

    // Get the sentence ID, and return it publicly.
    public int getSentenceId() { return mSentenceId; }

    // Get the default sentence, and return it publicly
    public String getDefaultSentence() { return mDefaultSentence; }

    // Get the phonetic example, and return it publicly
    public String getPhonetic() { return mPhonetic; }

    // Get the Danish sentence, and return it publicly
    public String getDanishSentence() { return mDanishSentence; }

    // Get the audio resource id, and return it publicly
    public int getAudioResourceId() { return mAudioResourceId; }
}
