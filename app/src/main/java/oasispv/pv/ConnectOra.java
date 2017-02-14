package oasispv.pv;



        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;

        import java.sql.CallableStatement;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;




public class ConnectOra {

    private static String driver, url;
    private static  Connection conexion;


    public  ConnectOra(String ip,String bd,String usr, String pass) {
        driver = "oracle.jdbc.driver.OracleDriver";


        url = new String("jdbc:oracle:thin:@" + ip + ":1521:" + bd);

        try {
            Class.forName(driver).newInstance();

            conexion = DriverManager.getConnection(url, usr, pass);
            // revisan el log para ver que tira....
            System.out.println("Conexion a Base de Datos " + bd + " Exitosa");

        } catch (Exception exc) {
            variables.erroroc=1;
            System.out.println("Error al tratar de abrir la base de Datos "
                    + bd + " : " + exc);
        }
    }

    public static Connection getConexion() {
        return conexion;
    }

    public static Connection CerrarConexion() throws SQLException {
        conexion.close();
        conexion = null;
        return conexion;
    }
    public int getlogin(String usr, String pwd) throws SQLException {
        int res;
        CallableStatement login = conexion.prepareCall("{? = call  cielo.check_login(?,?)}");
        login.registerOutParameter(1, java.sql.Types.INTEGER);
        login.setString(2, usr.toUpperCase());
        login.setString(3, pwd.toUpperCase());

        login.execute();
        //conexion.close();
        if (login.getInt(1)==1){res=1;}
        else{
            res=0;
        }
        return res;
    }
    public String genera_transa() throws SQLException {
        String res;
        CallableStatement transa = conexion.prepareCall("{? = call  CIELOPV.GENERA_TRANSA_ENC}");
        transa.registerOutParameter(1, java.sql.Types.VARCHAR);


        transa.execute();
        //conexion.close();
      res= transa.getString(1);

        return res;
    }
    public String genera_transa_det() throws SQLException {
        String res;
        CallableStatement transa = conexion.prepareCall("{? = call  CIELOPV.GENERA_TRANSA_DET}");
        transa.registerOutParameter(1, java.sql.Types.VARCHAR);


        transa.execute();
        //conexion.close();
        res= transa.getString(1);

        return res;
    }
    public void  inserta_comanda_enc() throws SQLException {

        String qry_fecha = "SELECT PR_FECHA FROM PVFRONT.FRPARAM";

        Statement s = conexion.createStatement();
        ResultSet r= s.executeQuery(qry_fecha);
        r.next();
       String fecha = Utils.fecha(r.getDate("PR_FECHA"));



        String sql = "insert into PVCHEQDIAENC (CE_MOVI,CE_FASE,CE_TRANSA,CE_MESA,CE_RESERVA,CE_HABI,CE_PAX,CE_FECHA,CE_MESERO," +
                "CE_ABRE_U,CE_ABRE_F,CE_ABRE_H,CE_MACHINE,CE_TURNO)"
                + " VALUES ('" + variables.movi + "'," + "'" + variables.fase + "'," + "'"
                + variables.cmd + "'," + "'" + Integer.parseInt(variables.mesa) + "'," + "'" + variables.rva + "',"+ "'" + variables.habi + "',"+ "'" + variables.mesa_pax + "',"
                +"to_date('" + fecha + "','DD/MM/YY')," + "'" + variables.mesero + "'," + "'" + variables.mesero + "',"
                +"to_date('" + fecha + "','DD/MM/YY')," + "'" + Utils.Hora(new Date()).toString() + "'," + "'" + "tablet" + "',"
                +  variables.turno + ")";

        s.executeQuery(sql);
        s.close();







    }
    public void  cierra_comanda_enc() throws SQLException {
       String res= calcula_comanda();

            String qry_fecha = "SELECT PR_FECHA FROM PVFRONT.FRPARAM";

            Statement s = conexion.createStatement();
            ResultSet r = s.executeQuery(qry_fecha);
            r.next();
            String fecha = Utils.fecha(r.getDate("PR_FECHA"));


            String sql = "UPDATE PVCHEQDIAENC SET CE_CIERRA_F=" + "to_date('" + fecha + "','DD/MM/YYYY')," +
                    "CE_CIERRA_H='" + Utils.Hora(new Date()).toString() + "', CE_CIERRA_U='" + variables.mesero
                    + "' WHERE CE_MOVI='" + variables.movi + "' AND CE_FASE= '" + variables.fase + "' AND CE_TRANSA= '" + variables.cmd + "' AND CE_MESA= '" + Integer.parseInt(variables.mesa)+"'";

            s.executeQuery(sql);
            s.close();


    }
    public void  cambio_mesa(String mesa) throws SQLException {

        Statement s = conexion.createStatement();
        String sql = "UPDATE PVCHEQDIAENC SET CE_MESA='"+Integer.parseInt(mesa)
                +"' WHERE CE_MOVI='" + variables.movi + "' AND CE_FASE= '" + variables.fase + "' AND CE_TRANSA= '" + variables.cmd + "' AND CE_MESA= '" + Integer.parseInt(variables.mesa)+"'";

        s.executeQuery(sql);
        s.close();


    }
    public void  inserta_comanda_det(DBhelper dbhelper) throws SQLException {

        SQLiteDatabase dbs = dbhelper.getWritableDatabase();

        String qry_fecha = "SELECT PR_FECHA FROM PVFRONT.FRPARAM";

        Statement s = conexion.createStatement();

        ResultSet r= s.executeQuery(qry_fecha);
        r.next();
        String fecha = Utils.fecha(r.getDate("PR_FECHA"));

        String sql_pr="SELECT ID,PRID,CANTIDAD,TIEMPO,NOTA,PRECIO,CARTA,COMENSAL FROM " + DBhelper.TABLE_COMANDA + " WHERE STATUS='A' AND SESION='"+variables.sesion+"' AND MESA='"+variables.mesa+"'";

        Cursor rs = dbs.rawQuery(sql_pr, null);
        if (rs.getCount() > 0) {
            rs.moveToFirst();
            do {
                String transa = genera_transa_det();


                String sql = "insert into PVCHEQDIADET (CD_MOVI, CD_FASE, CD_TRANSA, CD_ID, CD_PRODUCTO,CD_CANTIDAD, CD_PRECIO, CD_IVA, CD_IMPORTE, CD_DESCTO_IMP, CD_TOTAL, CD_TIEMPO, CD_MOD, CD_CAP_H, CD_CAP_U, CD_PROPINA_INC, CD_COMISION_INC, CD_COSTO,  CD_NOTAS,CD_CARTA,CD_RECETA)"
                        + " VALUES ('" + variables.movi + "'," + "'" + variables.fase + "'," + "'"
                        + variables.cmd + "'," + "'" + transa + "'," + "'" + rs.getString(rs.getColumnIndex(DBhelper.CMD_PRID)) + "',"
                        + rs.getInt(rs.getColumnIndex(DBhelper.CMD_CANTIDAD)) + "," + rs.getInt(rs.getColumnIndex(DBhelper.CMD_PRECIO)) + "," + (rs.getInt(rs.getColumnIndex(DBhelper.CMD_PRECIO))/1.16)*.16 + "," + rs.getInt(rs.getColumnIndex(DBhelper.CMD_PRECIO)) + "," + 0 + "," + rs.getInt(rs.getColumnIndex(DBhelper.CMD_PRECIO)) + ","
                        + "'" + rs.getString(rs.getColumnIndex(DBhelper.CMD_TIEMPO)) + "'," + "'" + "0" + "'," + "'" + Utils.Hora(new Date()).toString() + "'," + "'" + variables.mesero + "'," + 0 + "," + 0 + "," + 0 + ","
                        + "'" + rs.getString(rs.getColumnIndex(DBhelper.CMD_NOTA)) + "','" +  rs.getString(rs.getColumnIndex(DBhelper.CMD_CARTA)) + "','"+rs.getInt(rs.getColumnIndex(DBhelper.CMD_COMENSAL))+"')";

                s.executeQuery(sql);

                inserta_modificadores_oracle(dbhelper,rs.getString(rs.getColumnIndex(DBhelper.CMD_PRID)),transa,rs.getInt(rs.getColumnIndex(DBhelper.KEY_ID)));


            } while (rs.moveToNext());
        }


        s.close();

        String mac = Utils.getMACAddress("wlan0");
        mac = mac.replace(":","");

        CallableStatement comanda = conexion.prepareCall("begin CIELOPV.COMANDERO(?,?,?,?,?,?); end;");
        comanda.setString(1, mac); // Sesion
        comanda.setString(2, variables.mesero); // usuario
        comanda.setString(3, variables.movi); // movi
        comanda.setString(4, variables.fase); // fase
        comanda.setString(5, variables.cmd); // transa
        comanda.setString(6,"TABLET" ); // Hora

        comanda.execute();










    }
    public String  calcula_comanda() throws SQLException {



        String mac = Utils.getMACAddress("wlan0");
        mac = mac.replace(":","");
        String res;
        CallableStatement comanda = conexion.prepareCall("{? = call  CIELOPV.CALCULA_CHEQUE(?,?,?,?,?)}");
        comanda.registerOutParameter(1, java.sql.Types.VARCHAR);
        comanda.setString(2, mac); // Sesion
        comanda.setString(3, variables.mesero); // usuario
        comanda.setString(4, variables.movi); // movi
        comanda.setString(5, variables.fase); // fase
        comanda.setString(6, variables.cmd); // transa

        comanda.execute();
        //conexion.close();
        res=comanda.getString(1);

        return res;

    }
    public void  inserta_modificadores_oracle(DBhelper dbhelper, String prid,String transa,int id) throws SQLException {


        SQLiteDatabase dbs = dbhelper.getWritableDatabase();

        String query = "SELECT * FROM " + DBhelper.TABLE_PVMODCG + " WHERE CG_COMANDA='"
                + variables.cmd + "'  AND CG_PRODUCTO='"+prid+"' AND CG_COMANDA_DET="+id;

        String queryguar = "SELECT * FROM " + DBhelper.TABLE_PVGUAR + " WHERE GU_COMANDA='"
                + variables.cmd + "'  AND GU_PRODUCTO='"+prid+"' AND GU_COMANDA_DET="+id;


        Statement s = conexion.createStatement();
        Cursor r = dbs.rawQuery(query, null);
        Cursor rs = dbs.rawQuery(queryguar, null);

        if (rs.getCount() > 0) {
            rs.moveToFirst();
            do {
                String sql = "insert into PVCHEQDIADETMODI (DM_MOVI, DM_FASE, DM_TRANSA, DM_ID, DM_MODI, DM_DESC, DM_DEFAULT, DM_VALOR)"
                        + " VALUES ('" + variables.movi + "'," + "'" + variables.fase + "'," + "'"
                        + variables.cmd + "'," + "'" + transa + "'," + "'" + rs.getString(rs.getColumnIndex(DBhelper.GU_GUAR)) + "','"
                        + rs.getInt(rs.getColumnIndex(DBhelper.GU_DESC)) + "',"
                        + "'" + rs.getString(rs.getColumnIndex(DBhelper.GU_DEFAULT)) + "',"
                        + "'" + rs.getString(rs.getColumnIndex(DBhelper.GU_SELECCION)) +"')";

                s.executeQuery(sql);
            } while (rs.moveToNext());
        }

        if (r.getCount() > 0) {
            r.moveToFirst();
            do {
                String sql = "insert into PVCHEQDIADETMODO (MP_MOVI, MP_FASE, MP_TRANSA,MP_ID,MP_GRUPO, MP_GRUPO_DESC, MP_GRUPO_MANDAT, MP_MODO, MP_MODO_DESC, MP_DEFAULT, MP_VALOR)"
                        + " VALUES ('" + variables.movi + "'," + "'" + variables.fase + "'," + "'"
                        + variables.cmd + "'," + "'" + transa + "'," + "'" + r.getString(r.getColumnIndex(DBhelper.CG_GRUPO)) + "','"
                        + r.getInt(r.getColumnIndex(DBhelper.CG_GRUPO_DESC)) + "','"+ r.getInt(r.getColumnIndex(DBhelper.CG_MANDA)) + "',"
                        + "'" + r.getString(r.getColumnIndex(DBhelper.CG_MODO)) + "',"+ "'" + r.getString(r.getColumnIndex(DBhelper.CG_DESC)) + "',"
                        + "'" + r.getString(r.getColumnIndex(DBhelper.CG_DEFAULT)) +"','" + r.getString(r.getColumnIndex(DBhelper.CG_SELECCION)) +"')";

                s.executeQuery(sql);
            } while (r.moveToNext());
        }

        s.close();







    }

}
