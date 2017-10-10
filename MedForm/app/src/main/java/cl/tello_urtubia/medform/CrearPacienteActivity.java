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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class CrearPacienteActivity extends AppCompatActivity {

    EditText campoNombre, campoRut, campoFecha, campoDireccion ;
    String sexo;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crearPaciente);
        setSupportActionBar(toolbar);


        String rut = getIntent().getStringExtra("rut");
        campoRut = (EditText) findViewById(R.id.campoRut);
        campoRut.setText(rut);
        spinner = (Spinner) findViewById(R.id.crearPaciente_spinnerSexo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.sexo_paciente, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                sexo = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        campoNombre = (EditText) findViewById(R.id.campoNombre);
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
        registrarPacientesSQL();
    }

    private void registrarPacientesSQL() {
        ConexionSQLHelper conn = new ConexionSQLHelper(this, "bd_pacientes", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        String insert = "INSERT INTO "+Utilidades.TABLA_PACIENTE+" ( "+Utilidades.CAMPO_NOMBRE+","
                +Utilidades.CAMPO_RUT+","+Utilidades.CAMPO_FECHA+","+Utilidades.CAMPO_SEXO+","+Utilidades.CAMPO_DIRECCION+")"
                + "VALUES ( '"+campoNombre.getText().toString()+"', '"+campoRut.getText().toString()+"', '"
                +campoFecha.getText().toString()+"', '"+sexo+"', '"+campoDireccion.getText().toString()+"')" ;

        db.execSQL(insert);

        Toast.makeText(getApplicationContext(), "Paciente Registrado", Toast.LENGTH_SHORT).show();

        db.close();
    }



}
