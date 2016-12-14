package oasispv.pv;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class tables extends AppCompatActivity {

    private DBhelper dbhelper;
    ConnectOra db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_tables);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar backbtn = getSupportActionBar();
        backbtn.setDisplayHomeAsUpEnabled(true);
        actualiza_mesas();
        leermesas();
        leersesion();




    }
    private void leermesas(){
        String mesero = "N";
        dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getWritableDatabase();

            LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.btnlmaster);
            btnsContainer.removeAllViews();
            //Crea botons dinamicamente.
            int contl = 1;
            Boolean ban = false;
            LinearLayout contenedor = null;


            String query = "SELECT MESA,MESA_NAME,HABI,MESA_MESERO FROM " + DBhelper.TABLE_PVMESA + " WHERE MOVI='" + variables.movi + "' AND FASE='" + variables.fase + "'";

            Cursor rs = dbs.rawQuery(query, null);
            if (rs.getCount() > 0) {
                rs.moveToFirst();
                do {
                    if (!ban) {
                        ban = true;
                        contenedor = new LinearLayout(this);
                        contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                        contenedor.setOrientation((LinearLayout.HORIZONTAL));
                        btnsContainer.addView(contenedor);
                    }
                    ArrayList<String> datos = new ArrayList<>();
                    Button btn = new Button(this);
                    btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));



                    btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_NAME)));
                    datos.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA)));

                    //verifica si en la tabla existe registro de mesero para la mesa
                    if (rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_MESERO))!=null){
                        mesero=rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_MESERO));
                        btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_NAME)) + "\n" + rs.getString(rs.getColumnIndex(DBhelper.KEY_HABI)) + "\n" + rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_MESERO)));
                        btn.setBackgroundColor(getResources().getColor(R.color.ambar));
                    }

                    datos.add(mesero);
                    btn.setTag(datos);


                    contenedor.addView(btn);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent intent = new Intent(getApplicationContext(), platillos.class);
                            Intent intentcomanda = new Intent(getApplicationContext(), comanda.class);
                            Button b = (Button)v;


                            variables.mesa=((List<String>)v.getTag()).get(0).toString();

                            variables.mesero_mesa = ((List<String>) v.getTag()).get(1).toString();




                            variables.cmd = busca_comanda();
                            if ( variables.cmd==-1) {
                                genera_comanda();
                                variables.cmd = busca_comanda();

                            }
                            if(variables.mesero_mesa.equalsIgnoreCase("N")) {

                                startActivity(intent);
                            }else {
                                startActivity(intentcomanda);
                            }

                        }
                    });
                    contl = contl + 1;
                    if (contl == 6) {
                        contl = 1;
                        ban = false;
                    }




                }while (rs.moveToNext());

            }
        dbs.close();


    }
    private void leersesion() {
        dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getWritableDatabase();



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
        dbs.close();


    }
    private void actualiza_mesas(){

        Connection conexion=db.getConexion();
        Statement stmt = null;
        dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getWritableDatabase();
        //borrar datos existentes
        dbs.delete(DBhelper.TABLE_PVMESA, null, null);

        String query_mesas = "SELECT PM_MESA,PM_NOMBRE FROM PVMESAS WHERE PM_MOVI='"+variables.movi+"' AND PM_FASE='"+variables.fase+"' AND PM_AREA IS NOT NULL";

        String query = "select CE_MESA,PM_NOMBRE,CE_MESERO, CE_HABI, CE_PAX " +
                "                 FROM PVCHEQDIAENC,PVMESAS where CE_MOVI='"+variables.movi+"' and CE_FASE='"+variables.fase+"' and CE_CIERRA_F IS NULL" +
                "                 and CE_CAN_F IS NULL and CE_MESA IS NOT NULL AND PM_MOVI=CE_MOVI AND PM_FASE=CE_FASE AND PM_MESA=CE_MESA";

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
                cv.put(DBhelper.KEY_GUEST, "Conrado Gonzalez");
                cv.put(DBhelper.KEY_PAX, rs.getInt("CE_PAX"));
                dbs.update(DBhelper.TABLE_PVMESA,cv,"MESA='"+rs.getString("CE_MESA")+"'",null);
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println( "error pvcat1 " + e);
        }




    }
    private void genera_comanda(){

        dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBhelper.CE_SESION, variables.sesion);
        cv.put(DBhelper.CE_MESA,variables.mesa);
        cv.put(DBhelper.CE_MESERO, variables.mesero);
        cv.put(DBhelper.CE_STATUS, "A");
        dbs.insert(DBhelper.TABLE_COMANDAENC, null, cv);

    }
    private int busca_comanda(){

        int cmd=-1;

        dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getWritableDatabase();

        String query = "SELECT ID FROM " + DBhelper.TABLE_COMANDAENC + " WHERE CE_SESION='" + variables.sesion + "' AND CE_MESA='" + variables.mesa + "' AND CE_STATUS='A'";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.moveToFirst()) {
            do {
                cmd = rs.getInt(rs.getColumnIndex(DBhelper.KEY_ID));
            }while (rs.moveToNext());

        }

        return cmd;

    }




}
