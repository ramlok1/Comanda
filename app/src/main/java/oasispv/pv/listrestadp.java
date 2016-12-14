package oasispv.pv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class listrestadp extends BaseAdapter {
    // Declare Variables
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<datoscomanda> lista;

    public listrestadp(Context context, ArrayList<datoscomanda> lista) {
        this.context = context;
        this.lista=lista;
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

    public View getView(final int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtprd;

        TextView txtcomensal;
        TextView txtiempo;
        TextView txtnota;
        Button btnxpr;


                //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View itemView = inflater.inflate(R.layout.lay_comanda_r, parent, false);

        // Locate the TextViews in listview_item.xml

        txtprd = (TextView) itemView.findViewById(R.id.txtprd);

        txtcomensal = (TextView) itemView.findViewById(R.id.txtcomensal);
        txtiempo = (TextView) itemView.findViewById(R.id.txtiempo);
        btnxpr = (Button) itemView.findViewById(R.id.btndelpr);
        txtnota = (TextView) itemView.findViewById(R.id.txtnota);

        // Capture position and set to the TextViews
        txtprd.setText(lista.get(position).prdesc);

        txtcomensal.setText(Integer.toString(lista.get(position).comensal));
        txtiempo.setText(Integer.toString(lista.get(position).tiempo));
        txtnota.setText(lista.get(position).nota);
        btnxpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBhelper dbhelper = new DBhelper(context);
                SQLiteDatabase dbs = dbhelper.getWritableDatabase();
                dbs.delete(DBhelper.TABLE_COMANDA, DBhelper.KEY_ID+"="+lista.get(position).idpr, null);
                lista.remove(position);
                listrestadp.this.notifyDataSetChanged();
            }
        });

        return itemView;
    }
}