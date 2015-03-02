package oxbao.ru.dasWort;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ActivityAddWord extends ActionBarActivity
{
    private Handler handler;

    private EditText edtEng;
    private EditText edtRus;
    private ProgressBar pb_wait;
    private Button btn_add_DB;
    private Button btn_trans_eng;
    private Button btn_trans_rus;

    private Word m_WordRecieve;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_word_layout);
        initGUI();
        m_WordRecieve = (Word) getIntent().getParcelableExtra(Word.class.getCanonicalName()); // Recive object
        if (m_WordRecieve == null)
        {

        } else
        {
            edtEng.setText(m_WordRecieve.getEng());
            edtRus.setText(m_WordRecieve.getRus());
        }
    }

    private void initGUI()
    {
        edtEng = (EditText) findViewById(R.id.edtEng);
        edtRus = (EditText) findViewById(R.id.edtRus);
        btn_add_DB = (Button) findViewById(R.id.btn_write);
        btn_trans_eng = (Button) findViewById(R.id.btn_trans_on_Eng);
        btn_trans_rus = (Button) findViewById(R.id.btn_trans_on_RUS);
        pb_wait = (ProgressBar) findViewById(R.id.procB_wait_add);


        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                String resp = msg.getData().getString("message");
                String type = msg.getData().getString("type");
                if (type.equals(LanguageEnum.ON_ENG.toString()))
                {
                    edtEng.setText(resp);
                }
                if (type.equals(LanguageEnum.ON_RUS.toString()))
                {
                    edtRus.setText(resp);
                }
                pb_wait.setVisibility(View.INVISIBLE);
            }
        };

        btn_add_DB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String eng = edtEng.getText().toString();
                String rus = edtRus.getText().toString();
                Word tmpWord;
                if (m_WordRecieve == null)
                {
                    Word addWord = new Word(eng, rus);
                    ActivityMain.g_executor.addWordToDataBase(addWord);
                    tmpWord = addWord;
                } else
                {
                    m_WordRecieve.setEng(eng);
                    m_WordRecieve.setRus(rus);
                    ActivityMain.g_executor.updateWordInDataBase(m_WordRecieve);
                    tmpWord = m_WordRecieve;
                }
                Toast toast = Toast.makeText(getApplication(), tmpWord.toString(), Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });
        btn_trans_eng.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ActivityMain.g_executor.translate(LanguageEnum.ON_ENG, edtRus.getText().toString());
            }
        });

        btn_trans_rus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ActivityMain.g_executor.translate(LanguageEnum.ON_RUS, edtEng.getText().toString());
            }
        });
    }

}

