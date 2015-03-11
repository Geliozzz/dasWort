package oxbao.ru.dasWort;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by pocheptsov on 03.03.2015.
 */
public class Lerner
{
    private ActivityLearn m_ownActivity;
    private ArrayList<Word> m_newWordArrayList;
    private ArrayList<Word> m_fakeWordArrayList;


    private final String LOG_TAG = "LERNER";

    private int numberWords = 0; //Количество слов

    private static final int TRY = 1000;

    private int trueAnswer;
    private String trueString;

    private int indexTrueWord; // Индекс правильного слова

    public Lerner(ActivityLearn m_ownActivity)
    {
        this.m_ownActivity = m_ownActivity;
    }

    public void start()
    {
        m_newWordArrayList = new ArrayList<Word>(ActivityMain.g_executor.get100NewWords());
        Log.d(LOG_TAG, "start");
        if (ActivityMain.g_executor.getAllWordsFromDatabase().size() < 4)
        {
            m_ownActivity.createAlertForAddDB(); // Add if in all base little words
        }
        takeNewWords();
        Log.d(LOG_TAG, String.valueOf(m_newWordArrayList.size()) + " - number of words"  );
        initQuestion(m_newWordArrayList);
    }

    public void newQuestion()
    {
        if (m_newWordArrayList.size() <= 0)
        {
            takeNewWords();
        }
        initQuestion(m_newWordArrayList);
    }

    private void initQuestion(ArrayList<Word> wordArrayList)
    {
        boolean needMoreWords = false; // Нужен в случае если слов не хватает

        Log.d(LOG_TAG, String.valueOf(m_newWordArrayList.size()) + " - number of words"  );
        Log.d(LOG_TAG, String.valueOf(m_fakeWordArrayList.size()) + " - number of fake words"  );

        Word trueWord = getRandomWord(wordArrayList);
        /*Пользователь может сделать английское слово пустым*/
        int cnt = 0;
        while (trueWord.getRus().equals("") || trueWord.getEng().equals(""))
        {
            cnt++;
            if (cnt > TRY)
            {
                /*Формируем список только с нормальными словами*/
                ArrayList<Word> normWords = new ArrayList<Word>();
                for (Word word : wordArrayList)
                {
                    if (!word.getEng().equals("") && !word.getRus().equals(""))
                    {
                        normWords.add(word);
                    }
                }
                if (normWords.size() == 0)
                {
                    trueWord = new Word("fill", "заполнить");
                } else trueWord = getRandomWord(normWords); //БАГ!!!!!!!!!!!!!!!!
                break;
            }
        }
        // ****************************************************************
        indexTrueWord = wordArrayList.indexOf(trueWord);
        Log.d(LOG_TAG, String.valueOf(indexTrueWord + " - indexTrueWord"));

        //*********************************************


        trueString = trueWord.getRus();


        Word fake1 = getRandomWord(m_fakeWordArrayList);
        Word fake2 = getRandomWord(m_fakeWordArrayList);
        Word fake3 = getRandomWord(m_fakeWordArrayList);
        int count = 0;
        /*Цикл. Если слова совпадают между собой или если переводы или слово пустое*/
        while (trueWord.getRus().equals(fake1.getRus()) || trueWord.getRus().equals(fake2.getRus())
                || trueWord.getRus().equals(fake3.getRus())
                || fake1.getRus().equals(fake2.getRus()) || fake1.getRus().equals(fake3.getRus())
                || fake2.getRus().equals(fake3.getRus())
                /*Проверка на не пустые строчки*/
                || fake1.getRus().equals("") || fake2.getRus().equals("") || fake3.getRus().equals(""))
        {
            count++;
            fake1 = getRandomWord(m_fakeWordArrayList);
            fake2 = getRandomWord(m_fakeWordArrayList);
            fake3 = getRandomWord(m_fakeWordArrayList);
            if (count > TRY)
            {
                needMoreWords = true; // Флаг нужен для записания стандартный значений в кнопки
                break;
            }
        }
        Random random = new Random();
        trueAnswer = m_ownActivity.initButtonForQuest(random.nextInt(4), trueWord, fake1, fake2, fake3, needMoreWords);
    }

    private Word getRandomWord(ArrayList<Word> words)
    {
        Random random = new Random();
        int index = random.nextInt(words.size());
        return words.get(index);
    }

    public void answer(int numButton)
    {
        if (trueAnswer == numButton)
        {
            m_ownActivity.setResultText(true, " ");
            Word trueWord = m_newWordArrayList.get(indexTrueWord);
            //**************************************
            int repeatInWord = trueWord.getRepeat();
            int countInWord = trueWord.getCount();
            countInWord++;
            repeatInWord++;
            trueWord.setCount(countInWord);
            trueWord.setRepeat(repeatInWord);
            ActivityMain.g_executor.updateWordInDataBase(trueWord);
            m_newWordArrayList.remove(indexTrueWord);
            if (m_newWordArrayList.isEmpty())
            {
                takeNewWords();
            }
        } else
            m_ownActivity.setResultText(false, trueString);

        m_ownActivity.waitAndAsk();
    }

    private void takeNewWords()
    {
        m_newWordArrayList = new ArrayList<Word>(ActivityMain.g_executor.get100NewWords());
        if (m_newWordArrayList.size() == 0)
        {
            Log.d(LOG_TAG, "refresh db");
            refreshDB();
        }
        if (m_newWordArrayList.size() < 10)
        {
            m_fakeWordArrayList = new ArrayList<Word>(ActivityMain.g_executor.getAllWordsFromDatabase());
        } else {
            m_fakeWordArrayList = new ArrayList<Word>(m_newWordArrayList);
        }

    }

    private void refreshDB()
    {
       m_newWordArrayList = new ArrayList<Word>(ActivityMain.g_executor.getAllWordsFromDatabase());
       for (Word word: m_newWordArrayList)
       {
           word.setRepeat(0);
           ActivityMain.g_executor.updateWordInDataBase(word);
       }
        m_ownActivity.showToast("base refreshed");
    }
}
