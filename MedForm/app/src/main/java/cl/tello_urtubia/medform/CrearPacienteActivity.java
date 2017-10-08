package cl.tello_urtubia.medform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CrearPacienteActivity extends AppCompatActivity {

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

    public void agregarPaciente(View view) {
        /* TODO(3) recolectar los datos de la vista y agregar nuevo paciente*/
    }
}
