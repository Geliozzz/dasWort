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


public class ActivityLearn extends ActionBarActivity
{
    private  Lerner m_lerner;

 //   private ArrayList<Word> wordArrayList;
    private TextView txtQuesWord;
    private Button btnVersion1;
    private Button btnVersion2;
    private Button btnVersion3;
    private Button btnVersion4;

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
        m_lerner.start();

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
                            }
                        }
                );
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
    }



    private void initGUI()
    {
        txtQuesWord = (TextView) findViewById(R.id.tv_quest_word);
        btnVersion1 = (Button) findViewById(R.id.btn_version_1);
        btnVersion2 = (Button) findViewById(R.id.btn_version_2);
        btnVersion3 = (Button) findViewById(R.id.btn_version_3);
        btnVersion4 = (Button) findViewById(R.id.btn_version_4);
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                m_lerner.newQuestion();
            }
        };

        btnVersion1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_lerner.answer(BUTTON_1);
            }
        });
        btnVersion2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_lerner.answer(BUTTON_2);
            }
        });
        btnVersion3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_lerner.answer(BUTTON_3);
            }
        });
        btnVersion4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                m_lerner.answer(BUTTON_4);
            }
        });


    }

    public int initButtonForQuest(int variant, Word trueWord, Word fake1, Word fake2,
                                   Word fake3, boolean needMoreWords)
    {
        txtQuesWord.setText(trueWord.getEng());
        int trueAnswer = 1;
        switch (variant)
        {
            case 0:
                btnVersion1.setText(trueWord.getRus());
                trueAnswer = 1;
                if (!needMoreWords)
                {
                    btnVersion2.setText(fake1.getRus());
                    btnVersion3.setText(fake2.getRus());
                    btnVersion4.setText(fake3.getRus());
                } else
                {
                    btnVersion2.setText(getResources().getString(R.string.add));
                    btnVersion3.setText(getResources().getString(R.string.more));
                    btnVersion4.setText(getResources().getString(R.string.words));
                }

                break;
            case 1:
                trueAnswer = 2;
                btnVersion2.setText(trueWord.getRus());
                if (!needMoreWords)
                {
                    btnVersion1.setText(fake1.getRus());
                    btnVersion3.setText(fake2.getRus());
                    btnVersion4.setText(fake3.getRus());
                } else
                {
                    btnVersion1.setText(getResources().getString(R.string.add));
                    btnVersion3.setText(getResources().getString(R.string.more));
                    btnVersion4.setText(getResources().getString(R.string.words));
                }
                break;
            case 2:
                trueAnswer = 3;
                btnVersion3.setText(trueWord.getRus());
                if (!needMoreWords)
                {
                    btnVersion2.setText(fake1.getRus());
                    btnVersion1.setText(fake2.getRus());
                    btnVersion4.setText(fake3.getRus());
                } else
                {
                    btnVersion2.setText(getResources().getString(R.string.add));
                    btnVersion1.setText(getResources().getString(R.string.more));
                    btnVersion4.setText(getResources().getString(R.string.words));
                }
                break;
            case 3:
                trueAnswer = 4;
                btnVersion4.setText(trueWord.getRus());
                if (!needMoreWords)
                {
                    btnVersion2.setText(fake1.getRus());
                    btnVersion3.setText(fake2.getRus());
                    btnVersion1.setText(fake3.getRus());
                } else
                {
                    btnVersion2.setText(getResources().getString(R.string.add));
                    btnVersion3.setText(getResources().getString(R.string.more));
                    btnVersion1.setText(getResources().getString(R.string.words));
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
            txtQuesWord.setText(getResources().getString(R.string.right));
        } else
            txtQuesWord.setText(getResources().getString(R.string.wrong) + "(" + trueString + ")");
    }

}
