package oasispv.pv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class lcomandar_adapter extends BaseAdapter {
    // Declare Variables
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<datoscomanda> lista;


    public lcomandar_adapter(Context context, ArrayList<datoscomanda> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtprd;

        TextView txtcomensal;
        TextView txtiempo;
        TextView txtnota;
        TextView txtprecio;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lay_popup, parent, false);

        // Locate the TextViews in listview_item.xml
        txtprd = (TextView) itemView.findViewById(R.id.txtprd);

        txtcomensal = (TextView) itemView.findViewById(R.id.txtcomensal);
        txtiempo = (TextView) itemView.findViewById(R.id.txtiempo);
        txtnota = (TextView) itemView.findViewById(R.id.txtnota);
        txtprecio = (TextView) itemView.findViewById(R.id.txtprecio);

        // Capture position and set to the TextViews
        txtprd.setText(lista.get(position).prdesc);
        txtcomensal.setText(Integer.toString(lista.get(position).comensal));
        txtiempo.setText(Integer.toString(lista.get(position).tiempo));
        txtnota.setText(lista.get(position).nota);
        txtprecio.setText(Float.toString(lista.get(position).precio));
        // Totaliza precios


        return itemView;
    }
}