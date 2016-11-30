package oasispv.pv;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class platillos extends AppCompatActivity {
    Button btnClosePopup;
    private DBhelper dbhelper;
    SQLiteDatabase dbs;
    String movi = variables.movi;
    String fase = variables.fase;
    Bundle extras;
    Button btncomanda;
    String[] vrest = new String[]{"CARNE ASADA","VINO DOS LUNAS","POLLO A LA PLANCHA","PIZZA","HAMBURGUESA"};
    String[] vcant = new String[]{"1","2","3","1","1"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platillos);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        /*TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        View view1 = getLayoutInflater().inflate(R.layout.tabicon, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.cocinero);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));*/

        btncomanda = (Button) findViewById(R.id.btncomand);
        btncomanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            popup();

            }
        });
        dbhelper = new DBhelper(getApplicationContext());
        dbs = dbhelper.getWritableDatabase();
        ActionBar backbtn = getSupportActionBar();
        backbtn.setDisplayHomeAsUpEnabled(true);

        scrollcat1();
    }



    private void scrollcat1() {

        LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.laymenu);
        LinearLayout btnstmp = (LinearLayout) findViewById(R.id.laybtns);
        btnstmp.removeAllViews();
        btnsContainer.removeAllViews();
        //Crea botons dinamicamente.
        int contl = 1;
        Boolean ban = false;
        LinearLayout contenedor1=null;

        //Crea botons dinamicamente.

           String query = "SELECT CAT1,CAT1_DESC FROM "+DBhelper.TABLE_PVCAT1+" WHERE CAT1_MOVI='" + movi + "' AND CAT1_FASE='" + fase + "' ";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.moveToFirst()) {
            do {
                if (!ban) {
                    ban = true;
                    contenedor1 = new LinearLayout(this);
                    contenedor1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                    contenedor1.setOrientation((LinearLayout.HORIZONTAL));
                    btnsContainer.addView(contenedor1);
                }
                Button btn = new Button(this);
                List<String> data = new ArrayList<String>();

                btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
                btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT1_DESC)));

                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT1)));
                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT1_DESC)));
                btn.setTag(data);

                contenedor1.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button btn = (Button)v;
                        String dato1 = ((List<String>)v.getTag()).get(0).toString();
                        String dato2 = ((List<String>)v.getTag()).get(1).toString();
                        scrollcat2(dato1,dato2);
                    }
                });
                contl = contl + 1;
                if (contl == 6) {
                    contl = 1;
                    ban = false;
                }

            } while (rs.moveToNext());
        }

            }




    private void scrollcat2(final String cat1,final String texto) {

        LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.laymenu);
        LinearLayout btnstmp = (LinearLayout) findViewById(R.id.laybtns);
        btnsContainer.removeAllViews();
        //Crea botons dinamicamente.
        int contl = 1;
        Boolean ban = false;
        LinearLayout contenedor2=null;

        //Botones de objeto seleccionado.
        Button btntmp = new Button(this);
        btntmp.setLayoutParams(new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.MATCH_PARENT));
        btntmp.setText(texto);
        btntmp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        btntmp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        btntmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;

                scrollcat1();

            }
        });
        btnstmp.addView(btntmp);

        String query = "SELECT CAT2,CAT2_DESC FROM "+DBhelper.TABLE_PVCAT2+" WHERE CAT2_MOVI='" + movi + "' AND CAT2_FASE='" + fase + "' AND CAT2_CAT1='"+cat1+"' ";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.moveToFirst()) {
            do {
                if (!ban) {
                    ban = true;
                    contenedor2 = new LinearLayout(this);
                    contenedor2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                    contenedor2.setOrientation((LinearLayout.HORIZONTAL));
                    btnsContainer.addView(contenedor2);
                }
                Button btn = new Button(this);
                List<String> data = new ArrayList<String>();
                btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
                btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT2_DESC)));

                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT2)));
                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT2_DESC)));
                btn.setTag(data);
                contenedor2.addView(btn);


                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button btn = (Button)v;
                        String dato1 = ((List<String>)v.getTag()).get(0).toString();
                        String dato2 = ((List<String>)v.getTag()).get(1).toString();
                        scrollplatillos(cat1,dato1,dato2,texto);

                    }
                });
                contl = contl + 1;
                if (contl == 6) {
                    contl = 1;
                    ban = false;
                }

            } while (rs.moveToNext());
        }
    }

    private void scrollplatillos(final String cat1,String cat2, String texto,final String texto2) {
        LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.laymenu);
        LinearLayout btnstmp = (LinearLayout) findViewById(R.id.laybtns);
        btnsContainer.removeAllViews();
        //Crea botons dinamicamente.
        int contl = 1;
        Boolean ban = false;
        LinearLayout contenedor = null;

        //Botones de objeto seleccionado.
        Button btntmp = new Button(this);
        btntmp.setLayoutParams(new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.MATCH_PARENT));
        btntmp.setText(texto);
        btntmp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        btntmp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        btntmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollcat2(cat1,texto2);
            }
        });
        btnstmp.addView(btntmp);



        String query = "SELECT PM_PRODUCTO,PM_PRODUCTO_DESC FROM " + DBhelper.TABLE_PVMENUS + " WHERE PM_MOVI='" + movi + "' AND PM_FASE='" + fase + "' AND PM_CAT1='" + cat1 + "' AND PM_CAT2='"+cat2+"' ";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.moveToFirst()) {
            do {
                if (!ban) {
                    ban = true;
                    contenedor = new LinearLayout(this);
                    contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                    contenedor.setOrientation((LinearLayout.HORIZONTAL));
                    btnsContainer.addView(contenedor);
                }
                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
                btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_PM_PRODUCTO_DESC)));
                btn.setTag(rs.getString(rs.getColumnIndex(DBhelper.KEY_PM_PRODUCTO)));
                contenedor.addView(btn);
                btn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(getApplicationContext(), "Agregado", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                contl = contl + 1;
                if (contl == 6) {
                    contl = 1;
                    ban = false;
                }

                //Va agregegando botones al contenedor.


            }while (rs.moveToNext());

        }
    }

    private void popup() {
         final PopupWindow pwindo;
        ListView listprd;

        try {

            LayoutInflater inflat = (LayoutInflater) platillos.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflat.inflate(R.layout.poplay,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 500,900, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            listprd = (ListView) layout.findViewById(R.id.listprd);

            listrestadp adapter = new listrestadp(platillos.this,vrest,vcant);
            listprd.setAdapter(adapter);
            listprd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Toast.makeText(getApplicationContext(),"Producto",Toast.LENGTH_LONG).show();
                }
            });

            btnClosePopup = (Button) layout.findViewById(R.id.btn_x_pop);
            btnClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}





