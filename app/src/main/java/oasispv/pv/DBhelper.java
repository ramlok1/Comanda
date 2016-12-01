package oasispv.pv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "comandero";

    // Table Names
    public static final String TABLE_PVCAT1 = "PVCAT1";
    public static final String TABLE_PVCAT2 = "PVCAT2";
    public static final String TABLE_PVMENUS = "PVMENUS";
    public static final String TABLE_PVMESA = "PVMESA";
    public static final String TABLE_CONNECT = "CONEXION";
    public static final String TABLE_COMANDA = "COMANDA";
    public static final String TABLE_SESION = "SESION";

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
    public static final String KEY_PM_CARTA = "PM_CARTA";
    public static final String KEY_PM_COMISION = "PM_COMISION";
    public static final String KEY_PM_PROPINA = "PM_PROPINA";
    public static final String KEY_PM_FAMILIA = "PM_FAMILIA";
    public static final String KEY_PM_GRUPOPR = "PM_GRUPOPR";
    public static final String KEY_PM_SUBFAMILIAPR = "PM_SUBFAMILIAPR";

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

    // TABLA COMANDA
    public static final String CMD_SESION = "SESION";
    public static final String CMD_MESA = "MESA";
    public static final String CMD_PRID = "PRID";
    public static final String CMD_PRDESC = "PRDESC";
    public static final String CMD_CANTIDAD = "CANTIDAD";
    public static final String CMD_COMENSAL = "COMENSAL";
    public static final String CMD_TIEMPO = "TIEMPO";
    public static final String CMD_STATUS = "STATUS";

    // TABLA SESION
    public static final String SES_MESERO = "MESERO";
    public static final String SES_MOVI = "MOVI";
    public static final String SES_FASE = "FASE";
    public static final String SES_STATUS = "STATUS";




    // Table Create Statements
    // CREAR TABLA COMANDA
    private static final String CREATE_TABLE_COMANDA = "CREATE TABLE "
            + TABLE_COMANDA + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + CMD_SESION + " INTEGER,"+ CMD_MESA + " TEXT,"
            + CMD_PRID + " TEXT,"+ CMD_PRDESC + " TEXT,"+ CMD_CANTIDAD + " INTEGER,"
            + CMD_COMENSAL + " INTEGER,"+ CMD_TIEMPO + " INTEGER,"
            + CMD_STATUS + " TEXT"
            + ")";

    // CREAR TABLA PVCAT1
    private static final String CREATE_TABLE_PVCAT1 = "CREATE TABLE "
            + TABLE_PVCAT1 + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CAT1_MOVI + " TEXT,"+ KEY_CAT1_FASE + " TEXT,"
            + KEY_CAT1_CARTA + " TEXT,"+ KEY_CAT1 + " TEXT,"
            + KEY_CAT1_IMAGEN + " TEXT,"+ KEY_CAT1_DESC + " TEXT,"
            + KEY_CAT1_POS + " INTEGER"
            + ")";

    // CREAR TABLA PVCAT2
    private static final String CREATE_TABLE_PVCAT2 = "CREATE TABLE "
            + TABLE_PVCAT2 + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CAT2_MOVI + " TEXT,"+ KEY_CAT2_FASE + " TEXT,"+KEY_CAT2_CAT1+" TEXT,"
            + KEY_CAT2_CARTA + " TEXT,"+ KEY_CAT2 + " TEXT,"
            + KEY_CAT2_IMAGEN + " TEXT,"+ KEY_CAT2_DESC + " TEXT,"
            + KEY_CAT2_POS + " INTEGER"
            + ")";

    // CREAR TABLA PVMENUS
    private static final String CREATE_TABLE_PVMENUS = "CREATE TABLE "
            + TABLE_PVMENUS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PM_MOVI + " TEXT,"+ KEY_PM_FASE + " TEXT,"+KEY_PM_CAT1+" TEXT,"
            + KEY_PM_CAT2 + " TEXT,"+ KEY_PM_PRODUCTO + " TEXT,"
            + KEY_PM_PRODUCTO_DESC + " TEXT,"+ KEY_PM_POS + " INTEGER,"
            + KEY_PM_PRECIO + " INTEGER,"+ KEY_PM_CARTA + " TEXT,"
            + KEY_PM_COMISION + " INTEGER,"+ KEY_PM_PROPINA + " INTEGER,"
            + KEY_PM_FAMILIA + " TEXT,"+ KEY_PM_GRUPOPR + " TEXT,"
            + KEY_PM_SUBFAMILIAPR + " TEXT"
            + ")";

    // CREAR TABLA PVMESA
    private static final String CREATE_TABLE_PVMESA = "CREATE TABLE "
            + TABLE_PVMESA + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_MOVI + " TEXT,"+ KEY_FASE + " TEXT,"+KEY_MESA+" TEXT,"+KEY_MESA_NAME+" TEXT,"+KEY_MESA_MESERO+" TEXT,"
            + KEY_HABI + " TEXT,"+ KEY_GUEST + " TEXT,"
            + KEY_PAX + " TEXT"
            + ")";
    // CREAR TABLA CONEXION
    private static final String CREATE_TABLE_CONEXION = "CREATE TABLE "
            + TABLE_CONNECT + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TIPO + " TEXT,"+ KEY_MOVI_C + " TEXT,"+KEY_FASE_C+" TEXT,"+ KEY_NAME_C + " TEXT,"+KEY_UN+" TEXT,"+KEY_PW+" TEXT,"+KEY_CN+" TEXT,"
            + KEY_IP + " TEXT"
            + ")";
    // CREAR TABLA SESION
    private static final String CREATE_TABLE_SESION = "CREATE TABLE "
            + TABLE_SESION + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + SES_MESERO + " TEXT,"+ SES_MOVI + " TEXT,"+SES_FASE+" TEXT,"+SES_STATUS+" TEXT"
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



        // create new tables
        onCreate(db);
    }}
