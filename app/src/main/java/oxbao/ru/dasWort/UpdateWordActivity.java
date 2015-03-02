package oxbao.ru.dasWort;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


/**Activity need for upgrade word in database*/
public class UpdateWordActivity extends ActionBarActivity {
    final String LOG_TAG = "myLogs";
    private EditText edt_eng;
    private EditText edt_rus;
    private Button btn_update_db;
    private Button btn_translate_on_eng;
    private Button btn_translate_on_rus;
    private ProgressBar pb_wait;
    private Word word;
    private   SqliteWordHelper db = new SqliteWordHelper(this);
    private Handler handler;

    private static final int ON_ENG = 1;
    private static final int ON_RUS = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_word_layout);
        Log.d(LOG_TAG, "getParcelExtra");
        word = (Word) getIntent().getParcelableExtra(Word.class.getCanonicalName()); // Recive object
        /*Can word be null?*/
        /* From another activity send object Word . this object not can be null*/
        if (word == null){
            finish();
        }
        initGUI();
        edt_eng.setText(word.getEng());
        edt_rus.setText(word.getRus());

    }

    private void initGUI(){
        edt_eng = (EditText)findViewById(R.id.edtRus);
        edt_rus = (EditText)findViewById(R.id.edtEng);
        btn_update_db = (Button) findViewById(R.id.btn_write);
        btn_translate_on_eng = (Button)findViewById(R.id.btn_trans_on_Eng);
        btn_translate_on_rus = (Button)findViewById(R.id.btn_trans_on_RUS);
        pb_wait = (ProgressBar) findViewById(R.id.procB_wait_add);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String resp = msg.getData().getString("message");
                int type = msg.getData().getInt("type");
                if (type == ON_ENG){
                    edt_eng.setText(resp);
                }
                if (type == ON_RUS){
                    edt_rus.setText(resp);
                }
                pb_wait.setVisibility(View.INVISIBLE);
            }
        };

        btn_update_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word.setRus(edt_rus.getText().toString());
                word.setEng(edt_eng.getText().toString());
                db.updateWord(word);
                db.close();
                Toast toast = Toast.makeText(getApplicationContext(), word.toString() , Toast.LENGTH_SHORT);
                toast.show();
                finish(); // need for close activity
            }
        });

        btn_translate_on_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(ON_ENG, edt_rus.getText().toString());

            }
        });

        btn_translate_on_rus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(ON_RUS, edt_eng.getText().toString());

            }
        });
    }

    private void translate(final int type, String translate){
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
                    String  transtatedText = "*";
                    if (type == ON_RUS){
                        transtatedText = Translate.execute(finalTranslate, Language.ENGLISH, Language.RUSSIAN);
                    }
                    if( type == ON_ENG){
                        transtatedText = Translate.execute(finalTranslate, Language.RUSSIAN, Language.ENGLISH);
                                            }
                    threadMsg(transtatedText, type);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            private void threadMsg(String msg , int type){
                if (!msg.equals(null) && !msg.equals("")){
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
