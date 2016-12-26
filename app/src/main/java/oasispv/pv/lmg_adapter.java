package oasispv.pv;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class lmg_adapter extends BaseAdapter {
    // Declare Variables
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<datosmod> lista;



    public lmg_adapter(Context context, ArrayList<datosmod> lista) {
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

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtmg;
        TextView txtmod;
        TextView txtguar;
        TextView txtsel;



        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_mod_guar, parent, false);

        // Locate the TextViews in listview_item.xml
        txtmg = (TextView) itemView.findViewById(R.id.txtmg);
        txtmod = (TextView) itemView.findViewById(R.id.txtmodo);
        txtguar = (TextView) itemView.findViewById(R.id.txtguar);
        txtsel = (TextView) itemView.findViewById(R.id.txtsel);


        // Capture position and set to the TextViews
        txtmg.setText(lista.get(position).desc);
        txtmod.setText(lista.get(position).modo);
        txtguar.setText(lista.get(position).guar);
        txtsel.setText(lista.get(position).def);

        if(lista.get(position).def.equals("S")) {

            itemView.setBackgroundResource(R.drawable.shapesel);

        }

        return itemView;
    }
}