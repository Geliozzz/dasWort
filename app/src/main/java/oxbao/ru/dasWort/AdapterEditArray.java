package oxbao.ru.dasWort;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class AdapterEditArray extends ArrayAdapter<Word> {
    private Context context;
    private List<Word> values;

    public AdapterEditArray(Context context, List<Word> objects) {
        super(context, R.layout.edit_adapter, objects);
        this.context = context;
        Collections.sort(objects);
        this.values = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.edit_adapter, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(values.get(position).toString());

        return rowView;
    }


}
