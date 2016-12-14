package oasispv.pv;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

public class comanda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar backbtn = getSupportActionBar();
        backbtn.setDisplayHomeAsUpEnabled(true);
        ver_comanda();
    }


    private void ver_comanda() {

        DBhelper  dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getWritableDatabase();
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



            ListView laycmdread = (ListView) findViewById(R.id.listprd);
            lcomandar_adapter adapter = new lcomandar_adapter(comanda.this,datos);
            laycmdread.setAdapter(adapter);




        }




    }
}