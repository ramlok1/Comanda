package oasispv.pv;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;


public class MainActivity extends AppCompatActivity {
    private DBhelper dbhelper;
    private SQLiteDatabase dbs;
    Button btnini;
    TextView txtuser;
    TextView txtpwd;
    ConnectOra db = ConnectOra.getInstance();
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


        btnini = (Button) findViewById(R.id.btnini);
        txtuser = (TextView) findViewById(R.id.usrtxt);
        txtpwd = (TextView) findViewById(R.id.pwdtxt);
        final TextView txtturno = (TextView) findViewById(R.id.txtturno);
        btnini.setText(variables.movi_desc);

        btnini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = txtuser.getText().toString().trim();
                String pwd = txtpwd.getText().toString().trim();
                int loging ;

                try {
                    loging = db.getlogin(usr, pwd);
                } catch (Exception e) {
                    loging = 2;

                }

                if (loging == 1) {

                    variables.mesero = txtuser.getText().toString().trim();
                    variables.turno = Integer.parseInt(txtturno.getText().toString());


                    dbs.delete(DBhelper.TABLE_SESION, null, null);
                    ContentValues cv = new ContentValues();
                    cv.put(DBhelper.SES_MESERO, variables.mesero);
                    cv.put(DBhelper.SES_MOVI, variables.movi);
                    cv.put(DBhelper.SES_FASE, variables.fase);
                    cv.put(DBhelper.SES_STATUS, "A");
                    dbs.insert(DBhelper.TABLE_SESION, null, cv);

                    Intent intent = new Intent(getApplicationContext(), tables.class);
                    startActivity(intent);
                } else if (loging == 2) {
                    Toast.makeText(getApplicationContext(), "Favor de revisar Conexion", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Favor de revisar Usuario y Contraseña", Toast.LENGTH_LONG).show();
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
                if (turno == 1) {
                    txtturno.setText("1");
                } else {
                    turno = turno - 1;
                    txtturno.setText(Integer.toString(turno));
                }

            }
        });

        Button btnmast = (Button) findViewById(R.id.btnmastu);
        btnmast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int turno = Integer.parseInt(txtturno.getText().toString());
                if (turno == 3) {
                    txtturno.setText("3");
                } else {
                    turno = turno + 1;
                    txtturno.setText(Integer.toString(turno));
                }

            }
        });
        //Mantener Wifi Activo
        wifiManager.keepWiFiOn(getApplicationContext(), true);


    }

    @Override
    protected void onDestroy() {
        super.onStop();
        dbs.close();
        try {
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
