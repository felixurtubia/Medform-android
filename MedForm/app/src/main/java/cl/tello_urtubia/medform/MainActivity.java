package cl.tello_urtubia.medform;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class MainActivity extends AppCompatActivity {

    EditText campoRut, campoNombre;
    ConexionSQLHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        conn = new ConexionSQLHelper(getApplicationContext(), "bd_pacientes", null, 1);

        campoRut = (EditText) findViewById(R.id.main_RutPaciente_et);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inicioOptionsSelected(View view) {
        Button boton = (Button) view;
        int id = boton.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.buscar_paciente_button:
                consultar();
                /** Boolean encontrado = false;
                EditText rut = (EditText) findViewById(R.id.main_RutPaciente_et);
                String rut_string = rut.getText().toString();
                if(encontrado){
                    intent.setClass(this, DatosPacienteActivity.class);
                    intent.putExtra("rut", rut_string);

                }
                else {
                    intent.setClass(this, CrearPacienteActivity.class);
                    intent.putExtra("rut", rut_string);
                    intent.putExtra("encontrado", false);

                }
                startActivity(intent);
                break;
                 */
            case R.id.historial_recetas_button:
                intent.setClass(this, HistorialRecetasActivity.class);
                startActivity(intent);
                break;

            case R.id.lista_pacientes_button:
                intent.setClass(this, ListaPacientesActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }


    }

    private void consultar() {

        Intent intent = new Intent();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros= {campoRut.getText().toString()};

        String[] campos = {Utilidades.CAMPO_NOMBRE, Utilidades.CAMPO_RUT, Utilidades.CAMPO_SEXO, Utilidades.CAMPO_FECHA, Utilidades.CAMPO_DIRECCION};

        try {
            Cursor cursor = db.query(Utilidades.TABLA_PACIENTE,campos, Utilidades.CAMPO_RUT+"=?", parametros , null, null, null);
            cursor.moveToFirst();
            DatosPacienteActivity datos = new DatosPacienteActivity();
            datos.setPaciente(cursor.getString(0) , cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
            intent.setClass(this, DatosPacienteActivity.class);

        }catch(Exception e){
            intent.setClass(this, CrearPacienteActivity.class);


        }



    }


}
