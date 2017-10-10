package cl.tello_urtubia.medform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DatosPacienteActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toast.makeText(getApplicationContext(), "Paciente", Toast.LENGTH_LONG).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_datospaciente);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String nombre = getIntent().getStringExtra("nombre");
        String rut = getIntent().getStringExtra("rut");
        String sexo = getIntent().getStringExtra("sexo");
        String fecha = getIntent().getStringExtra("fecha");

        TextView rut_tv = (TextView) findViewById(R.id.rutPaciente_tv);
        TextView nombre_tv = (TextView) findViewById(R.id.nombrePaciente_tv);
        TextView sexo_tv = (TextView) findViewById(R.id.sexoPaciente_tv);
        TextView edad_tv = (TextView) findViewById(R.id.edadPaciente_tv);
        rut_tv.setText(rut);
        nombre_tv.setText(nombre);
        sexo_tv.setText(sexo);
        edad_tv.setText(fecha);

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

    public void OptionsSelected(View view) {

        Button boton = (Button) view;
        int id = boton.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.editarPacientebutton:
                editarPaciente();
                break;

            case R.id.crearReceta_button:
                vistaCrearReceta();
                break;

            default:
                break;
        }


    }

    public void vistaCrearReceta(){
        Intent intent_vistaReceta = new Intent(this, CrearRecetaActivity.class);

        startActivity(intent_vistaReceta);

    }

    public void editarPaciente() {

        String rut = getIntent().getStringExtra("rut");

        Intent intent = new Intent();
        intent.setClass(this, EditarPacienteActivity.class);
        intent.putExtra("rut", rut);

        startActivity(intent);

    }
}
