package oasispv.pv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
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
            popup_comanda();

            }
        });
        dbhelper = new DBhelper(getApplicationContext());
        dbs = dbhelper.getWritableDatabase();
        ActionBar backbtn = getSupportActionBar();
        backbtn.setDisplayHomeAsUpEnabled(true);
        muestra_cmdtmp();
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
                List<String> data = new ArrayList<>();

                btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
                btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT1_DESC)));

                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT1)));
                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_CAT1_DESC)));
                btn.setTag(data);

                contenedor1.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
        btnstmp.removeAllViews();
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



        String query = "SELECT PM_PRODUCTO,PM_PRODUCTO_DESC,PM_MODI FROM " + DBhelper.TABLE_PVMENUS + " WHERE PM_MOVI='" + movi + "' AND PM_FASE='" + fase + "' AND PM_CAT1='" + cat1 + "' AND PM_CAT2='"+cat2+"' ";

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
                List<String> data = new ArrayList<>();
                btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
                btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_PM_PRODUCTO_DESC)));
                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_PM_PRODUCTO)));
                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_PM_PRODUCTO_DESC)));
                data.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_PM_MODI)));
                btn.setTag(data);
                contenedor.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String dato1 = ((List<String>)v.getTag()).get(0).toString();
                    String dato2 = ((List<String>)v.getTag()).get(1).toString();
                    String dato3 = ((List<String>)v.getTag()).get(2).toString();

                    if (variables.modipv.equalsIgnoreCase("S")) {
                        popup_window(dato1, dato2,dato3);
                    }else{
                        inserta_producto(dato1,dato2,1,1);
                        muestra_cmdtmp();
                    }




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

    private void popup_comanda() {
         final PopupWindow pwindo;
        String query = "SELECT ID,PRDESC,CANTIDAD,COMENSAL,TIEMPO,NOTA FROM " + DBhelper.TABLE_COMANDA + " WHERE MESA='" + variables.mesa + "'  AND STATUS='A' AND SESION='"+variables.sesion+"'";
        Cursor rs = dbs.rawQuery(query, null);
        ArrayList<datoscomanda> datos = new ArrayList<>();

        if (rs.moveToFirst()) {
            do {
                try {
                    datos.add(new datoscomanda(
                            rs.getString(rs.getColumnIndex(DBhelper.CMD_PRDESC)),
                            rs.getInt(rs.getColumnIndex(DBhelper.CMD_CANTIDAD)),
                            rs.getInt(rs.getColumnIndex(DBhelper.CMD_COMENSAL)),
                            rs.getInt(rs.getColumnIndex(DBhelper.CMD_TIEMPO)),
                            rs.getInt(rs.getColumnIndex(DBhelper.KEY_ID)),
                            rs.getString(rs.getColumnIndex(DBhelper.CMD_NOTA))
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }while (rs.moveToNext());

            LayoutInflater inflat = (LayoutInflater) platillos.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflat.inflate(R.layout.poplay,
                    (ViewGroup) findViewById(R.id.popup_element));

            ListView laycmdread = (ListView) layout.findViewById(R.id.listprd);
            listrestadp adapter = new listrestadp(platillos.this,datos);
            laycmdread.setAdapter(adapter);
            int x = findViewById(R.id.laybtns).getWidth();
            pwindo = new PopupWindow(layout, x,900, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            btnClosePopup = (Button) layout.findViewById(R.id.btncmdcont);
            btnClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                    muestra_cmdtmp();
                }
            });

        }




    }
    private void popup_window( final String dato1,final String dato2, final String modi) {

        final PopupWindow pwindo;


            LayoutInflater inflat = (LayoutInflater) platillos.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflat.inflate(R.layout.popup_window,
                    (ViewGroup) findViewById(R.id.popup_window_lp));




            pwindo = new PopupWindow(layout, 600,450, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnClosePopup = (Button) layout.findViewById(R.id.btnxpop);
            btnClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();

                }
            });
        LinearLayout contenedorpop = (LinearLayout) layout.findViewById(R.id.laypopbtn);
        LinearLayout contenedor = new LinearLayout(this);
        contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
        contenedor.setOrientation((LinearLayout.HORIZONTAL));
        contenedorpop.addView(contenedor);

        final EditText txtcomensal= (EditText) layout.findViewById(R.id.txtvncomensal);



        /////////////////TIEMPO

        final TextView txttiempo= (TextView) layout.findViewById(R.id.txtvntiempo);
        txttiempo.setText("1");
        Button btnmenost = (Button) layout.findViewById(R.id.btnmenost);
        btnmenost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txttiempo.getText().equals("1")) {
                    txttiempo.setText("1");
                }else{
                    int tiempo = Integer.parseInt(txttiempo.getText().toString());
                    tiempo=tiempo-1;
                    txttiempo.setText(Integer.toString(tiempo));
                }

            }
        });
        Button  btnmast= (Button) layout.findViewById(R.id.btnmast);
        btnmast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txttiempo.getText().equals("3")) {
                    txttiempo.setText("3");
                }else{
                    int tiempo = Integer.parseInt(txttiempo.getText().toString());
                    tiempo=tiempo+1;
                    txttiempo.setText(Integer.toString(tiempo));
                }

            }
        });

        Button  btncont= (Button) layout.findViewById(R.id.btncont);
        btncont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 variables.comensal = Integer.parseInt(txtcomensal.getText().toString());
                 variables.tiempo = Integer.parseInt(txttiempo.getText().toString());
                if (modi.equalsIgnoreCase("N")) {
                    inserta_producto(dato1, dato2, variables.comensal, variables.tiempo);
                    muestra_cmdtmp();
                    pwindo.dismiss();
                }else{

                    modificadores(dato1,dato2,modi,layout,pwindo);
                }


            }
        });



        }


    private void inserta_producto(String prid,String prdesc, int comensal,int tiempo){

        ContentValues cv = new ContentValues();
        cv.put(DBhelper.CMD_SESION, variables.sesion);
        cv.put(DBhelper.CMD_MESA,variables.mesa);
        cv.put(DBhelper.CMD_TRANSA,variables.cmd);
        cv.put(DBhelper.CMD_PRID, prid);
        cv.put(DBhelper.CMD_PRDESC, prdesc);
        cv.put(DBhelper.CMD_CANTIDAD, 1);
        cv.put(DBhelper.CMD_COMENSAL, comensal);
        cv.put(DBhelper.CMD_TIEMPO, tiempo);
        cv.put(DBhelper.CMD_STATUS, "A");
        dbs.insert(DBhelper.TABLE_COMANDA, null, cv);

        String query = "SELECT MAX(id) FROM " + DBhelper.TABLE_COMANDA + " WHERE MESA='" + variables.mesa + "'  AND STATUS='A' AND SESION='"+variables.sesion+"'";
        Cursor c = dbs.rawQuery(query, null);
        c.moveToFirst();
        variables.max_id = c.getInt(0);




    }
    private void inserta_modificadores(String prid){
        String nota="-";

        String query = "SELECT * FROM " + DBhelper.TABLE_TMP_PVMODCG + " WHERE CG_COMANDA='"
                + variables.cmd + "'  AND CG_PRODUCTO='"+prid+"'";

        Cursor rs = dbs.rawQuery(query, null);

        if (rs.moveToFirst()) {
            do {

                try {
                    ContentValues cv = new ContentValues();
                    cv.put(DBhelper.CG_COMANDA, variables.cmd);
                    cv.put(DBhelper.CG_COMANDA_DET, variables.max_id);
                    cv.put(DBhelper.CG_PRODUCTO, prid);
                    cv.put(DBhelper.CG_GRUPO, rs.getString(rs.getColumnIndex("CG_GRUPO")));
                    cv.put(DBhelper.CG_MODO, rs.getString(rs.getColumnIndex("CG_MODO")));
                    cv.put(DBhelper.CG_DESC, rs.getString(rs.getColumnIndex("CG_DESC")));
                    cv.put(DBhelper.CG_SELECCION, rs.getString(rs.getColumnIndex("CG_SELECCION")));
                    dbs.insert(DBhelper.TABLE_PVMODCG, null, cv);

                    if(rs.getString(rs.getColumnIndex("CG_SELECCION")).equals("S")){
                        nota=nota+","+rs.getString(rs.getColumnIndex("CG_DESC"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (rs.moveToNext());
        }
     dbs.delete(DBhelper.TABLE_TMP_PVMODCG,"CG_COMANDA="+variables.cmd,null);

        ContentValues n = new ContentValues();
        n.put(DBhelper.CMD_NOTA, nota);
        dbs.update(DBhelper.TABLE_COMANDA,n,"id="+variables.max_id,null);





    }

    private void muestra_cmdtmp(){

        String query = "SELECT ID,PRDESC,CANTIDAD,COMENSAL,TIEMPO,NOTA FROM " + DBhelper.TABLE_COMANDA + " WHERE MESA='" + variables.mesa + "'  AND STATUS='A' AND SESION='"+variables.sesion+"'";
        Cursor rs = dbs.rawQuery(query, null);
        ArrayList<datoscomanda> datos = new ArrayList<>();

        if (rs.moveToFirst()) {
            do {

                try {
                    datos.add(new datoscomanda(
                            rs.getString(rs.getColumnIndex(DBhelper.CMD_PRDESC)),
                            rs.getInt(rs.getColumnIndex(DBhelper.CMD_CANTIDAD)),
                            rs.getInt(rs.getColumnIndex(DBhelper.CMD_COMENSAL)),
                            rs.getInt(rs.getColumnIndex(DBhelper.CMD_TIEMPO)),
                            rs.getInt(rs.getColumnIndex(DBhelper.KEY_ID)),
                            rs.getString(rs.getColumnIndex(DBhelper.CMD_NOTA))
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }while (rs.moveToNext());

            /*LayoutInflater inflat = (LayoutInflater) platillos.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflat.inflate(R.layout.activity_platillos,
                    (ViewGroup) findViewById(R.id.laycmdread));*/

            ListView laycmdread = (ListView) findViewById(R.id.listcmdread);
            lcomandar_adapter adapter = new lcomandar_adapter(platillos.this,datos);
            laycmdread.setAdapter(adapter);

        }
        else {
            ListView laycmdread = (ListView) findViewById(R.id.listcmdread);
            lcomandar_adapter adapter = new lcomandar_adapter(platillos.this,datos);
            laycmdread.setAdapter(adapter);

        }

    }

    private void modificadores(final String pr,final String dato2,final String modi, final View layout, final PopupWindow pwindo) {

        LinearLayout btnsContainer = (LinearLayout) layout.findViewById(R.id.laypopbtn);
        btnsContainer.removeAllViews();

        LinearLayout contenedor1 = null;
        LinearLayout contenedor2 = null;

        contenedor1 = new LinearLayout(this);
        ListView listamg = new ListView(this);
        listamg.setId(R.id.listamg);
        listamg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        contenedor1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300));
        contenedor1.setOrientation((LinearLayout.HORIZONTAL));
        contenedor1.setId(R.id.laylistamod);
        contenedor1.addView(listamg);
        contenedor2 = new LinearLayout(this);
        contenedor2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
        contenedor2.setOrientation((LinearLayout.HORIZONTAL));
        btnsContainer.addView(contenedor1);
        btnsContainer.addView(contenedor2);

        ////boton atras
        Button btnatras = new Button(this);
        btnatras.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
        btnatras.setText("ATRAS");
        contenedor2.addView(btnatras);
        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwindo.dismiss();
                popup_window(pr,dato2,modi);


            }
        });


        String query = "SELECT GP_GRUPO,GP_GRUPO_DESC FROM " + DBhelper.TABLE_PVPRODUCTOSMODOSG + " WHERE GP_PRODUCTO='" + pr + "'";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.moveToFirst()) {
            String mod1 = rs.getString(rs.getColumnIndex(DBhelper.GP_GRUPO));
            do {
                String grupo = rs.getString(rs.getColumnIndex(DBhelper.GP_GRUPO));
                String gdesc= rs.getString(rs.getColumnIndex(DBhelper.GP_GRUPO_DESC));

                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
                btn.setText(gdesc);
                btn.setTag(grupo) ;
                contenedor2.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button)v;
                        String txt = b.getTag().toString();
                        muestra_modificador(pr,txt,layout);


                    }
                });

                //Va agregegando botones al contenedor.
                muestra_modificador(pr,grupo,layout);

            }while (rs.moveToNext());
                        muestra_modificador(pr,mod1,layout);
        }
        ////boton guarni
        Button btnguar = new Button(this);
        btnguar.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
        btnguar.setText("COMPLEMENTOS");
        contenedor2.addView(btnguar);
        btnguar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        ////boton continuar
        Button btncont = new Button(this);
        btncont.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
        btncont.setText("CONTINUAR");
        contenedor2.addView(btncont);
        btncont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserta_producto(pr, dato2, variables.comensal, variables.tiempo);
                inserta_modificadores(pr);
                muestra_cmdtmp();
                pwindo.dismiss();

            }
        });

    }
    private void muestra_modificador(final String pr, final String mod, final View layout) {
        Cursor rs;

        String query = "SELECT MD_GRUPO,MD_MODO,MD_DESC,MD_DEFAULT FROM " + DBhelper.TABLE_PVMODOS + " WHERE MD_GRUPO='" + mod + "'";
        String querytmp = "SELECT CG_GRUPO,CG_MODO,CG_DESC,CG_SELECCION FROM " + DBhelper.TABLE_TMP_PVMODCG + " WHERE CG_COMANDA=" + variables.cmd + " AND CG_PRODUCTO='"+pr+"' AND CG_GRUPO='"+mod+"'";
        String manda ="SELECT MG_MANDAT FROM "+DBhelper.TABLE_PVMODOSG+" WHERE MG_GRUPO='"+mod+"'";

        Cursor m = dbs.rawQuery(manda,null);
        m.moveToFirst();
        variables.mandatory= m.getString(m.getColumnIndex(DBhelper.MG_MANDAT));

        ArrayList<datosmod> datos = new ArrayList<>();
        long countmd= DatabaseUtils.queryNumEntries(dbs,DBhelper.TABLE_TMP_PVMODCG,"CG_COMANDA="+variables.cmd+" AND CG_PRODUCTO='"+pr+"' AND CG_GRUPO='"+mod+"'");

        if (countmd>0){
             rs = dbs.rawQuery(querytmp, null);
        }else {
             rs = dbs.rawQuery(query, null);
        }
        if (rs.moveToFirst()) {
            do {

                try {
                    if (countmd>0) {
                        datos.add(new datosmod(
                                rs.getString(rs.getColumnIndex(DBhelper.CG_DESC)),
                                rs.getString(rs.getColumnIndex(DBhelper.CG_MODO)),
                                "NO",
                                rs.getString(rs.getColumnIndex(DBhelper.CG_SELECCION))
                       ));
                    }else {

                        // Inserta datos de grupo en tabla temporal
                        ContentValues cv = new ContentValues();
                        cv.put(DBhelper.CG_COMANDA, variables.cmd);
                        cv.put(DBhelper.CG_PRODUCTO, pr);
                        cv.put(DBhelper.CG_GRUPO, rs.getString(rs.getColumnIndex(DBhelper.MD_GRUPO)));
                        cv.put(DBhelper.CG_MODO, rs.getString(rs.getColumnIndex(DBhelper.MD_MODO)));
                        cv.put(DBhelper.CG_DESC, rs.getString(rs.getColumnIndex(DBhelper.MD_DESC)));
                        cv.put(DBhelper.CG_SELECCION, rs.getString(rs.getColumnIndex(DBhelper.MD_DEFAULT)));
                        dbs.insert(DBhelper.TABLE_TMP_PVMODCG, null, cv);

                        //Inserta valores en Array para enviar a adapter de lista
                        datos.add(new datosmod(
                                rs.getString(rs.getColumnIndex(DBhelper.MD_DESC)),
                                rs.getString(rs.getColumnIndex(DBhelper.MD_MODO)),
                                "NO",
                                rs.getString(rs.getColumnIndex(DBhelper.MD_DEFAULT))
                        ));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }while (rs.moveToNext());



            final ListView laymod = (ListView) layout.findViewById(R.id.listamg);
            laymod.setAdapter(null);

            lmg_adapter adapter = new lmg_adapter(platillos.this,datos);
            laymod.setAdapter(adapter);


            laymod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView txtmodo = (TextView) view.findViewById(R.id.txtmodo);
                    String modo = txtmodo.getText().toString();

                    ContentValues cv = new ContentValues();
                    if(variables.mandatory.equals("S")){
                        ContentValues v =new ContentValues();
                        v.put(DBhelper.CG_SELECCION,"N");
                        dbs.update(DBhelper.TABLE_TMP_PVMODCG,v,"CG_COMANDA="+variables.cmd+" AND CG_PRODUCTO='"+pr+"' AND CG_GRUPO='"+mod+"'",null);
                    }
                    cv.put(DBhelper.CG_SELECCION,"S");
                    dbs.update(DBhelper.TABLE_TMP_PVMODCG,cv,"CG_COMANDA="+variables.cmd+" AND CG_PRODUCTO='"+pr+"' AND CG_GRUPO='"+mod+"' AND CG_MODO='"+modo+"'",null);
                    muestra_modificador(pr,mod,layout);
                }
            });

        }

    }

}






