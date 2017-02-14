package oasispv.pv;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private DBhelper dbhelper;
    private SQLiteDatabase dbs;
    Button btnini;
    TextView txtuser;
    TextView txtpwd;
    ConnectOra db;
    Connection conexion;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbhelper = DBhelper.getInstance(getApplicationContext());
        dbs = dbhelper.getWritableDatabase();

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        btnini= (Button) findViewById(R.id.btnini);
        txtuser= (TextView) findViewById(R.id.usrtxt);
        txtpwd= (TextView) findViewById(R.id.pwdtxt);
        final TextView txtturno= (TextView) findViewById(R.id.txtturno);


        btnini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr=txtuser.getText().toString().trim();
                String pwd=txtpwd.getText().toString().trim();
                int loging = 0;
               // db = new ConnectOra(variables.ip, variables.cn, "CIELO", variables.pw);
                try {
                    loging = db.getlogin(usr,pwd);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (loging==1) {
                    //db = new ConnectOra(variables.ip, variables.cn, variables.un, variables.pw);
                    variables.mesero = txtuser.getText().toString().trim();
                    variables.turno = Integer.parseInt(txtturno.getText().toString());

                    //dbhelper = new DBhelper(getApplicationContext());
                    //SLiteDatabase dbs = dbhelper.getWritableDatabase();
                    dbs.delete(DBhelper.TABLE_SESION, null, null);
                    ContentValues cv = new ContentValues();
                    cv.put(DBhelper.SES_MESERO, variables.mesero);
                    cv.put(DBhelper.SES_MOVI, variables.movi);
                    cv.put(DBhelper.SES_FASE, variables.fase);
                    cv.put(DBhelper.SES_STATUS, "A");
                    dbs.insert(DBhelper.TABLE_SESION, null, cv);

                    Intent intent = new Intent(getApplicationContext(), tables.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Hay problemas con el usuario, favor verificar Usuario y Contraseña",Toast.LENGTH_LONG).show();
                }

            }
        });

        //// Datos Turno

        txtturno.setText("1");
        Button btnmenost = (Button) findViewById(R.id.btnmenostu);
        btnmenost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int turno = Integer.parseInt(txtturno.getText().toString());
                if(turno==1) {
                    txtturno.setText("1");
                }else{
                    turno=turno-1;
                    txtturno.setText(Integer.toString(turno));
                }

            }
        });

        Button  btnmast= (Button) findViewById(R.id.btnmastu);
        btnmast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int turno = Integer.parseInt(txtturno.getText().toString());
                if(turno==3) {
                    txtturno.setText("3");
                }else{
                    turno=turno+1;
                    txtturno.setText(Integer.toString(turno));
                }

            }
        });
        new datostablet().execute();





    }

    @Override
    protected void onDestroy(){
        super.onStop();
        dbs.close();
        try {
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class datostablet extends AsyncTask<String,String,String>{
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,R.style.AppTheme_Dark_Dialog);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Verificando datos...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String resp) {

            super.onPostExecute(resp);
            progressDialog.dismiss();
            btnini.setText(variables.movi_desc);
            Toast.makeText(getApplicationContext(),resp,Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            String resp;

            verificatablet();
             db = new ConnectOra(variables.ip,variables.cn,variables.un,variables.pw);
            resp=llenatablas();
            verifica_mod_guar();

            return resp;
        }
    }



    public String llenatablas() {

        String res = "";
        Statement stmt = null;
        String movi = variables.movi;
        String fase = variables.fase;
        conexion = db.getConexion();


        //borrar datos existentes
        dbs.delete(DBhelper.TABLE_PVCAT1, null, null);
        dbs.delete(DBhelper.TABLE_PVCAT2, null, null);
        dbs.delete(DBhelper.TABLE_PVMENUS, null, null);
        dbs.delete(DBhelper.TABLE_PVMODOS, null, null);
        dbs.delete(DBhelper.TABLE_COMANDAENC, null, null);
        dbs.delete(DBhelper.TABLE_COMANDA, null, null);
        dbs.delete(DBhelper.TABLE_PVPRODUCTOSMODI, null, null);
        dbs.delete(DBhelper.TABLE_PVPRODUCTOSMODOSG, null, null);
        dbs.delete(DBhelper.TABLE_PRMOD, null, null);
        dbs.delete(DBhelper.TABLE_PVMODOSG, null, null);
        dbs.delete(DBhelper.TABLE_PVRVANOMBRE, null, null);
        dbs.delete(DBhelper.TABLE_BRAZA, null, null);

        String querycat1 = "select CAT1_MOVI, CAT1_FASE, CAT1_CARTA, CAT1, CAT1_IMAGEN, CAT1_DESC, CAT1_POS" +
                " FROM PVCAT1 where CAT1_MOVI='" + movi + "' and CAT1_FASE='" + fase + "' ";


        String querycat2 = "SELECT CAT2_MOVI, CAT2_FASE, CAT2_CARTA, CAT2_CAT1, CAT2, CAT2_IMAGEN, CAT2_DESC, CAT2_POS " +
                "FROM PVCAT2,PVCAT1 WHERE CAT2_MOVI='" + movi + "' AND CAT2_FASE='" + fase + "' AND CAT1_MOVI=CAT2_MOVI AND CAT1_FASE=CAT2_FASE AND CAT2_CAT1=CAT1";

        String queryPVMODI = "SELECT MP_PRODUCTO, MP_MODI, MP_DEFAULT " +
                "FROM PVPRODUCTOSMODI";

        String queryPVMODOS = "SELECT MD_GRUPO, MD_MODO, MD_DESC, MD_DEFAULT " +
                "FROM PVMODOS";

        String queryPVMODOSG = "SELECT MG_GRUPO, MG_DESC, MG_MANDAT " +
                "FROM PVMODOSG";

        String queryPRMOD = "SELECT PR_PRODUCTO,PR_DESC " +
                "FROM PVPRODUCTOS WHERE PR_MODI='S'";

        String queryPVMODOSGPR = " SELECT GP_PRODUCTO, GP_GRUPO,MG_DESC " +
                "FROM PVPRODUCTOSMODOSG,PVMODOSG WHERE MG_GRUPO=GP_GRUPO";

        String queryRVANMES = "SELECT RV_RESERVA,RV_HABI,NOMBRE,VN_SECUENCIA " +
                "FROM PVRVANOMBRE WHERE VN_SECUENCIA=1";

        String queryBRAZA = " SELECT BU_RESERVA,BU_FOLIO " +
                "FROM PVFRONT.FRBRAZALUSO";


        String querypvmenus = " SELECT PM_MOVI, PM_FASE, PM_CAT1, PM_CAT2, PM_PRODUCTO,PR_DESC, PM_POS," +
                " CASE WHEN (NVL(PM_PRECIO,0)<>0) THEN PM_PRECIO ELSE NVL(PR_PRECIO,0) END PM_PRECIO," +
                " PM_CARTA, PM_COMISION, PM_PROPINA, PM_FAMILIA, PM_GRUPOPR, PM_SUBFAMILIAPR" +
                " FROM PVMENUS,PVPRODUCTOS WHERE PM_MOVI='" + movi + "' AND PM_FASE='" + fase + "' AND PR_PRODUCTO=PM_PRODUCTO AND PR_ACTIVO='S'";


        //////INSERTA PVPRODUCTOSMODOSG
        try {
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(queryPVMODOSGPR);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.GP_PRODUCTO, rs.getString("GP_PRODUCTO"));
                cv.put(DBhelper.GP_GRUPO, rs.getString("GP_GRUPO"));
                cv.put(DBhelper.GP_GRUPO_DESC, rs.getString("MG_DESC"));
                dbs.insert(DBhelper.TABLE_PVPRODUCTOSMODOSG, null, cv);
            }

        } catch (SQLException e) {
            res = "error PVPRODUCTOSMODOSG " + e;
        }
        //////INSERTA PVRCANOMBRE
        try {

            ResultSet rs = stmt.executeQuery(queryRVANMES);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.PN_RESERVA, rs.getString("RV_RESERVA"));
                cv.put(DBhelper.PN_HABI, rs.getString("RV_HABI"));
                cv.put(DBhelper.PN_NOMBRE, rs.getString("NOMBRE"));
                cv.put(DBhelper.PN_SEC, rs.getInt("VN_SECUENCIA"));
                dbs.insert(DBhelper.TABLE_PVRVANOMBRE, null, cv);
            }

        } catch (SQLException e) {
            res = "error PVRCANOMBRE " + e;
        }
        //////INSERTA BRAZALETE
        try {

            ResultSet rs = stmt.executeQuery(queryBRAZA);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.BU_RESERVA, rs.getString("BU_RESERVA"));
                cv.put(DBhelper.BU_FOLIO, rs.getString("BU_FOLIO"));

                dbs.insert(DBhelper.TABLE_BRAZA, null, cv);
            }

        } catch (SQLException e) {
            res = "error BRAZALETE " + e;
        }
        //////INSERTA PVMODOSG
        try {

            ResultSet rs = stmt.executeQuery(queryPVMODOSG);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.MG_GRUPO, rs.getString("MG_GRUPO"));
                cv.put(DBhelper.MG_DESC, rs.getString("MG_DESC"));
                cv.put(DBhelper.MG_MANDAT, rs.getString("MG_MANDAT"));
                dbs.insert(DBhelper.TABLE_PVMODOSG, null, cv);
            }

        } catch (SQLException e) {
            res = "error PVMODOSG " + e;
        }
        //////INSERTA PRMOD
        try {

            ResultSet rs = stmt.executeQuery(queryPRMOD);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.PD_PRODUCTO, rs.getString("PR_PRODUCTO"));
                cv.put(DBhelper.PD_DESC, rs.getString("PR_DESC"));
                dbs.insert(DBhelper.TABLE_PRMOD, null, cv);
            }

        } catch (SQLException e) {
            res = "error PRMOD " + e;
        }
        //////INSERTA PVMODOS
        try {

            ResultSet rs = stmt.executeQuery(queryPVMODOS);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.MD_GRUPO, rs.getString("MD_GRUPO"));
                cv.put(DBhelper.MD_MODO, rs.getString("MD_MODO"));
                cv.put(DBhelper.MD_DESC, rs.getString("MD_DESC"));
                cv.put(DBhelper.MD_DEFAULT, rs.getString("MD_DEFAULT"));
                dbs.insert(DBhelper.TABLE_PVMODOS, null, cv);
            }

        } catch (SQLException e) {
            res = "error PVMODOS " + e;
        }
        //////INSERTA PVPRODUCTOSMODI
        try {

            ResultSet rs = stmt.executeQuery(queryPVMODI);
            while (rs.next()) {
                ContentValues cv = new ContentValues();
                cv.put(DBhelper.MP_PRODUCTO, rs.getString("MP_PRODUCTO"));
                cv.put(DBhelper.MP_MODI, rs.getString("MP_MODI"));
                cv.put(DBhelper.MP_DEFAULT, rs.getString("MP_DEFAULT"));
                dbs.insert(DBhelper.TABLE_PVPRODUCTOSMODI, null, cv);
            }

        } catch (SQLException e) {
            res = "error PVPRODUCTOSMODI " + e;
        }
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
            res = "error PVCAT2 " + e;
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
                cv.put(DBhelper.KEY_PM_PRECIO, rs.getFloat("PM_PRECIO"));
                cv.put(DBhelper.KEY_PM_CARTA, rs.getString("PM_CARTA"));
                cv.put(DBhelper.KEY_PM_COMISION, rs.getInt("PM_COMISION"));
                cv.put(DBhelper.KEY_PM_PROPINA, rs.getInt("PM_PROPINA"));
                cv.put(DBhelper.KEY_PM_FAMILIA, rs.getString("PM_FAMILIA"));
                cv.put(DBhelper.KEY_PM_GRUPOPR, rs.getString("PM_GRUPOPR"));
                cv.put(DBhelper.KEY_PM_SUBFAMILIAPR, rs.getString("PM_SUBFAMILIAPR"));
                cv.put(DBhelper.KEY_PM_MODI,"N");
                cv.put(DBhelper.KEY_PM_GUAR,"N");
                dbs.insert(DBhelper.TABLE_PVMENUS, null, cv);
            }
            stmt.close();

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
                variables.modipv=c.getString(c.getColumnIndex(DBhelper.KEY_MODI));
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
                    conexion = db.getConexion();



                    try {
                        stmt = conexion.createStatement();
                        String selectpv = "select PT_NOMBRE, PT_PV, PT_FASE, PT_UN, PT_CN, PT_PW, PT_IP_PV,PT_MODI" +
                                " FROM PVXTABLET where PT_MAC='" + mac + "'";
                       /* String selectpv = "select PT_NOMBRE, PT_PV, PT_FASE, PT_UN, PT_CN, PT_PW, PT_IP_PV,PT_MODI" +
                                " FROM PVXTABLET where PT_MAC='EMULADOR'";*/

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
                            cv.put(DBhelper.KEY_MODI, rs.getString("PT_MODI"));
                            cv.put(DBhelper.KEY_NAME_C, rs.getString("PT_NOMBRE"));
                            dbs.insert(DBhelper.TABLE_CONNECT, null, cv);

                            // Graba info de Pv de la Tablet en variables
                            variables.movi=rs.getString("PT_PV");
                            variables.fase=rs.getString("PT_FASE");
                            variables.ip=rs.getString("PT_IP_PV");
                            variables.cn=rs.getString("PT_CN");
                            variables.un=rs.getString("PT_UN");
                            variables.pw=rs.getString("PT_PW");
                            variables.modipv=rs.getString("PT_MODI");
                            variables.movi_desc=rs.getString("PT_NOMBRE");
                        }
                        stmt.close();



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

                    verificatablet();
                }

            }





            if (res == "") {
                res = "Actualizacion Realizada";
            }
            return res;


    }
    private void verifica_mod_guar() {

        String query = "SELECT PM_PRODUCTO FROM " + DBhelper.TABLE_PVMENUS + " WHERE PM_MOVI='" + variables.movi + "' AND PM_FASE='" + variables.fase + "'";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.moveToFirst()) {
            do {
                String pr =rs.getString(rs.getColumnIndex(DBhelper.KEY_PM_PRODUCTO));

                long countmd=DatabaseUtils.queryNumEntries(dbs,DBhelper.TABLE_PVPRODUCTOSMODOSG,""+DBhelper.GP_PRODUCTO+"="+"'"+pr+"'");
                long countgu=DatabaseUtils.queryNumEntries(dbs,DBhelper.TABLE_PVPRODUCTOSMODI,""+DBhelper.MP_PRODUCTO+"="+"'"+pr+"'");

                if (countmd>0){
                    ContentValues cv = new ContentValues();
                    cv.put(DBhelper.KEY_PM_MODI,"S");
                    dbs.update(DBhelper.TABLE_PVMENUS,cv,""+DBhelper.KEY_PM_PRODUCTO+"="+"'"+pr+"'",null);
                }
                if (countgu>0){
                    ContentValues cv = new ContentValues();
                    cv.put(DBhelper.KEY_PM_GUAR,"S");
                    dbs.update(DBhelper.TABLE_PVMENUS,cv,""+DBhelper.KEY_PM_PRODUCTO+"="+"'"+pr+"'",null);
                }

            }while (rs.moveToNext());

        }
    }


}
