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



    // Constantes campos tabla receta
    public static final String TABLA_RECETA= "receta";
    public static final String CAMPO_ID_RECETA = "_id_r";
    public static final String CAMPO_DIAGNOSTICO= "diagnostico";
    public static final String CAMPO_FECHA_ACTUAL= "DateActual";



    public static final String CREAR_TABLA_RECETA = "CREATE TABLE "+TABLA_RECETA+" ("+CAMPO_ID_RECETA+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
            ", "+CAMPO_NOMBRE+" TEXT, "+CAMPO_RUT+" TEXT, "+CAMPO_FECHA+" TEXT, "+CAMPO_SEXO+" TEXT, "+CAMPO_DIRECCION+"" +
            " TEXT, "+CAMPO_DIAGNOSTICO+" TEXT, "+CAMPO_FECHA_ACTUAL+" TEXT ); ";




    // Constantes campos tabla RecetaxMedicamentos
    public static final String TABLA_MEDICAMENTOS= "medicamentos";
    public static final String CAMPO_ID_MEDICAMENTO = "_id_M";
    public static final String CAMPO_MEDICAMENTO= "medicamento";


    public static final String CREAR_TABLA_MEDICAMENTOS = "CREATE TABLE "+TABLA_MEDICAMENTOS+" ("+CAMPO_ID_MEDICAMENTO+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
            ", "+CAMPO_ID_RECETA+" INTEGER, "+CAMPO_MEDICAMENTO+" TEXT); ";
}
