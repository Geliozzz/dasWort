package oxbao.ru.dasWort;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashSet;
import java.util.List;

public class ActivityDeleteDB extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_db_layout);

        ListView lvDel = (ListView) findViewById(R.id.lv_delete);
        Button btn_delete = (Button)findViewById(R.id.btn_deleteWords);

       List<Word> list = ActivityMain.g_executor.getAllWordsFromDatabase();
       final AdapterMyDel adapter = new AdapterMyDel(getApplicationContext(), list);
       lvDel.setAdapter(adapter);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashSet<Word> set = adapter.getWordHashSet();
                if (set != null){
                    for(Word word : set){
                        ActivityMain.g_executor.deleteWordFromDataBase(word);
                    }
                }
                ActivityDeleteDB.this.finish();
            }
        });
    }
}
