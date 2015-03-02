package oxbao.ru.dasWort;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by pocheptsov on 07.10.2014.
 */
public class Word implements Parcelable, Comparable {

    final static String LOG_TAG = "word_log";

    private int id;
    private String eng;
    private String rus;

    public Word(String eng, String rus) {
        this.eng = eng;
        this.rus = rus;
    }

    public Word() {
    }

    private Word(Parcel parcel) {
        Log.d(LOG_TAG, "Word constructor form parcel");
        id = parcel.readInt();
        eng = parcel.readString();
        rus = parcel.readString();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return eng + " - " + rus;
    }

    public String getEng() {
        return eng;
    }

    public String getRus() {
        return rus;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRus(String rus) {
        this.rus = rus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (id != word.id) return false;
        if (eng != null ? !eng.equals(word.eng) : word.eng != null) return false;
        if (rus != null ? !rus.equals(word.rus) : word.rus != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (eng != null ? eng.hashCode() : 0);
        result = 31 * result + (rus != null ? rus.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    // Pack object
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Log.d(LOG_TAG, "writeParcel");
        parcel.writeInt(id);
        parcel.writeString(eng);
        parcel.writeString(rus);
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel parcel) {
            Log.d(LOG_TAG, "createFromParcel");
            return new Word(parcel);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public int compareTo(Object o) {
        Word cmp = (Word)o;
        return this.getEng().compareTo(cmp.getEng());
    }
}
