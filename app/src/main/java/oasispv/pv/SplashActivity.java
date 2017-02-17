package oasispv.pv;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SplashActivity extends AppCompatActivity {

    private DBhelper dbhelper;
    private SQLiteDatabase dbs;
    ConnectOra db = ConnectOra.getInstance();
    Connection conexion;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dbhelper = DBhelper.getInstance(getApplicationContext());
        dbs = dbhelper.getWritableDatabase();
        progressBar = (ProgressBar) findViewById(R.id.pb_oasis);

        new datostablet().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
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

        } catch (Exception e) {
            return "PVPRODUCTOSMODOSG  " + e.getMessage();
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

        } catch (Exception e) {
            return "PVRCANOMBRE  " + e.getMessage();
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

        } catch (Exception e) {
            return "BRAZALETE  " + e.getMessage();
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

        } catch (Exception e) {
            return "PVMODOSG  " + e.getMessage();
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

        } catch (Exception e) {
            return "PRMOD  " + e.getMessage();
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

        } catch (Exception e) {
            return "PVMODOS  " + e.getMessage();
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

        } catch (Exception e) {
            return "PVPRODUCTOSMODI  " + e.getMessage();
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

        } catch (Exception e) {
            return "PVCAT1  " + e.getMessage();
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

        } catch (Exception e) {
            return "PVCAT2  " + e.getMessage();
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
                cv.put(DBhelper.KEY_PM_MODI, "N");
                cv.put(DBhelper.KEY_PM_GUAR, "N");
                dbs.insert(DBhelper.TABLE_PVMENUS, null, cv);
            }
            stmt.close();

        } catch (Exception e) {
            return "PVMENUS  " + e.getMessage();
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
            variables.movi = c.getString(c.getColumnIndex(DBhelper.KEY_MOVI_C));
            variables.fase = c.getString(c.getColumnIndex(DBhelper.KEY_FASE_C));
            variables.ip = c.getString(c.getColumnIndex(DBhelper.KEY_IP));
            variables.cn = c.getString(c.getColumnIndex(DBhelper.KEY_CN));
            variables.un = c.getString(c.getColumnIndex(DBhelper.KEY_UN));
            variables.pw = c.getString(c.getColumnIndex(DBhelper.KEY_PW));
            variables.modipv = c.getString(c.getColumnIndex(DBhelper.KEY_MODI));
            variables.movi_desc = c.getString(c.getColumnIndex(DBhelper.KEY_NAME_C));

        } else {
            c = dbs.rawQuery(selectremoto, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                String ip, un, cn, pw;
                Statement stmt = null;

                variables.ip = c.getString(c.getColumnIndex(DBhelper.KEY_IP));
                variables.cn = c.getString(c.getColumnIndex(DBhelper.KEY_CN));
                variables.un = c.getString(c.getColumnIndex(DBhelper.KEY_UN));
                variables.pw = c.getString(c.getColumnIndex(DBhelper.KEY_PW));
                Log.e("oracle", variables.ip + " " + variables.cn + " " + variables.un + " " + variables.pw);
                conexion = ConnectOra.getInstance().getConexion();

                try {
                    stmt = conexion.createStatement();
                    String selectpv = "select PT_NOMBRE, PT_PV, PT_FASE, PT_UN, PT_CN, PT_PW, PT_IP_PV,PT_MODI" +
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
                        cv.put(DBhelper.KEY_MODI, rs.getString("PT_MODI"));
                        cv.put(DBhelper.KEY_NAME_C, rs.getString("PT_NOMBRE"));
                        dbs.insert(DBhelper.TABLE_CONNECT, null, cv);

                        // Graba info de Pv de la Tablet en variables
                        variables.movi = rs.getString("PT_PV");
                        variables.fase = rs.getString("PT_FASE");
                        variables.ip = rs.getString("PT_IP_PV");
                        variables.cn = rs.getString("PT_CN");
                        variables.un = rs.getString("PT_UN");
                        variables.pw = rs.getString("PT_PW");
                        variables.modipv = rs.getString("PT_MODI");
                        variables.movi_desc = rs.getString("PT_NOMBRE");
                    }
                    stmt.close();
                    ConnectOra.CerrarConexion();

                } catch (Exception e) {

                    return e.getMessage();
                }

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


        return res;
    }

    private void verifica_mod_guar() {

        String query = "SELECT PM_PRODUCTO FROM " + DBhelper.TABLE_PVMENUS + " WHERE PM_MOVI='" + variables.movi + "' AND PM_FASE='" + variables.fase + "'";

        Cursor rs = dbs.rawQuery(query, null);
        if (rs.moveToFirst()) {
            do {
                String pr = rs.getString(rs.getColumnIndex(DBhelper.KEY_PM_PRODUCTO));

                long countmd = DatabaseUtils.queryNumEntries(dbs, DBhelper.TABLE_PVPRODUCTOSMODOSG, "" + DBhelper.GP_PRODUCTO + "=" + "'" + pr + "'");
                long countgu = DatabaseUtils.queryNumEntries(dbs, DBhelper.TABLE_PVPRODUCTOSMODI, "" + DBhelper.MP_PRODUCTO + "=" + "'" + pr + "'");

                if (countmd > 0) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBhelper.KEY_PM_MODI, "S");
                    dbs.update(DBhelper.TABLE_PVMENUS, cv, "" + DBhelper.KEY_PM_PRODUCTO + "=" + "'" + pr + "'", null);
                }
                if (countgu > 0) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBhelper.KEY_PM_GUAR, "S");
                    dbs.update(DBhelper.TABLE_PVMENUS, cv, "" + DBhelper.KEY_PM_PRODUCTO + "=" + "'" + pr + "'", null);
                }
            } while (rs.moveToNext());
        }
    }

    public class datostablet extends AsyncTask<String, String, String> {
        Toast t;

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            if (resp == "") {
                if (t != null) {
                    t.cancel();
                }
                Snackbar snackbar;
                snackbar = Snackbar.make(findViewById(R.id.snack), "Se ha descargado la información", Snackbar.LENGTH_LONG);
                snackbar.show();
                Drawable drawable = getResources().getDrawable(R.drawable.wheel);
                Drawable drawable2 = progressBar.getIndeterminateDrawable();
                Rect rect = drawable2.getBounds();
                drawable.setBounds(rect);
                progressBar.setIndeterminateDrawable(drawable);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            } else {
                t = Toast.makeText(getApplicationContext(), "Error en conexion. Intentando nuevamente...", Toast.LENGTH_SHORT);
                t.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new datostablet().execute();
                    }
                }, 3000);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String resp;
            resp = verificatablet();
            conexion = ConnectOra.getInstance().getConexion();
            if (resp.equalsIgnoreCase("")) {

                resp = llenatablas();
            }
            if (resp.equalsIgnoreCase("")) {

                verifica_mod_guar();
            }
            return resp;
        }
    }
}
