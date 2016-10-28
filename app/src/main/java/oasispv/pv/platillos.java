package oasispv.pv;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class platillos extends AppCompatActivity {
Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platillos);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        ActionBar backbtn = getSupportActionBar();
        backbtn.setDisplayHomeAsUpEnabled(true);
        extras = getIntent().getExtras();
        setTitle("Restaurante: "+extras.getString("rest").toUpperCase()+"     Mesa: "+extras.getString("table").toUpperCase());
        scrollcat1();
    }



    private void scrollcat1() {
        ConnectOra db = new ConnectOra("192.168.3.170", "XE", "PVDATOS", "SERVICE");
        Connection conexion = db.getConexion();
        Statement stmt = null;
        String movi = "CAREY";
        String fase = "7";

        LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.laygrupo);
        //Crea botons dinamicamente.
        int contl = 1;
        Boolean ban = false;
        LinearLayout contenedor = null;

        String query = "SELECT DISTINCT PM_CAT1,C1_DESC FROM PVMENUS,PVMENUSCAT1 WHERE PM_MOVI='" + movi + "' AND PM_FASE='" + fase + "' AND C1_CAT1=PM_CAT1";


        try {
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {

                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                btn.setText(rs.getString("C1_DESC"));
                btn.setTag(rs.getString("PM_CAT1"));
                btnsContainer.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        scrollcat2();

                    }
                });
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        try {
            db.CerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void scrollcat2() {
        ConnectOra db = new ConnectOra("192.168.3.170", "XE", "PVDATOS", "SERVICE");
        Connection conexion = db.getConexion();
        Statement stmt = null;
        String movi = "CAREY";
        String fase = "7";

        LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.laysuvgr);
        //Crea botons dinamicamente.
        int contl = 1;
        Boolean ban = false;
        LinearLayout contenedor = null;
        String query = "SELECT DISTINCT PM_CAT2,C2_DESC FROM PVMENUS,PVMENUSCAT2 WHERE PM_MOVI='" + movi + "' AND PM_FASE='" + fase + "' AND C2_CAT2=PM_CAT2";


        try {
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {

                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                btn.setText(rs.getString("C2_DESC"));
                btn.setTag(rs.getString("PM_CAT2"));
                btnsContainer.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        scrollplatillos();

                    }
                });
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    private void scrollplatillos(){
        LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.layplatillos);
        //Crea botons dinamicamente.
        int contl=1;
        Boolean ban = false;
        LinearLayout contenedor = null;


        for (int i = 0; i < 20; i++){
            if (!ban){
                ban=true;
                contenedor = new LinearLayout(this);
                contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                contenedor.setOrientation((LinearLayout.HORIZONTAL));
                contenedor.setId(i);
                btnsContainer.addView(contenedor);
            }
            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
            btn.setId(i);
            btn.setText("TB" +  i);
            btn.setTag("TB"+i);
            contenedor.addView(btn);
          btn.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                  Toast.makeText(getApplicationContext(),"Agregado",Toast.LENGTH_SHORT).show();
                  return false;
              }
          });
            contl=contl+1;
            if (contl==5){
                contl=1;
                ban=false;
            }
            //Va agregegando botones al contenedor.

        }

    }
}
