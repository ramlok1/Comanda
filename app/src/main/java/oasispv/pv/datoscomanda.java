package oasispv.pv;

/**
 * Created by Usuario on 01/12/2016.
 */

public class datoscomanda {
    public String prdesc;
    public int cantidad;
    public int comensal;
    public int tiempo;
    public int idpr;
    public float precio;
    public String nota;

    public datoscomanda(String prdesc, Integer cantidad, Integer comensal, Integer tiempo, Integer idpr, String nota, Float precio) {
        this.prdesc = prdesc;
        this.cantidad = cantidad;
        this.comensal = comensal;
        this.tiempo = tiempo;
        this.idpr = idpr;
        this.nota = nota;
        this.precio = precio;

    }
}
