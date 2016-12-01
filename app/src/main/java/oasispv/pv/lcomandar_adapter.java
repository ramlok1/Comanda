package oasispv.pv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Usuario on 01/12/2016.
 */

public class lcomandar_adapter extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] prdesc;
    Integer[] cant;
    Integer[] comensal;
    Integer[] tiempo;

    LayoutInflater inflater;

    public lcomandar_adapter(Context context, String[] prdesc, Integer[] cant, Integer[] comensal, Integer[] tiempo) {
        this.context = context;
        this.prdesc = prdesc;
        this.cant = cant;
        this.comensal = comensal;
        this.tiempo = tiempo;
    }

    @Override
    public int getCount() {
        return prdesc.length;
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
        TextView txtprd;
        TextView txtcnt;
        TextView txtcomensal;
        TextView txtiempo;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lay_popup, parent, false);

        // Locate the TextViews in listview_item.xml
        txtprd = (TextView) itemView.findViewById(R.id.txtprd);
        txtcnt = (TextView) itemView.findViewById(R.id.txtcnt);
        txtcomensal = (TextView) itemView.findViewById(R.id.txtcomensal);
        txtiempo = (TextView) itemView.findViewById(R.id.txtiempo);

        // Capture position and set to the TextViews
        txtprd.setText(prdesc[position]);
        txtcnt.setText(cant[position]);
        txtcomensal.setText(comensal[position]);
        txtiempo.setText(tiempo[position]);

        return itemView;
    }
}