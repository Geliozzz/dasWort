package oxbao.ru.dasWort;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by pocheptsov on 02.03.2015.
 */
public class Executor
{
    private static Executor ourInstance = new Executor();
    private Context m_context;

    private final String LOG_TAG = "ExecutorLogs";
    private SqliteWordHelper m_SqliteWordHelper;

    public static Executor getInstance()
    {
        return ourInstance;
    }

    private Executor()
    {
    }

    public void createDB(Context context)
    {
        m_SqliteWordHelper = new SqliteWordHelper(context);
        m_context = context;
        Log.d(LOG_TAG, context.getCacheDir().toString());
    }

    public void addWordToDataBase(Word addWord)
    {
        m_SqliteWordHelper.addWord(addWord);
    }

    public List<Word> getAllWordsFromDatabase()
    {
        return m_SqliteWordHelper.getAllWords();
    }

    public void deleteWordFromDataBase(Word delWord)
    {
        m_SqliteWordHelper.deleteWord(delWord);
    }

    public void updateWordInDataBase(Word updateWord)
    {
        m_SqliteWordHelper.updateWord(updateWord);
    }

    public void createStandardDataBase()
    {
        String[] arr = m_context.getResources().getStringArray(R.array.standart_words);
        for (int i = 0; i < arr.length; i++) {
            String[] arr_split = arr[i].split("=");
            addWordToDataBase(new Word(arr_split[0], arr_split[1]));
        }
    }


}
