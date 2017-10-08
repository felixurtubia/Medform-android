package cl.tello_urtubia.medform.Utilidades;

import static android.provider.BaseColumns._ID;

public class Utilidades {

    // Constantes campos tabla paciente
    public static final String TABLA_PACIENTE= "paciente";
    public static final String CAMPO_ID = _ID;
    public static final String CAMPO_NOMBRE= "nombre";
    public static final String CAMPO_RUT= "rut";
    public static final String CAMPO_FECHA= "fecnac";
    public static final String CAMPO_SEXO= "sexo";
    public static final String CAMPO_DIRECCION= "direccion";

    public static final String CREAR_TABLA_PACIENTE = "CREATE TABLE "+TABLA_PACIENTE+" ("+CAMPO_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
            ", "+CAMPO_NOMBRE+" TEXT, "+CAMPO_RUT+" TEXT, "+CAMPO_FECHA+" TEXT, "+CAMPO_SEXO+" TEXT,"+CAMPO_DIRECCION+" TEXT ); ";
}
