package oasispv.pv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class listrestadp extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] titulos;
    String[] vcant;

    LayoutInflater inflater;

    public listrestadp(Context context, String[] titulos, String[] vcant) {
        this.context = context;
        this.titulos = titulos;
        this.vcant = vcant;
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtTitle;
        TextView txtcant;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lay_popup, parent, false);

        // Locate the TextViews in listview_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.txtprd);
        txtcant = (TextView) itemView.findViewById(R.id.txtcnt);

        // Capture position and set to the TextViews
        txtTitle.setText(titulos[position]);
        txtcant.setText(vcant[position]);

        return itemView;
    }
}