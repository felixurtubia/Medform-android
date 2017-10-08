package cl.tello_urtubia.medform;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static android.provider.BaseColumns._ID;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class CrearPacienteActivity extends AppCompatActivity {



    EditText campoNombre, campoRut, campoFecha, campoDireccion ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paciente);
        /* TODO (4) Cambiar las action bar de cada activity para que quede bien*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crearPaciente);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Boolean encontrado = intent.getBooleanExtra("encontrado", true);

        if (!encontrado) {
            /*TextView encontrado_msg = (TextView) findViewById(R.id.crearPaciente_noEncontrado);
            encontrado_msg.setVisibility(View.VISIBLE);*/
            Toast.makeText(this, "Paciente no encontrado.\n Crea uno nuevo", Toast.LENGTH_SHORT);
        }

        Spinner spinner = (Spinner) findViewById(R.id.crearPaciente_spinnerSexo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.sexo_paciente, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        campoNombre = (EditText) findViewById(R.id.campoNombre);
        campoRut = (EditText) findViewById(R.id.campoRut);
        campoFecha = (EditText) findViewById(R.id.campoFecha);
        campoDireccion = (EditText) findViewById(R.id.campoDireccion);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_print) {
            return true;
        }
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick (View view) {
        //registrarPacientes();
        registrarPacientesSQL();
    }

    private void registrarPacientesSQL() {
        ConexionSQLHelper conn = new ConexionSQLHelper(this, "bd_pacientes", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        String insert = "INSERT INTO "+Utilidades.TABLA_PACIENTE+" ( "+Utilidades.CAMPO_NOMBRE+","
                +Utilidades.CAMPO_RUT+","+Utilidades.CAMPO_FECHA+","+Utilidades.CAMPO_DIRECCION+")"
                + "VALUES ( '"+campoNombre.getText().toString()+"', '"+campoRut.getText().toString()+"', '"
                +campoFecha.getText().toString()+"', '"+campoDireccion.getText().toString()+"')" ;

        db.execSQL(insert);

        db.close();
    }

    private void registrarPacientes(){
        ConexionSQLHelper conn = new ConexionSQLHelper(this, "bd_pacientes", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE, campoNombre.getText().toString());
        values.put(Utilidades.CAMPO_RUT, campoRut.getText().toString());
        values.put(Utilidades.CAMPO_FECHA, campoFecha.getText().toString());
        values.put(Utilidades.CAMPO_DIRECCION, campoDireccion.getText().toString());

        Long idResultante = db.insert(Utilidades.TABLA_PACIENTE, Utilidades.CAMPO_ID, values);

        Toast.makeText(getApplicationContext(), "Paciente Registrado: "+idResultante, Toast.LENGTH_SHORT).show();

        db.close();

    }


}
