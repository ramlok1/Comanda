package oasispv.pv;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.Statement;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity{

    ListView listrest;
    Button btnini,btnpvd;


    String[] vrest = new String[]{"CAREYES","DOS LUNAS","WHITE BOX","CIGAR BAR","RODIZIO","SARAPE","BENAZUZA"};
    int[] imagenes = {
            R.drawable.rest
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnini= (Button) findViewById(R.id.btnini);
        btnpvd=(Button) findViewById(R.id.btnpvd);
        btnini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), tables.class);
                Button b = (Button)v;
                intent.putExtra("rest",b.getText().toString());
                startActivity(intent);

            }
        });
        btnpvd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenatablas();
            }
        });



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

    public void llenatablas(){
        ConnectOra db = new ConnectOra("192.168.3.170","XE","PVDATOS","SERVICE");
        Connection conexion=db.getConexion();
        Statement stmt = null;
        String movi="CAREY";
        String fase="7";
    }


}
