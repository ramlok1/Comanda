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
    private static final String TABLE_PVCAT1 = "PVCAT1";
    private static final String TABLE_PVCAT2 = "PVCAT2";
    private static final String TABLE_PVMENUS = "PVMENUS";

    // Common column names
    private static final String KEY_ID = "id";

    // TABLA PVCAT1
    private static final String KEY_CAT1_MOVI = "CAT1_MOVI";
    private static final String KEY_CAT1_FASE = "CAT1_FASE";
    private static final String KEY_CAT1_CARTA = "CAT1_CARTA";
    private static final String KEY_CAT1 = "CAT1";
    private static final String KEY_CAT1_IMAGEN = "CAT1_IMAGEN";
    private static final String KEY_CAT1_DESC = "CAT1_DESC";
    private static final String KEY_CAT1_POS = "CAT1_POS";

    // TABLA PVCAT2
    private static final String KEY_CAT2_MOVI = "CAT2_MOVI";
    private static final String KEY_CAT2_FASE = "CAT2_FASE";
    private static final String KEY_CAT2_CARTA = "CAT2_CARTA";
    private static final String KEY_CAT2__CAT1 = "CAT2_CAT1";
    private static final String KEY_CAT2 = "CAT2";
    private static final String KEY_CAT2_IMAGEN = "CAT2_IMAGEN";
    private static final String KEY_CAT2_DESC = "CAT2_DESC";
    private static final String KEY_CAT2_POS = "CAT2_POS";



    // Table Create Statements
    // CREAR TABLA PVCAT1
    private static final String CREATE_TABLE_PVCAT1 = "CREATE TABLE "
            + TABLE_PVCAT1 + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CAT1_MOVI + " TEXT,"+ KEY_CAT1_FASE + " TEXT,"
            + KEY_CAT1_CARTA + " TEXT,"+ KEY_CAT1 + " TEXT,"
            + KEY_CAT1_IMAGEN + " TEXT,"+ KEY_CAT1_DESC + " TEXT,"
            + KEY_CAT1_POS + " INTEGER,"
            + ")";

    // CREAR TABLA PVCAT2
    private static final String CREATE_TABLE_PVCAT2 = "CREATE TABLE "
            + TABLE_PVCAT2 + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CAT2_MOVI + " TEXT,"+ KEY_CAT2_FASE + " TEXT,"+KEY_CAT2__CAT1+" TEXT,"
            + KEY_CAT2_CARTA + " TEXT,"+ KEY_CAT2 + " TEXT,"
            + KEY_CAT2_IMAGEN + " TEXT,"+ KEY_CAT2_DESC + " TEXT,"
            + KEY_CAT2_POS + " INTEGER,"
            + ")";



    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_PVCAT1);
        db.execSQL(CREATE_TABLE_PVCAT2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVCAT1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PVCAT2);


        // create new tables
        onCreate(db);
    }}
