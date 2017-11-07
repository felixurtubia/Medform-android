package cl.tello_urtubia.medform;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cl.tello_urtubia.medform.Utilidades.Utilidades;


public class MedicamentoSQLHelper extends SQLiteOpenHelper {

    public MedicamentoSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_MEDICAMENTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_MEDICAMENTOS+" );");
        onCreate(db);

    }
}
