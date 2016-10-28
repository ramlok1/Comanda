package oasispv.pv;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class tables extends AppCompatActivity {
    Bundle extras;
    Button btnupd;

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
         extras = getIntent().getExtras();
        setTitle("Restaurante: "+extras.getString("rest").toUpperCase());
        btnupd = (Button) findViewById(R.id.btntablesupd);
        btnupd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leermesas();
            }
        });

        leermesas();

    }

    private void leermesas(){
        ConnectOra db = new ConnectOra("192.168.3.170","XE","PVDATOS","SERVICE");
        Connection conexion=db.getConexion();
        Statement stmt = null;
        String movi="CAREY";
        String fase="7";


        LinearLayout btnsContainer = (LinearLayout) findViewById(R.id.btnlmaster);
        //Crea botons dinamicamente.
        int contl=1;
        Boolean ban = false;
        LinearLayout contenedor = null;
        String query = "select 'M'||lpad(to_char(CE_MESA),2,'0') MESA, CE_HABI, CE_PAX, CE_ABRE_H, CE_MESERO"+
                " FROM PVCHEQDIAENC where CE_MOVI='" + movi+"' and CE_FASE='"+fase+"' and CE_CIERRA_F IS NULL"+
                " and CE_CAN_F IS NULL and CE_MESA IS NOT NULL";

        try {
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {

                String mesa = rs.getString("MESA");
                if (!ban){
                    ban=true;
                    contenedor = new LinearLayout(this);
                    contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                    contenedor.setOrientation((LinearLayout.HORIZONTAL));
                    //contenedor.setId(i);
                    btnsContainer.addView(contenedor);
                }
                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
                btn.setText(rs.getString("MESA")+"\n"+rs.getString("CE_HABI")+"\n"+rs.getString("CE_MESERO"));
                contenedor.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(), platillos.class);
                        Button b = (Button)v;
                        intent.putExtra("rest",extras.getString("rest").toUpperCase());
                        intent.putExtra("table",b.getText().toString());
                        startActivity(intent);

                    }
                });
                contl=contl+1;
                if (contl==5){
                    contl=1;
                    ban=false;
                }


            }
        } catch (SQLException e ) {
            System.out.println(e);
        }

    }


}
