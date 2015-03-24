package oxbao.ru.dasWort;
/*Проверят ьпри пустой БД. При одном слове в БД валится исключание*/

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityLearn extends ActionBarActivity
{
    private  Lerner m_lerner;

 //   private ArrayList<Word> wordArrayList;
    private TextView m_txtQuesWord;
    private Button m_btnVersion1;
    private Button m_btnVersion2;
    private Button m_btnVersion3;
    private Button m_btnVersion4;
    private TextView m_tvNumOfWords;

    private Handler handler;

    private final static int BUTTON_1 = 1;
    private final static int BUTTON_2 = 2;
    private final static int BUTTON_3 = 3;
    private final static int BUTTON_4 = 4;

    private final static long DELAY_MS = 1200; //Задержка отображения Верно не верно



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_layout);
        m_lerner = new Lerner(this);
        initGUI();
        if(m_lerner.isNeedAddWord())
        {
            createAlertForAddDB();
        } else {
            m_lerner.start();
        }

    }

    public void createAlertForAddDB()
    {
        AlertDialog.Builder ad = new AlertDialog.Builder(ActivityLearn.this)
                .setTitle(getResources().getString(R.string.alert_title))
                .setMessage(getResources().getString(R.string.alert_message))
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.alert_no),
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                //Отказ от стандартных слов. Дальнейшая работа невозможна
                                finish();
                                dialog.cancel();
                            }
                        }
                )
                .setPositiveButton(getResources().getString(R.string.alert_yes),
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int ii)
                            {
                                //finish();
                                //Заполняем базу данных стандартным набором слов если пользователь согласен
                                ActivityMain.g_executor.createStandardDataBase();
                                dialog.cancel();
                                m_lerner.start();
                            }
                        }
                );
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
    }



    private void initGUI()
    {
        m_txtQuesWord = (TextView) findViewById(R.id.tv_quest_word);
        m_btnVersion1 = (Button) findViewById(R.id.btn_version_1);
        m_btnVersion2 = (Button) findViewById(R.id.btn_version_2);
        m_btnVersion3 = (Button) findViewById(R.id.btn_version_3);
        m_btnVersion4 = (Button) findViewById(R.id.btn_version_4);
        m_tvNumOfWords = (TextView)findViewById(R.id.tvNumWords);

        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                m_lerner.newQuestion();
            }
        };

        m_btnVersion1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_lerner.answer(BUTTON_1);
            }
        });
        m_btnVersion2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_lerner.answer(BUTTON_2);
            }
        });
        m_btnVersion3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_lerner.answer(BUTTON_3);
            }
        });
        m_btnVersion4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_lerner.answer(BUTTON_4);
            }
        });


    }

    public int initButtonForQuest(int variant, Word trueWord, Word fake1, Word fake2,
                                   Word fake3, boolean needMoreWords, int NumWords)
    {
        m_txtQuesWord.setText(trueWord.getEng());
        m_tvNumOfWords.setText(String.valueOf(NumWords));
        int trueAnswer = 1;
        switch (variant)
        {
            case 0:
                m_btnVersion1.setText(trueWord.getRus());
                trueAnswer = 1;
                if (!needMoreWords)
                {
                    m_btnVersion2.setText(fake1.getRus());
                    m_btnVersion3.setText(fake2.getRus());
                    m_btnVersion4.setText(fake3.getRus());
                } else
                {
                    m_btnVersion2.setText(getResources().getString(R.string.add));
                    m_btnVersion3.setText(getResources().getString(R.string.more));
                    m_btnVersion4.setText(getResources().getString(R.string.words));
                }

                break;
            case 1:
                trueAnswer = 2;
                m_btnVersion2.setText(trueWord.getRus());
                if (!needMoreWords)
                {
                    m_btnVersion1.setText(fake1.getRus());
                    m_btnVersion3.setText(fake2.getRus());
                    m_btnVersion4.setText(fake3.getRus());
                } else
                {
                    m_btnVersion1.setText(getResources().getString(R.string.add));
                    m_btnVersion3.setText(getResources().getString(R.string.more));
                    m_btnVersion4.setText(getResources().getString(R.string.words));
                }
                break;
            case 2:
                trueAnswer = 3;
                m_btnVersion3.setText(trueWord.getRus());
                if (!needMoreWords)
                {
                    m_btnVersion2.setText(fake1.getRus());
                    m_btnVersion1.setText(fake2.getRus());
                    m_btnVersion4.setText(fake3.getRus());
                } else
                {
                    m_btnVersion2.setText(getResources().getString(R.string.add));
                    m_btnVersion1.setText(getResources().getString(R.string.more));
                    m_btnVersion4.setText(getResources().getString(R.string.words));
                }
                break;
            case 3:
                trueAnswer = 4;
                m_btnVersion4.setText(trueWord.getRus());
                if (!needMoreWords)
                {
                    m_btnVersion2.setText(fake1.getRus());
                    m_btnVersion3.setText(fake2.getRus());
                    m_btnVersion1.setText(fake3.getRus());
                } else
                {
                    m_btnVersion2.setText(getResources().getString(R.string.add));
                    m_btnVersion3.setText(getResources().getString(R.string.more));
                    m_btnVersion1.setText(getResources().getString(R.string.words));
                }
                break;
        }
        return trueAnswer;
    }

    public void waitAndAsk()
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(DELAY_MS);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        });
        t.start();
    }

    public void setResultText(boolean answer, String trueString)
    {
        if (answer)
        {
            m_txtQuesWord.setText(getResources().getString(R.string.right));
        } else
            m_txtQuesWord.setText(getResources().getString(R.string.wrong) + "(" + trueString + ")");
    }

    public void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
