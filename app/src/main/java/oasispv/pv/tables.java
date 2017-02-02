package oasispv.pv;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class tables extends AppCompatActivity {

    private DBhelper dbhelper;
    private SQLiteDatabase dbs;
    private Connection conexion;
    ConnectOra db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbhelper = DBhelper.getInstance(getApplicationContext());
        dbs = dbhelper.getWritableDatabase();
        db = new ConnectOra(variables.ip,variables.cn,variables.un,variables.pw);


        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_tables);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(variables.mesero);
        setSupportActionBar(toolbar);

        Button btnxsesion = (Button) findViewById(R.id.btnxsesion);
        btnxsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* long count= DatabaseUtils.queryNumEntries(dbs,DBhelper.TABLE_COMANDAENC," CE_MESERO='"+variables.mesero+"' AND CE_STATUS!='C'");
                if (count>0){
                    Toast.makeText(getApplicationContext(),"Aun tiene mesas abiertas",Toast.LENGTH_LONG).show();
                }else{*/

                    final AlertDialog.Builder msgcerrar = new AlertDialog.Builder(tables.this);
                    msgcerrar.setTitle("Confirmar cierre");
                    msgcerrar.setMessage("Seguro desea cerrar la sesion?");
                    msgcerrar.setCancelable(false);
                    msgcerrar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface msgcerrar, int id) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                });
                    msgcerrar.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface msgcerrar, int id) {
                            msgcerrar.dismiss();
                        }
                    });
                    msgcerrar.show();//muestra mensaje
               // }



            }

        });


        new tables.datosmesas().execute();




    }


    private void leermesas(){



            LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.btnlmaster);
            btnsContainer.removeAllViews();
            //Crea botons dinamicamente.
            int contl = 1;
            Boolean ban = false;
            LinearLayout contenedor = null;


            String query = "SELECT MESA,MESA_NAME,HABI,MESA_MESERO FROM " + DBhelper.TABLE_PVMESA+" ORDER BY MESA" ;

            Cursor rs = dbs.rawQuery(query, null);
            if (rs.getCount() > 0) {
                rs.moveToFirst();
                do {
                    String mesero = "N";
                    if (!ban) {
                        ban = true;
                        contenedor = new LinearLayout(this);
                        contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                        contenedor.setOrientation((LinearLayout.HORIZONTAL));
                        btnsContainer.addView(contenedor);
                    }
                    ArrayList<String> datos = new ArrayList<>();
                    Button btn = new Button(this);
                    btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT,1.0f));



                    btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_NAME)));
                    datos.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA)));

                    //verifica si en la tabla existe registro de mesero para la mesa
                    if (rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_MESERO))!=null){
                        mesero = rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_MESERO));
                        btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_NAME)) + "\n" + rs.getString(rs.getColumnIndex(DBhelper.KEY_HABI)) + "\n" + rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_MESERO)));

                        if (rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_MESERO)).equalsIgnoreCase(variables.mesero)){
                                btn.setBackgroundResource(R.drawable.shapebtnmesa_mismo);
                            }else {
                               //btn.setBackgroundColor(getResources().getColor(R.color.ambar));
                                btn.setBackgroundResource(R.drawable.shapebtnmesa);
                            }
                    }else{
                        btn.setBackgroundResource(R.drawable.shapebtnfree);

                    }

                    datos.add(mesero);
                    btn.setTag(datos);


                    contenedor.addView(btn);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                      


                            Intent intentcomanda = new Intent(getApplicationContext(), comanda.class);
                            Button b = (Button)v;
                            variables.mesa=((List<String>)v.getTag()).get(0).toString();
                            variables.mesero_mesa = ((List<String>) v.getTag()).get(1).toString();

                            if(variables.mesero_mesa.equalsIgnoreCase("N")) {
                                popup_window();
                            }else {
                                variables.cmd=busca_comanda(variables.mesa);
                                startActivity(intentcomanda);
                            }
                        }
                    });
                    contl = contl + 1;
                    if (contl == 8) {
                        contl = 1;
                        ban = false;
                    }




                }while (rs.moveToNext());

            }



    }
    private void leersesion() {



        String query = "SELECT id FROM " + DBhelper.TABLE_SESION + " WHERE MOVI='" + variables.movi + "' AND FASE='" + variables.fase + "' AND MESERO='"+variables.mesero+"' AND STATUS='A' ";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.getCount() > 0&&rs.getCount()<2) {
            rs.moveToFirst();
            do {
               variables.sesion=rs.getInt(rs.getColumnIndex(DBhelper.KEY_ID));

            }while (rs.moveToNext());

        }else {
            Toast.makeText(getApplicationContext(), "ERROR CON SESION", Toast.LENGTH_LONG).show();
        }



    }
    private void actualiza_mesas(){

        conexion=db.getConexion();
        Statement stmt = null;


        //borrar datos existentes
        dbs.delete(DBhelper.TABLE_PVMESA, null, null);

        String query_mesas = "SELECT PM_MESA,PM_NOMBRE FROM PVMESAS WHERE PM_MOVI='"+variables.movi+"' AND PM_FASE='"+variables.fase+"'";

        String query = "select CE_TRANSA,LPAD(CE_MESA,2,'0') CE_MESA,CE_MESERO, CE_HABI, CE_PAX " +
                "                 FROM PVCHEQDIAENC where CE_MOVI='"+variables.movi+"' and CE_FASE='"+variables.fase+"' and CE_CIERRA_F IS NULL" +
                "                 and CE_CAN_F IS NULL and CE_MESA IS NOT NULL ";

        //////INSERTA PVMESA
        try {
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query_mesas);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.KEY_MOVI, variables.movi);
                cv.put(DBhelper.KEY_FASE, variables.fase);
                cv.put(DBhelper.KEY_MESA, rs.getString("PM_MESA"));
                cv.put(DBhelper.KEY_MESA_NAME, rs.getString("PM_NOMBRE"));
                dbs.insert(DBhelper.TABLE_PVMESA, null, cv);
            }


        } catch (SQLException e) {
            System.out.println( "error pvcat1 " + e);
        }

        //////UPDATE PVMESA
        try {

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.KEY_MESA_MESERO, rs.getString("CE_MESERO"));
                cv.put(DBhelper.KEY_HABI, rs.getString("CE_HABI"));
                cv.put(DBhelper.KEY_PAX, rs.getInt("CE_PAX"));
                dbs.update(DBhelper.TABLE_PVMESA,cv,"MESA='"+rs.getString("CE_MESA")+"'",null);

                actualiza_comanda_mesa(rs.getString("CE_TRANSA"),rs.getString("CE_MESA"),rs.getString("CE_MESERO"));
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println( "error pvcat1 " + e);
        }




    }
    private void actualiza_comanda_mesa(String transa, String mesa,String mesero) {

        conexion = db.getConexion();
        Statement stmt = null;




        String cmd = busca_comanda(mesa);
        if (cmd.equalsIgnoreCase("N")) {
            genera_comanda(mesero,mesa,transa);
            cmd = busca_comanda(mesa);



        String query = "SELECT CD_PRODUCTO,PR_DESC,CD_CANTIDAD,CD_TIEMPO FROM PVCHEQDIADET,PVPRODUCTOS " +
                "WHERE CD_MOVI='" + variables.movi + "' AND CD_FASE='" + variables.fase + "' AND CD_CAN_U IS NULL AND CD_TRANSA='" + transa + "' AND PR_PRODUCTO=CD_PRODUCTO";


        //////INSERTA PRODUCTOS DE MESA
        try {
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.CMD_SESION, variables.sesion);
                cv.put(DBhelper.CMD_MESA, mesa);
                cv.put(DBhelper.CMD_TRANSA, cmd);
                cv.put(DBhelper.CMD_PRID, rs.getString("CD_PRODUCTO"));
                cv.put(DBhelper.CMD_PRDESC, rs.getString("PR_DESC"));
                cv.put(DBhelper.CMD_CANTIDAD, rs.getInt("CD_CANTIDAD"));
                cv.put(DBhelper.CMD_COMENSAL, 1);
                cv.put(DBhelper.CMD_TIEMPO, rs.getInt("CD_TIEMPO"));
                cv.put(DBhelper.CMD_STATUS, "E");
                dbs.insert(DBhelper.TABLE_COMANDA, null, cv);

            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println("error pvcat1 " + e);
        }
    }




    }
    private void genera_comanda(String mesero,String mesa,String transa){




        ContentValues cv = new ContentValues();
        cv.put(DBhelper.CE_SESION, variables.sesion);
        cv.put(DBhelper.CE_TRANSA, transa);
        cv.put(DBhelper.CE_MESA,mesa);
        cv.put(DBhelper.CE_MESERO, mesero);
        cv.put(DBhelper.CE_STATUS, "A");
        dbs.insert(DBhelper.TABLE_COMANDAENC, null, cv);

    }
    private String busca_comanda(String mesa){
        dbs = dbhelper.getWritableDatabase();
        String cmd="N";




        String query = "SELECT CE_TRANSA FROM " + DBhelper.TABLE_COMANDAENC + " WHERE CE_SESION='" + variables.sesion + "' AND CE_MESA='" + mesa + "' AND CE_STATUS='A'";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.moveToFirst()) {
            do {
                cmd = rs.getString(rs.getColumnIndex(DBhelper.CE_TRANSA));
            }while (rs.moveToNext());

        }

        return cmd;

    }
    private void popup_window( ) {

        Button btnClosePopup,btnacep;
        final EditText txthabi,txtbraza,txtrva,txtpax,txtname;
        final PopupWindow pwindo;




        LayoutInflater inflat = (LayoutInflater) tables.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflat.inflate(R.layout.activity_abre_mesa,
                (ViewGroup) findViewById(R.id.activity_abre_mesa));





        pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,600, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, -100);
        txtbraza = (EditText) layout.findViewById(R.id.txtbraza);
        txthabi = (EditText) layout.findViewById(R.id.txthabi);
        txtrva = (EditText) layout.findViewById(R.id.txtrva);
        txtpax = (EditText) layout.findViewById(R.id.txtpax);
        txtname = (EditText) layout.findViewById(R.id.txtname);
        txtbraza.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false&&txtbraza.getText().toString().trim().length()>0){
                    ArrayList<String> h_dato;
                   h_dato= busca_datos_huesped(txtbraza.getText().toString(),"B");
                    if (h_dato.isEmpty() ){
                        Toast.makeText(tables.this,"No se encontro información",Toast.LENGTH_SHORT).show();

                    }else {
                        txthabi.setText(h_dato.get(1).toString());
                        txtrva.setText(h_dato.get(0).toString());
                        txtname.setText(h_dato.get(2).toString());
                    }
                }
            }
        });
        txthabi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false&&txthabi.getText().toString().trim().length()>0){
                    ArrayList<String> h_dato;
                    h_dato= busca_datos_huesped(txthabi.getText().toString(),"H");
                if (h_dato.isEmpty() ){
                    Toast.makeText(tables.this,"No se encontro información",Toast.LENGTH_SHORT).show();

                }else {
                    txtrva.setText(h_dato.get(0).toString());
                    txtname.setText(h_dato.get(2).toString());
                        }
                }
            }
        });

        btnClosePopup = (Button) layout.findViewById(R.id.btnclosepwmesa);
        btnacep = (Button) layout.findViewById(R.id.btnabrir);
        btnacep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transa = "";
                txtpax.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                if (txtname.getText().toString().trim().length()>0) {

                    if(txtpax.getText().toString().trim().length()>0){
                        ////datos Comensal
                        variables.comensal_name=txtname.getText().toString();
                        variables.rva=txtrva.getText().toString();
                        variables.habi=txthabi.getText().toString();
                        variables.mesa_pax=Integer.parseInt(txtpax.getText().toString());
                        ////Abre comanda
                        try {

                                transa = db.genera_transa();
                                genera_comanda(variables.mesero, variables.mesa, transa);
                                variables.cmd = busca_comanda(variables.mesa);
                                db.inserta_comanda_enc();
                            } catch (SQLException e) {
                                    e.printStackTrace();
                            }
                ////Abre platillos
                    Intent intent = new Intent(getApplicationContext(), platillos.class);
                    startActivity(intent);



                    }else {
                        Toast.makeText(getApplicationContext(),"PAX vacio",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Nombre vacio",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwindo.dismiss();

            }
        });





    }
    private ArrayList<String> busca_datos_huesped(String dato,String tipo){

        dbs = dbhelper.getWritableDatabase();
        ArrayList<String> h_dato = new ArrayList<>();
        String consulta="N";

        switch (tipo){
            case "B": consulta="SELECT PN_RESERVA,PN_HABI,PN_NOMBRE FROM " + DBhelper.TABLE_BRAZA + ","+DBhelper.TABLE_PVRVANOMBRE+" WHERE BU_FOLIO='" + dato + "' AND PN_RESERVA=BU_RESERVA AND PN_SEC=1";
                break;
            case "H": consulta="SELECT PN_RESERVA,PN_HABI,PN_NOMBRE FROM "+DBhelper.TABLE_PVRVANOMBRE+" WHERE PN_HABI='"+dato+"' AND PN_SEC=1";
                break;
            default: consulta="";
        }



        if( consulta.equalsIgnoreCase("N")){
            Toast.makeText(getApplicationContext(),"Error en busqueda de informacion",Toast.LENGTH_SHORT);

        }else{
            String query = consulta;

            Cursor rs = dbs.rawQuery(query, null);
            if (rs.moveToFirst()) {
                do {
                    h_dato.add(rs.getString(rs.getColumnIndex(DBhelper.PN_RESERVA)));
                    h_dato.add(rs.getString(rs.getColumnIndex(DBhelper.PN_HABI)));
                    h_dato.add(rs.getString(rs.getColumnIndex(DBhelper.PN_NOMBRE)));
                } while (rs.moveToNext());

            }
        }

        return h_dato;

    }

    public class datosmesas extends AsyncTask<String,String,String> {

        final ProgressDialog progressDialog = new ProgressDialog(tables.this,R.style.AppTheme_Dark_Dialog);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Trabajando...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String resp) {

            super.onPostExecute(resp);
            progressDialog.dismiss();
            leermesas();

        }

        @Override
        protected String doInBackground(String... params) {
            String resp="ok";
            leersesion();
            actualiza_mesas();



            return resp;
        }
    }




}
