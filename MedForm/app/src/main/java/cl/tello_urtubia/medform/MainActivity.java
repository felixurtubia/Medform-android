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
import android.widget.Toast;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class MainActivity extends AppCompatActivity {

    EditText campoRut;
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
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
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
                break;

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

        String[] campos = {Utilidades.CAMPO_NOMBRE, Utilidades.CAMPO_RUT, Utilidades.CAMPO_SEXO, Utilidades.CAMPO_DIRECCION };

        try {
            //Cursor cursor = db.query(Utilidades.TABLA_PACIENTE,campos, Utilidades.CAMPO_RUT+"=?", parametros , null, null, null);
            Cursor cursor = db.rawQuery("SELECT nombre,rut,sexo,fecnac,direccion FROM paciente WHERE rut =?", parametros);
            cursor.moveToFirst();
            intent.setClass(this, DatosPacienteActivity.class);
            intent.putExtra("nombre", cursor.getString(0)+"");
            intent.putExtra("rut", cursor.getString(1)+"");
            intent.putExtra("sexo", cursor.getString(2)+"");
            intent.putExtra("fecha", cursor.getString(3)+"");
            intent.putExtra("direccion", cursor.getShort(4)+"");
            cursor.close();
            Toast.makeText(getApplicationContext(), "PacienteEncontrado", Toast.LENGTH_LONG).show();
            startActivity(intent); // Si existe el paciente, pasamos a la vista de mostrar los datos del paciente

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Paciente no encontrado", Toast.LENGTH_LONG).show();
            intent.setClass(this, CrearPacienteActivity.class);
            intent.putExtra("rut", campoRut.getText().toString());

            startActivity(intent); // Si falla al buscar paciente, vamos a crear uno con el rut que se puso


        }



    }


}
