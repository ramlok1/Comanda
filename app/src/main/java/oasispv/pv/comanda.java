package oasispv.pv;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class comanda extends AppCompatActivity {
    Button addpr, closecmd;
    TextView total_precio;
    private DBhelper dbhelper;
    SQLiteDatabase dbs;
    ConnectOra db = ConnectOra.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar backbtn = getSupportActionBar();
        backbtn.setDisplayHomeAsUpEnabled(true);
        dbhelper = new DBhelper(getApplicationContext());
        dbs = dbhelper.getWritableDatabase();


        addpr = (Button) findViewById(R.id.btnaddpr);
        total_precio = (TextView) findViewById(R.id.txttprecio);
        closecmd = (Button) findViewById(R.id.btncerrarcmd);
        variables.tprecio = 0;
        ver_comanda();

        total_precio.setText(Float.toString(variables.tprecio));

        addpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), platillos.class);
                startActivity(intent);
            }
        });

        // Trigger Boton cerrar comanda
        closecmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Float.parseFloat(total_precio.getText().toString()) > 0) {
                    Toast.makeText(comanda.this, "Comanda con saldo, favor de cerrar en Punto de Venta", Toast.LENGTH_LONG).show();
                } else {
                    /// Mensaje confirmacion cierre comanda
                    final AlertDialog.Builder msgcerrar = new AlertDialog.Builder(comanda.this);
                    msgcerrar.setTitle("Confirmar cierre");
                    msgcerrar.setMessage("Seguro desea cerrar la comanda?");
                    msgcerrar.setCancelable(false);


                    msgcerrar.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface msgcerrar, int id) {
                            msgcerrar.dismiss();
                            // Hilo de cierre comanda
                            final ProgressDialog pd = ProgressDialog.show(comanda.this, "", "Cerrando Comanda...");
                            pd.setCancelable(false);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {

                                        db.cierra_comanda_enc();// Metodo para cerrar comanda en ConnectOra
                                    } catch (SQLException e) {
                                        e.printStackTrace();

                                    }
                                    /// Actualiza la informacion de la comanda en la tablet
                                    ContentValues cv = new ContentValues();
                                    cv.put(DBhelper.CE_STATUS, "C");
                                    dbs.update(DBhelper.TABLE_COMANDAENC, cv, "CE_STATUS='A'  AND CE_MESA='" + variables.mesa + "'", null);

                                    //COMANDA DETALLE
                                    ContentValues cd = new ContentValues();
                                    cd.put(DBhelper.CMD_STATUS, "C");
                                    dbs.update(DBhelper.TABLE_COMANDA, cd, "STATUS='E'  AND MESA='" + variables.mesa + "'", null);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pd.dismiss();// cierra progress dialog
                                        }
                                    });
                                }
                            }).start();

                            // regresa a activity de mesas
                            Intent intent = new Intent(getApplicationContext(), tables.class);
                            startActivity(intent);

                        }
                    });

                    msgcerrar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface msgcerrar, int id) {
                            msgcerrar.dismiss();
                        }
                    });

                    msgcerrar.show();//muestra mensaje

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater men = getMenuInflater();
        men.inflate(R.menu.mecambio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Spinner input = new Spinner(this);
        switch (item.getItemId()) {
            case R.id.cam_mesa:

                // Sacar mesas disponibles
                String query = "SELECT MESA,MESA_NAME FROM PVMESA WHERE MESA NOT IN (SELECT CE_MESA FROM COMANDAENC WHERE CE_STATUS='A') ORDER BY MESA_NAME";
                Cursor rs = dbs.rawQuery(query, null);
                ArrayList<String> nmesa = new ArrayList<>();
                ArrayList<String> nom_mesa = new ArrayList<>();
                if (rs.moveToFirst()) {
                    do {
                        try {
                            nmesa.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA)));
                            nom_mesa.add(rs.getString(rs.getColumnIndex(DBhelper.KEY_MESA_NAME)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (rs.moveToNext());
                }
                rs.close();
                ///////////////////////////////////////////////////////
                // Prepara info para spinner
                String[] spinnerArray = new String[nmesa.size()];
                final HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();
                for (int i = 0; i < nmesa.size(); i++) {
                    spinnerMap.put(i, nmesa.get(i));
                    spinnerArray[i] = nom_mesa.get(i);
                }
                /// Llena spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinneritem, spinnerArray);
                adapter.setDropDownViewResource(R.layout.spinneritem);
                input.setAdapter(adapter);

                //Dialogo para mostrar spinner
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Cambio de mesa");

                // Set up the input

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mesa = spinnerMap.get(input.getSelectedItemPosition());
                        try {
                            db.cambio_mesa(mesa);
                            Intent intent = new Intent(getApplicationContext(), tables.class);
                            startActivity(intent);
                        } catch (SQLException e) {
                            e.printStackTrace();

                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ver_comanda() {

        String query = "SELECT ID,PRDESC,CANTIDAD,COMENSAL,TIEMPO,NOTA,PRECIO FROM " + DBhelper.TABLE_COMANDA + " WHERE MESA='" + variables.mesa + "'  AND STATUS='E' AND SESION='" + variables.sesion + "'";
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
                            rs.getString(rs.getColumnIndex(DBhelper.CMD_NOTA)),
                            rs.getFloat(rs.getColumnIndex(DBhelper.CMD_PRECIO))
                    ));
                    variables.tprecio = variables.tprecio + rs.getFloat(rs.getColumnIndex(DBhelper.CMD_PRECIO));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (rs.moveToNext());
            rs.close();
            // reseteo variable de total de precio

            ListView laycmdread = (ListView) findViewById(R.id.listprd);
            lcomandar_adapter adapter = new lcomandar_adapter(comanda.this, datos);
            laycmdread.setAdapter(adapter);


        }


    }

}
