package oasispv.pv;


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



public class tables extends AppCompatActivity {

    private DBhelper dbhelper;


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

        leermesas();




    }
    private void leermesas(){
        dbhelper = new DBhelper(getApplicationContext());
        SQLiteDatabase dbs = dbhelper.getWritableDatabase();

            LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.btnlmaster);
            btnsContainer.removeAllViews();
            //Crea botons dinamicamente.
            int contl = 1;
            Boolean ban = false;
            LinearLayout contenedor = null;


            String query = "SELECT id,MESA_NAME,HABI,MESA_MESERO FROM " + DBhelper.TABLE_PVMESA + " WHERE MOVI='" + variables.movi + "' AND FASE='" + variables.fase + "'";

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
                    Button btn = new Button(this);
                    btn.setLayoutParams(new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT));
                    btn.setText(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_NAME))+"\n"+rs.getString(rs.getColumnIndex(DBhelper.KEY_HABI))+"\n"+rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_MESERO)));
                    btn.setTag(rs.getString(rs.getColumnIndex(DBhelper.KEY_ID)));
                    contenedor.addView(btn);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getApplicationContext(), platillos.class);
                            Button b = (Button)v;
                            intent.putExtra("table",b.getText().toString());
                            intent.putExtra("table_id",b.getTag().toString());
                            startActivity(intent);

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
        dbs.close();


    }




}
