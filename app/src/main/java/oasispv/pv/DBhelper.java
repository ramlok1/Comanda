package oasispv.pv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {


    private static DBhelper mInstance = null;

    public static DBhelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (mInstance == null) {
            mInstance = new DBhelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "comandero";

    // Table Names
    public static final String TABLE_PVCAT1 = "PVCAT1";
    public static final String TABLE_PVCAT2 = "PVCAT2";
    public static final String TABLE_PVPRODUCTOSMODI = "PVPRODUCTOSMODI";
    public static final String TABLE_PVMODOS = "PVMODOS";
    public static final String TABLE_PVMODOSG = "PVMODOSG";
    public static final String TABLE_PVMODCG = "PVMODCG";
    public static final String TABLE_PVGUAR = "PVGUAR";
    public static final String TABLE_TMP_PVMODCG = "TMP_PVMODCG";
    public static final String TABLE_TMP_PVGUAR = "TMP_PVGUAR";
    public static final String TABLE_PVPRODUCTOSMODOSG = "PVPRODUCTOSMODOSG";
    public static final String TABLE_PVMENUS = "PVMENUS";
    public static final String TABLE_PVMESA = "PVMESA";
    public static final String TABLE_CONNECT = "CONEXION";
    public static final String TABLE_COMANDA = "COMANDA";
    public static final String TABLE_SESION = "SESION";
    public static final String TABLE_PRMOD = "PRMOD";
    public static final String TABLE_COMANDAENC = "COMANDAENC";
    public static final String TABLE_PVRVANOMBRE = "PVRVANOMBRE";
    public static final String TABLE_BRAZA = "BRAZALETES";

    // Common column names
    public static final String KEY_ID = "id";

    // TABLA PVCAT1
    public static final String KEY_CAT1_MOVI = "CAT1_MOVI";
    public static final String KEY_CAT1_FASE = "CAT1_FASE";
    public static final String KEY_CAT1_CARTA = "CAT1_CARTA";
    public static final String KEY_CAT1 = "CAT1";
    public static final String KEY_CAT1_IMAGEN = "CAT1_IMAGEN";
    public static final String KEY_CAT1_DESC = "CAT1_DESC";
    public static final String KEY_CAT1_POS = "CAT1_POS";

    // TABLA PVCAT2
    public static final String KEY_CAT2_MOVI = "CAT2_MOVI";
    public static final String KEY_CAT2_FASE = "CAT2_FASE";
    public static final String KEY_CAT2_CARTA = "CAT2_CARTA";
    public static final String KEY_CAT2_CAT1 = "CAT2_CAT1";
    public static final String KEY_CAT2 = "CAT2";
    public static final String KEY_CAT2_IMAGEN = "CAT2_IMAGEN";
    public static final String KEY_CAT2_DESC = "CAT2_DESC";
    public static final String KEY_CAT2_POS = "CAT2_POS";

    // TABLA PVMENUS
    public static final String KEY_PM_MOVI = "PM_MOVI";
    public static final String KEY_PM_FASE = "PM_FASE";
    public static final String KEY_PM_CAT1 = "PM_CAT1";
    public static final String KEY_PM_CAT2 = "PM_CAT2";
    public static final String KEY_PM_PRODUCTO = "PM_PRODUCTO";
    public static final String KEY_PM_PRODUCTO_DESC = "PM_PRODUCTO_DESC";
    public static final String KEY_PM_POS = "PM_POS";
    public static final String KEY_PM_PRECIO = "PM_PRECIO";
    public static final String KEY_PM_IVA = "PM_IVA";
    public static final String KEY_PM_CARTA = "PM_CARTA";
    public static final String KEY_PM_COMISION = "PM_COMISION";
    public static final String KEY_PM_PROPINA = "PM_PROPINA";
    public static final String KEY_PM_FAMILIA = "PM_FAMILIA";
    public static final String KEY_PM_GRUPOPR = "PM_GRUPOPR";
    public static final String KEY_PM_SUBFAMILIAPR = "PM_SUBFAMILIAPR";
    public static final String KEY_PM_MODI = "PM_MODI";
    public static final String KEY_PM_GUAR = "PM_GUAR";

    // TABLA PVMESA
    public static final String KEY_MOVI = "MOVI";
    public static final String KEY_FASE = "FASE";
    public static final String KEY_MESA = "MESA";
    public static final String KEY_MESA_NAME = "MESA_NAME";
    public static final String KEY_MESA_MESERO = "MESA_MESERO";
    public static final String KEY_HABI = "HABI";
    public static final String KEY_GUEST = "GUEST";
    public static final String KEY_PAX = "PAX";

    // TABLA CONEXION
    public static final String KEY_TIPO = "TIPO";
    public static final String KEY_MOVI_C = "MOVI";
    public static final String KEY_FASE_C = "FASE";
    public static final String KEY_NAME_C = "NAME";
    public static final String KEY_UN = "UN";
    public static final String KEY_PW = "PW";
    public static final String KEY_CN = "CN";
    public static final String KEY_IP = "IP";
    public static final String KEY_MODI = "MODI";

    // TABLA COMANDA
    public static final String CMD_SESION = "SESION";
    public static final String CMD_TRANSA = "TRANSA";
    public static final String CMD_MESA = "MESA";
    public static final String CMD_PRID = "PRID";
    public static final String CMD_PRDESC = "PRDESC";
    public static final String CMD_CANTIDAD = "CANTIDAD";
    public static final String CMD_PRECIO = "PRECIO";
    public static final String CMD_CARTA = "CARTA";
    public static final String CMD_COMENSAL = "COMENSAL";
    public static final String CMD_TIEMPO = "TIEMPO";
    public static final String CMD_NOTA = "NOTA";
    public static final String CMD_STATUS = "STATUS";

    // TABLA SESION
    public static final String SES_MESERO = "MESERO";
    public static final String SES_MOVI = "MOVI";
    public static final String SES_FASE = "FASE";
    public static final String SES_STATUS = "STATUS";

    // TABLA COMANDAENC
    public static final String CE_SESION = "CE_SESION";
    public static final String CE_TRANSA = "CE_TRANSA";
    public static final String CE_MESA = "CE_MESA";
    public static final String CE_MESERO = "CE_MESERO";
    public static final String CE_STATUS = "CE_STATUS";

    // TABLA PVPRODUCTOSMODI
    public static final String MP_PRODUCTO = "MP_PRODUCTO";
    public static final String MP_MODI = "MP_MODI";
    public static final String MP_DEFAULT = "MP_DEFAULT";


    // TABLA PVMODOS
    public static final String MD_GRUPO = "MD_GRUPO";
    public static final String MD_MODO = "MD_MODO";
    public static final String MD_DESC = "MD_DESC";
    public static final String MD_DEFAULT = "MD_DEFAULT";

    // TABLA PVMODOSG
    public static final String MG_GRUPO = "MG_GRUPO";
    public static final String MG_DESC = "MG_DESC";
    public static final String MG_MANDAT = "MG_MANDAT";

    // TABLA PVMODCG
    public static final String CG_COMANDA = "CG_COMANDA";
    public static final String CG_COMANDA_DET = "CG_COMANDA_DET";
    public static final String CG_PRODUCTO = "CG_PRODUCTO";
    public static final String CG_GRUPO = "CG_GRUPO";
    public static final String CG_GRUPO_DESC = "CG_GRUPO_DESC";
    public static final String CG_MODO = "CG_MODO";
    public static final String CG_DESC = "CG_DESC";
    public static final String CG_MANDA = "CG_MANDA";
    public static final String CG_DEFAULT = "CG_DEFAULT";
    public static final String CG_SELECCION = "CG_SELECCION";

    // TABLA PVMODGUAR
    public static final String GU_COMANDA = "GU_COMANDA";
    public static final String GU_COMANDA_DET = "GU_COMANDA_DET";
    public static final String GU_PRODUCTO = "GU_PRODUCTO";
    public static final String GU_GUAR = "GU_GUAR";
    public static final String GU_DESC = "GU_DESC";
    public static final String GU_DEFAULT = "GU_DEFAULT";
    public static final String GU_SELECCION = "GU_SELECCION";


    // TABLA PVPRODUCTOSMODOSG
    public static final String GP_PRODUCTO = "GP_PRODUCTO";
    public static final String GP_GRUPO = "GP_GRUPO";
    public static final String GP_GRUPO_DESC = "GP_GRUPO_DESC";

    // TABLA PRMOD
    public static final String PD_PRODUCTO = "PD_PRODUCTO";
    public static final String PD_DESC = "PD_DESC";

    // TABLA PVRVANOMBRE
    public static final String PN_RESERVA = "PN_RESERVA";
    public static final String PN_HABI = "PN_HABI";
    public static final String PN_NOMBRE = "PN_NOMBRE";
    public static final String PN_SEC = "PN_SEC";
    // TABLA BRAZALETES
    public static final String BU_RESERVA = "BU_RESERVA";
    public static final String BU_FOLIO = "BU_FOLIO";


    // Table Create Statements

    // CREAR TABLA PVRVANOMBRE
    private static final String CREATE_TABLE_PVRVANOMBRE = "CREATE TABLE "
            + TABLE_PVRVANOMBRE + "(" + PN_RESERVA + " TEXT," + PN_HABI + " TEXT,"
            + PN_NOMBRE + " TEXT," + PN_SEC + " INTEGER"
            + ")";
    // CREAR TABLA BRAZALETES
    private static final String CREATE_TABLE_BRAZA = "CREATE TABLE "
            + TABLE_BRAZA + "(" + BU_RESERVA + " TEXT,"
            + BU_FOLIO + " TEXT"
            + ")";
    // CREAR TABLA PRMOD
    private static final String CREATE_TABLE_PRMOD = "CREATE TABLE "
            + TABLE_PRMOD + "(" + PD_PRODUCTO + " TEXT,"
            + PD_DESC + " TEXT"
            + ")";
    // CREAR TABLA COMANDAENC
    private static final String CREATE_TABLE_COMANDAENC = "CREATE TABLE "
            + TABLE_COMANDAENC + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + CE_SESION + " TEXT," + CE_TRANSA + " TEXT," + CE_MESA + " TEXT,"
            + CE_MESERO + " TEXT," + CE_STATUS + " TEXT"
            + ")";
    // CREAR TABLA PVPRODUCTOSMODOSG
    private static final String CREATE_TABLE_PVPRODUCTOSMODOSG = "CREATE TABLE "
            + TABLE_PVPRODUCTOSMODOSG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + GP_PRODUCTO + " TEXT," + GP_GRUPO + " TEXT," + GP_GRUPO_DESC + " TEXT"
            + ")";
    // CREAR TABLA PVMODOS
    private static final String CREATE_TABLE_PVMODOS = "CREATE TABLE "
            + TABLE_PVMODOS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + MD_GRUPO + " TEXT," + MD_MODO + " TEXT," + MD_DESC + " TEXT,"
            + MD_DEFAULT + " TEXT"
            + ")";
    // CREAR TABLA PVMODCG
    private static final String CREATE_TABLE_PVMODCG = "CREATE TABLE "
            + TABLE_PVMODCG + "(" + CG_COMANDA + " INTEGER," + CG_COMANDA_DET + " INTEGER,"
            + CG_PRODUCTO + " TEXT," + CG_GRUPO + " TEXT," + CG_GRUPO_DESC + " TEXT," + CG_MODO + " TEXT," + CG_DESC + " TEXT,"
            + CG_MANDA + " TEXT," + CG_DEFAULT + " TEXT," + CG_SELECCION + " TEXT"
            + ")";
    // CREAR TABLA PVGUAR
    private static final String CREATE_TABLE_PVGUAR = "CREATE TABLE "
            + TABLE_PVGUAR + "(" + GU_COMANDA + " INTEGER," + GU_COMANDA_DET + " INTEGER,"
            + GU_PRODUCTO + " TEXT," + GU_GUAR + " TEXT," + GU_DESC + " TEXT," + GU_DEFAULT + " TEXT," + GU_SELECCION + " TEXT"
            + ")";
    // CREAR TABLA PVMODCG_TMP
    private static final String CREATE_TABLE_TMP_PVMODCG = "CREATE TABLE "
            + TABLE_TMP_PVMODCG + "(" + CG_COMANDA + " INTEGER,"
            + CG_PRODUCTO + " TEXT," + CG_GRUPO + " TEXT," + CG_GRUPO_DESC + " TEXT," + CG_MODO + " TEXT," + CG_DESC + " TEXT,"
            + CG_MANDA + " TEXT," + CG_DEFAULT + " TEXT," + CG_SELECCION + " TEXT"
            + ")";
    // CREAR TABLA PVGUAR_TMP
    private static final String CREATE_TABLE_TMP_PVGUAR = "CREATE TABLE "
            + TABLE_TMP_PVGUAR + "(" + GU_COMANDA + " INTEGER,"
            + GU_PRODUCTO + " TEXT," + GU_GUAR + " TEXT," + GU_DESC + " TEXT," + GU_DEFAULT + " TEXT," + GU_SELECCION + " TEXT"
            + ")";
    // CREAR TABLA PVMODOSG
    private static final String CREATE_TABLE_PVMODOSG = "CREATE TABLE "
            + TABLE_PVMODOSG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + MG_GRUPO + " TEXT," + MG_DESC + " TEXT," + MG_MANDAT + " TEXT"
            + ")";
    // CREAR TABLA PVPRODUCTOSMODI
    private static final String CREATE_TABLE_PVPRODUCTOSMODI = "CREATE TABLE "
            + TABLE_PVPRODUCTOSMODI + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + MP_PRODUCTO + " TEXT," + MP_MODI + " TEXT," + MP_DEFAULT + " TEXT"
            + ")";
    // CREAR TABLA COMANDA
    private static final String CREATE_TABLE_COMANDA = "CREATE TABLE "
            + TABLE_COMANDA + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + CMD_SESION + " INTEGER," + CMD_MESA + " TEXT," + CMD_TRANSA + " TEXT,"
            + CMD_PRID + " TEXT," + CMD_PRDESC + " TEXT," + CMD_CANTIDAD + " INTEGER," + CMD_PRECIO + " FLOAT," + CMD_CARTA + " TEXT,"
            + CMD_COMENSAL + " INTEGER," + CMD_TIEMPO + " INTEGER," + CMD_NOTA + " TEXT,"
            + CMD_STATUS + " TEXT"
            + ")";

    // CREAR TABLA PVCAT1
    private static final String CREATE_TABLE_PVCAT1 = "CREATE TABLE "
            + TABLE_PVCAT1 + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CAT1_MOVI + " TEXT," + KEY_CAT1_FASE + " TEXT,"
            + KEY_CAT1_CARTA + " TEXT," + KEY_CAT1 + " TEXT,"
            + KEY_CAT1_IMAGEN + " TEXT," + KEY_CAT1_DESC + " TEXT,"
            + KEY_CAT1_POS + " INTEGER"
            + ")";

    // CREAR TABLA PVCAT2
    private static final String CREATE_TABLE_PVCAT2 = "CREATE TABLE "
            + TABLE_PVCAT2 + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CAT2_MOVI + " TEXT," + KEY_CAT2_FASE + " TEXT," + KEY_CAT2_CAT1 + " TEXT,"
            + KEY_CAT2_CARTA + " TEXT," + KEY_CAT2 + " TEXT,"
            + KEY_CAT2_IMAGEN + " TEXT," + KEY_CAT2_DESC + " TEXT,"
            + KEY_CAT2_POS + " INTEGER"
            + ")";

    // CREAR TABLA PVMENUS
    private static final String CREATE_TABLE_PVMENUS = "CREATE TABLE "
            + TABLE_PVMENUS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PM_MOVI + " TEXT," + KEY_PM_FASE + " TEXT," + KEY_PM_CAT1 + " TEXT,"
            + KEY_PM_CAT2 + " TEXT," + KEY_PM_PRODUCTO + " TEXT,"
            + KEY_PM_PRODUCTO_DESC + " TEXT," + KEY_PM_POS + " INTEGER,"
            + KEY_PM_PRECIO + " INTEGER," + KEY_PM_IVA + " INTEGER," + KEY_PM_CARTA + " TEXT,"
            + KEY_PM_COMISION + " INTEGER," + KEY_PM_PROPINA + " INTEGER,"
            + KEY_PM_FAMILIA + " TEXT," + KEY_PM_GRUPOPR + " TEXT,"
            + KEY_PM_SUBFAMILIAPR + " TEXT," + KEY_PM_MODI + " TEXT," + KEY_PM_GUAR + " TEXT"
            + ")";

    // CREAR TABLA PVMESA
    private static final String CREATE_TABLE_PVMESA = "CREATE TABLE "
            + TABLE_PVMESA + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_MOVI + " TEXT," + KEY_FASE + " TEXT," + KEY_MESA + " TEXT," + KEY_MESA_NAME + " TEXT," + KEY_MESA_MESERO + " TEXT,"
            + KEY_HABI + " TEXT," + KEY_GUEST + " TEXT,"
            + KEY_PAX + " TEXT"
            + ")";
    // CREAR TABLA CONEXION
    private static final String CREATE_TABLE_CONEXION = "CREATE TABLE "
            + TABLE_CONNECT + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TIPO + " TEXT," + KEY_MOVI_C + " TEXT," + KEY_FASE_C + " TEXT," + KEY_NAME_C + " TEXT," + KEY_UN + " TEXT," + KEY_PW + " TEXT," + KEY_CN + " TEXT,"
            + KEY_IP + " TEXT," + KEY_MODI + " TEXT"
            + ")";
    // CREAR TABLA SESION
    private static final String CREATE_TABLE_SESION = "CREATE TABLE "
            + TABLE_SESION + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + SES_MESERO + " TEXT," + SES_MOVI + " TEXT," + SES_FASE + " TEXT," + SES_STATUS + " TEXT"
            + ")";


    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_PVCAT1);
        db.execSQL(CREATE_TABLE_PVCAT2);
        db.execSQL(CREATE_TABLE_PVMENUS);
        db.execSQL(CREATE_TABLE_PVMESA);
        db.execSQL(CREATE_TABLE_CONEXION);
        db.execSQL(CREATE_TABLE_COMANDA);
        db.execSQL(CREATE_TABLE_SESION);
        db.execSQL(CREATE_TABLE_PVPRODUCTOSMODI);
        db.execSQL(CREATE_TABLE_PVMODOS);
        db.execSQL(CREATE_TABLE_PVMODOSG);
        db.execSQL(CREATE_TABLE_PVMODCG);
        db.execSQL(CREATE_TABLE_PVGUAR);
        db.execSQL(CREATE_TABLE_TMP_PVMODCG);
        db.execSQL(CREATE_TABLE_TMP_PVGUAR);
        db.execSQL(CREATE_TABLE_PRMOD);
        db.execSQL(CREATE_TABLE_COMANDAENC);
        db.execSQL(CREATE_TABLE_PVPRODUCTOSMODOSG);
        db.execSQL(CREATE_TABLE_BRAZA);
        db.execSQL(CREATE_TABLE_PVRVANOMBRE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVCAT1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVCAT2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVMENUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVMESA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMANDA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVPRODUCTOSMODI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVMODOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVMODOSG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVMODCG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVGUAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TMP_PVMODCG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TMP_PVGUAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRMOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMANDAENC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVPRODUCTOSMODOSG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAZA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVRVANOMBRE);


        // create new tables
        onCreate(db);
    }
}
