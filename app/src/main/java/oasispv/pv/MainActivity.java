package oasispv.pv;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class MainActivity extends AppCompatActivity{
    private DBhelper dbhelper;
    Button btnini;
    TextView txtuser;
    ConnectOra db;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        btnini= (Button) findViewById(R.id.btnini);
        txtuser= (TextView) findViewById(R.id.usrtxt);

        btnini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 variables.mesero=txtuser.getText().toString();
                dbhelper = new DBhelper(getApplicationContext());
                SQLiteDatabase dbs = dbhelper.getWritableDatabase();
                dbs.delete(DBhelper.TABLE_SESION, null, null);
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.SES_MESERO,variables.mesero);
                cv.put(DBhelper.SES_MOVI,variables.movi);
                cv.put(DBhelper.SES_FASE, variables.fase);
                cv.put(DBhelper.SES_STATUS, "A");
                dbs.insert(DBhelper.TABLE_SESION, null, cv);

                Intent intent = new Intent(getApplicationContext(), tables.class);
                startActivity(intent);

            }
        });
        new datostablet().execute(); // Proceso verifica restaurante y conexion




       /* listrest = (ListView) findViewById(R.id.listrest);
        btnrest = (Button) findViewById(R.id.btnpvd);

        listrestadp adapter = new listrestadp(this,vrest,imagenes);
        listrest.setAdapter(adapter);
        listrest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), tables.class);
                intent.putExtra("rest",vrest[position]);
                startActivity(intent);
            }
        });*/



    }
    public class datostablet extends AsyncTask<String,String,String>{
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,R.style.AppTheme_Dark_Dialog);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Verificando datos...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String resp) {

            super.onPostExecute(resp);
            progressDialog.dismiss();
            btnini.setText(variables.movi_desc);
            Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... params) {
            String resp;

            verificatablet();
             db = new ConnectOra(variables.ip,variables.cn,variables.un,variables.pw);
            resp=llenatablas();

            return resp;
        }
    }



    public String llenatablas() {
        String res = "";
        Connection conexion = db.getConexion();
        Statement stmt = null;
        String movi = variables.movi;
        String fase = variables.fase;
        dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getWritableDatabase();
        //borrar datos existentes
        dbs.delete(DBhelper.TABLE_PVCAT1, null, null);
        dbs.delete(DBhelper.TABLE_PVCAT2, null, null);
        dbs.delete(DBhelper.TABLE_PVMENUS, null, null);

        String querycat1 = "select CAT1_MOVI, CAT1_FASE, CAT1_CARTA, CAT1, CAT1_IMAGEN, CAT1_DESC, CAT1_POS" +
                " FROM PVCAT1 where CAT1_MOVI='" + movi + "' and CAT1_FASE='" + fase + "' ";


        String querycat2 = "SELECT CAT2_MOVI, CAT2_FASE, CAT2_CARTA, CAT2_CAT1, CAT2, CAT2_IMAGEN, CAT2_DESC, CAT2_POS " +
                "FROM PVCAT2,PVCAT1 WHERE CAT2_MOVI='" + movi + "' AND CAT2_FASE='" + fase + "' AND CAT1_MOVI=CAT2_MOVI AND CAT1_FASE=CAT2_FASE AND CAT2_CAT1=CAT1";

        String querypvmenus = " SELECT PM_MOVI, PM_FASE, PM_CAT1, PM_CAT2, PM_PRODUCTO,PR_DESC, PM_POS, PM_PRECIO, PM_CARTA, PM_COMISION, PM_PROPINA, PM_FAMILIA, PM_GRUPOPR, PM_SUBFAMILIAPR" +
                " FROM PVMENUS,PVPRODUCTOS WHERE PM_MOVI='" + movi + "' AND PM_FASE='" + fase + "' AND PR_PRODUCTO=PM_PRODUCTO";


        //////INSERTA PVCAT1
        try {
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(querycat1);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.KEY_CAT1_MOVI, rs.getString("CAT1_MOVI"));
                cv.put(DBhelper.KEY_CAT1_FASE, rs.getString("CAT1_FASE"));
                cv.put(DBhelper.KEY_CAT1_CARTA, rs.getString("CAT1_CARTA"));
                cv.put(DBhelper.KEY_CAT1, rs.getString("CAT1"));
                cv.put(DBhelper.KEY_CAT1_IMAGEN, rs.getString("CAT1_IMAGEN"));
                cv.put(DBhelper.KEY_CAT1_DESC, rs.getString("CAT1_DESC"));
                cv.put(DBhelper.KEY_CAT1_POS, rs.getInt("CAT1_POS"));
                dbs.insert(DBhelper.TABLE_PVCAT1, null, cv);
            }

        } catch (SQLException e) {
            res = "error pvcat1 " + e;
        }

        ///////INSERTA PVCAT2

        try {
            //stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(querycat2);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.KEY_CAT2_MOVI, rs.getString("CAT2_MOVI"));
                cv.put(DBhelper.KEY_CAT2_FASE, rs.getString("CAT2_FASE"));
                cv.put(DBhelper.KEY_CAT2_CARTA, rs.getString("CAT2_CARTA"));
                cv.put(DBhelper.KEY_CAT2_CAT1, rs.getString("CAT2_CAT1"));
                cv.put(DBhelper.KEY_CAT2, rs.getString("CAT2"));
                cv.put(DBhelper.KEY_CAT2_IMAGEN, rs.getString("CAT2_IMAGEN"));
                cv.put(DBhelper.KEY_CAT2_DESC, rs.getString("CAT2_DESC"));
                cv.put(DBhelper.KEY_CAT2_POS, rs.getInt("CAT2_POS"));
                dbs.insert(DBhelper.TABLE_PVCAT2, null, cv);
            }

        } catch (SQLException e) {
            res = "error pvcat1 " + e;
        }
        try {

            ResultSet rs = stmt.executeQuery(querypvmenus);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.KEY_PM_MOVI, rs.getString("PM_MOVI"));
                cv.put(DBhelper.KEY_PM_FASE, rs.getString("PM_FASE"));
                cv.put(DBhelper.KEY_PM_CAT1, rs.getString("PM_CAT1"));
                cv.put(DBhelper.KEY_PM_CAT2, rs.getString("PM_CAT2"));
                cv.put(DBhelper.KEY_PM_PRODUCTO, rs.getString("PM_PRODUCTO"));
                cv.put(DBhelper.KEY_PM_PRODUCTO_DESC, rs.getString("PR_DESC"));
                cv.put(DBhelper.KEY_PM_POS, rs.getInt("PM_POS"));
                cv.put(DBhelper.KEY_PM_PRECIO, rs.getInt("PM_PRECIO"));
                cv.put(DBhelper.KEY_PM_CARTA, rs.getString("PM_CARTA"));
                cv.put(DBhelper.KEY_PM_COMISION, rs.getInt("PM_COMISION"));
                cv.put(DBhelper.KEY_PM_PROPINA, rs.getInt("PM_PROPINA"));
                cv.put(DBhelper.KEY_PM_FAMILIA, rs.getString("PM_FAMILIA"));
                cv.put(DBhelper.KEY_PM_GRUPOPR, rs.getString("PM_GRUPOPR"));
                cv.put(DBhelper.KEY_PM_SUBFAMILIAPR, rs.getString("PM_SUBFAMILIAPR"));
                dbs.insert(DBhelper.TABLE_PVMENUS, null, cv);
            }
            stmt.close();
            dbs.close();
        } catch (SQLException e) {
            res = "error pvmenu " + e;
        }
        if (res == "") {
            res = "Actualizacion Realizada";
        }
        return res;
    }
    public String verificatablet() {

        String mac = Utils.getMACAddress("wlan0");
        String res = "";
        dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getReadableDatabase();

        String selectlocal = "select * from " + DBhelper.TABLE_CONNECT + " where " + DBhelper.KEY_TIPO + "='local'";
        String selectremoto = "select * from " + DBhelper.TABLE_CONNECT + " where " + DBhelper.KEY_TIPO + "='remote'";
        Cursor c = dbs.rawQuery(selectlocal, null);

            if (c.getCount() > 0) {
                c.moveToFirst();

                variables.movi=c.getString(c.getColumnIndex(DBhelper.KEY_MOVI_C));
                variables.fase=c.getString(c.getColumnIndex(DBhelper.KEY_FASE_C));
                variables.ip=c.getString(c.getColumnIndex(DBhelper.KEY_IP));
                variables.cn=c.getString(c.getColumnIndex(DBhelper.KEY_CN));
                variables.un=c.getString(c.getColumnIndex(DBhelper.KEY_UN));
                variables.pw=c.getString(c.getColumnIndex(DBhelper.KEY_PW));
                variables.movi_desc=c.getString(c.getColumnIndex(DBhelper.KEY_NAME_C));

            } else {// Busca valores de PV de Tablet en Cronos
                c = dbs.rawQuery(selectremoto, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    String name, movi, fase, ip, un, cn, pw;
                    Statement stmt = null;

                    ip = c.getString(c.getColumnIndex(DBhelper.KEY_IP));
                    cn = c.getString(c.getColumnIndex(DBhelper.KEY_CN));
                    un = c.getString(c.getColumnIndex(DBhelper.KEY_UN));
                    pw = c.getString(c.getColumnIndex(DBhelper.KEY_PW));

                    db = new ConnectOra(ip, cn, un, pw);
                    Connection conexion = db.getConexion();

                    try {
                        stmt = conexion.createStatement();
                        String selectpv = "select PT_NOMBRE, PT_PV, PT_FASE, PT_UN, PT_CN, PT_PW, PT_IP_PV" +
                                " FROM PVXTABLET where PT_MAC='" + mac + "'";

                        ResultSet rs = stmt.executeQuery(selectpv);
                        while (rs.next()) {
                            /// inserta info de PV de la Tablet
                            ContentValues cv = new ContentValues();
                            cv.put(DBhelper.KEY_TIPO, "local");
                            cv.put(DBhelper.KEY_MOVI_C, rs.getString("PT_PV"));
                            cv.put(DBhelper.KEY_FASE_C, rs.getString("PT_FASE"));
                            cv.put(DBhelper.KEY_UN, rs.getString("PT_UN"));
                            cv.put(DBhelper.KEY_CN, rs.getString("PT_CN"));
                            cv.put(DBhelper.KEY_PW, rs.getString("PT_PW"));
                            cv.put(DBhelper.KEY_IP, rs.getString("PT_IP_PV"));
                            cv.put(DBhelper.KEY_NAME_C, rs.getString("PT_NOMBRE"));
                            dbs.insert(DBhelper.TABLE_CONNECT, null, cv);

                            // Graba info de Pv de la Tablet en variables
                            variables.movi=rs.getString("PT_PV");
                            variables.fase=rs.getString("PT_FASE");
                            variables.ip=rs.getString("PT_IP_PV");
                            variables.cn=rs.getString("PT_CN");
                            variables.un=rs.getString("PT_UN");
                            variables.pw=rs.getString("PT_PW");
                            variables.movi_desc=rs.getString("PT_NOMBRE");
                        }
                        stmt.close();
                        dbs.close();
                        conexion.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    res = "";
                } else {// no encontro valores de cronos ni de PV-- inserta valores Cronos y verifica de nuevo
                    ContentValues cv = new ContentValues();

                    cv.put(DBhelper.KEY_TIPO, "remote");
                    cv.put(DBhelper.KEY_MOVI_C, "CRONOS");
                    cv.put(DBhelper.KEY_FASE_C, "0");
                    cv.put(DBhelper.KEY_UN, "FRGRAND");
                    cv.put(DBhelper.KEY_CN, "CRONOS");
                    cv.put(DBhelper.KEY_PW, "SERVICE");
                    cv.put(DBhelper.KEY_IP, "192.168.1.8");
                    cv.put(DBhelper.KEY_NAME_C, "CONEXION CRONOS");
                    dbs.insert(DBhelper.TABLE_CONNECT, null, cv);
                    dbs.close();
                    verificatablet();
                }

            }


            dbs.close();


            if (res == "") {
                res = "Actualizacion Realizada";
            }
            return res;


    }

}
