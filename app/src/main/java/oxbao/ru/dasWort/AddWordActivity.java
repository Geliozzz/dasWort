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

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class AddWordActivity extends ActionBarActivity {
    private SqliteWordHelper db = new SqliteWordHelper(this);
    private Handler handler;
    private static final int ON_ENG = 1;
    private static final int ON_RUS = 2;
    private EditText edtEng;
    private EditText edtRus;
    private ProgressBar pb_wait;
    private Button btn_add_DB;
    private Button btn_trans_eng;
    private Button btn_trans_rus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_word_layout);
        initGUI();

    }

    private void initGUI(){
        edtEng = (EditText) findViewById(R.id.edtEng);
        edtRus = (EditText) findViewById(R.id.edtRus);
        btn_add_DB = (Button) findViewById(R.id.btn_write);
        btn_trans_eng = (Button) findViewById(R.id.btn_trans_on_Eng);
        btn_trans_rus = (Button) findViewById(R.id.btn_trans_on_RUS);
        pb_wait = (ProgressBar) findViewById(R.id.procB_wait_add);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String resp = msg.getData().getString("message");
                int type = msg.getData().getInt("type");
                if (type == ON_ENG) {
                    edtEng.setText(resp);
                }
                if (type == ON_RUS) {
                    edtRus.setText(resp);
                }
                pb_wait.setVisibility(View.INVISIBLE);
            }
        };

        btn_add_DB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eng = edtEng.getText().toString();
                String rus = edtRus.getText().toString();
                Word addWord = new Word(eng, rus);
                db.addWord(addWord);
                db.close();
                Toast toast = Toast.makeText(getApplication(), addWord.toString(), Toast.LENGTH_SHORT);
                toast.show();
                finish();

            }
        });
        btn_trans_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(ON_ENG, edtRus.getText().toString());
            }
        });

        btn_trans_rus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(ON_RUS, edtEng.getText().toString());
            }
        });
    }

    private void translate(final int type, String translate) {

        final String finalTranslate = translate;
        if (finalTranslate.equals("") || finalTranslate.equals(null)){
            return;
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Translate.setClientId(MainActivity.getClientId());
                Translate.setClientSecret(MainActivity.getClientSecret());
                try {
                    String transtatedText = "*";
                    if (type == ON_RUS) {
                        transtatedText = Translate.execute(finalTranslate, Language.GERMAN, Language.RUSSIAN);
                    }
                    if (type == ON_ENG) {
                        transtatedText = Translate.execute(finalTranslate, Language.RUSSIAN, Language.GERMAN);
                    }
                    threadMsg(transtatedText, type);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void threadMsg(String msg, int type) {
                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    b.putInt("type", type);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
            }
        });
        t.start();
        pb_wait.setVisibility(View.VISIBLE);
    }
}

