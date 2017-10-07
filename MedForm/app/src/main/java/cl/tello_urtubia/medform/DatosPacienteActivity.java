package cl.tello_urtubia.medform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DatosPacienteActivity extends AppCompatActivity {
    static String DATOSPACIENTE = "Datos Paciente";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_datospaciente);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String rut = intent.getStringExtra("rut");
        TextView rut_tv = (TextView) findViewById(R.id.rutPaciente_tv);
        rut_tv.setText(rut);

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
}
