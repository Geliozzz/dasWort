package oxbao.ru.dasWort;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;


public class ActivityEditDB extends ActionBarActivity {
    private ListView lvEdit;
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_db_layout);

        lvEdit = (ListView) findViewById(R.id.lv_edit);

        List<Word> list = ActivityMain.g_executor.getAllWordsFromDatabase();
        final AdapterEditArray adapter = new AdapterEditArray(getApplicationContext(), list);
        lvEdit.setAdapter(adapter);
        lvEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ActivityAddWord.class);
                intent.putExtra(Word.class.getCanonicalName(), adapter.getItem(i));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Word> list = ActivityMain.g_executor.getAllWordsFromDatabase();

        final AdapterEditArray adapter = new AdapterEditArray(getApplicationContext(), list);
        lvEdit.setAdapter(adapter);

    }
}
