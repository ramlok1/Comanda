package oasispv.pv;

        import android.util.Log;

        import java.sql.CallableStatement;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;
        import java.sql.Statement;

public class ConnectOra {
    public String driver, url;//, ip, bd, usr, pass;
    public Connection conexion;
    private Statement stmt;

    public ConnectOra(String ip,String bd,String usr, String pass) {
        driver = "oracle.jdbc.driver.OracleDriver";
        // driver = "oracle.jdbc.OracleDriver";
      /*  this.ip = "192.168.1.8";
        this.bd = "CRONOS";
        this.usr = "FRGRAND";
        this.pass = "SERVICE";*/

        url = new String("jdbc:oracle:thin:@" + ip + ":1521:" + bd);

        try {
            Class.forName(driver).newInstance();

            conexion = DriverManager.getConnection(url, usr, pass);
            // revisan el log para ver que tira....
            System.out.println("Conexion a Base de Datos " + bd + " Exitosa");

        } catch (Exception exc) {
            System.out.println("Error al tratar de abrir la base de Datos "
                    + bd + " : " + exc);
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public Connection CerrarConexion() throws SQLException {
        conexion.close();
        conexion = null;
        return conexion;
    }
    public String getlogin(String usr, String pwd) throws SQLException {
        String res;
        CallableStatement login = conexion.prepareCall("{? = call  check_login(?,?)}");
        login.registerOutParameter(1, java.sql.Types.INTEGER);
        login.setString(2, usr.toUpperCase());
        login.setString(3, pwd.toUpperCase());

        login.execute();
        conexion.close();
        if (login.getInt(1)==1){res="Login Ok";}
        else{
            res="Usuario Incorrecto";
        }
        return res;
    }
}
