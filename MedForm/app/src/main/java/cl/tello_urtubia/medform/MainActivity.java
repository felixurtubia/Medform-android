package cl.tello_urtubia.medform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                /* TODO(1) buscar paciente en la base de datos, cargar activity dependiendo de si se encuentra*/
                Boolean encontrado = true;
                EditText rut = (EditText) findViewById(R.id.main_RutPaciente_et);
                String rut_string = rut.getText().toString();
                if(encontrado){
                    intent.setClass(this, DatosPacienteActivity.class);
                    intent.putExtra("rut", rut_string);

                }
                else {
                    intent.setClass(this, CrearPacienteActivity.class);
                    intent.putExtra("rut", rut_string);

                }
                startActivity(intent);
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


}
