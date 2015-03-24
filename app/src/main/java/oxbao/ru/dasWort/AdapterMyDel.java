package oxbao.ru.dasWort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class AdapterMyDel extends ArrayAdapter<Word> {
    private Context context;
    private List<Word> values;
    private HashSet<Word> wordHashSet = new HashSet<Word>();
    int lay_res = R.layout.del_adapter;


    public AdapterMyDel(Context context, List<Word> objects) {
        super(context, R.layout.del_adapter, objects);
        Collections.sort(objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(lay_res, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText("[" + values.get(position).getCount()+ "] " +  values.get(position).toString());
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.chbx_del);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkBox.isChecked()){
                    wordHashSet.add(values.get(position));
                } else {
                    if (wordHashSet.contains(values.get(position))){
                        wordHashSet.remove(values.get(position));
                    }
                }
            }
        });

        return rowView;
    }

    public HashSet<Word> getWordHashSet() {
        return wordHashSet;
    }
}
