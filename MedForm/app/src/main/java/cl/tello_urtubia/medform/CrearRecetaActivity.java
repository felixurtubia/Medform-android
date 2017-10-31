package cl.tello_urtubia.medform;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

import static cl.tello_urtubia.medform.R.id.campoDireccion;
import static cl.tello_urtubia.medform.R.id.campoFecha;
import static cl.tello_urtubia.medform.R.id.campoRut;

public class CrearRecetaActivity extends AppCompatActivity {

    EditText campoMedicamento, campoDiagnostico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crearReceta);
        setSupportActionBar(toolbar);

        campoMedicamento = (EditText) findViewById(R.id.campoMedicamento);
        campoDiagnostico = (EditText) findViewById(R.id.campoDiagnostico); // Falta crear estos Edits View

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crear_receta, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_user_settings:
                break;
            case R.id.action_settings:
                break;
            case R.id.action_print:
                break;
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick (View view) {
        String nombre = getIntent().getStringExtra("nombre");
        String rut = getIntent().getStringExtra("rut");
        String medicamento = campoMedicamento.getText().toString();
        String diagnostico = campoDiagnostico.getText().toString();
        registrarRecetasSQL(nombre, rut, medicamento, diagnostico);
        crearArchivoReceta(nombre,rut,medicamento,diagnostico);

        Intent intent = new Intent(this,MainActivity.class); // Luego de crear la receta, volvemos al inicio
        startActivity(intent);
    }

    public void crearArchivoReceta(String nombre, String rut, String medicamento, String diagnostico) {
    }

    public void registrarRecetasSQL(String nombre, String rut, String medicamento, String diagnostico) {

        RecetaSQLHelper conn = new RecetaSQLHelper(this, "bd_recetas", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        String insert = "INSERT INTO "+ Utilidades.TABLA_RECETA+" ( "+Utilidades.CAMPO_NOMBRE+","
                +Utilidades.CAMPO_RUT+","+Utilidades.CAMPO_MEDICAMENTO+","+Utilidades.CAMPO_DIAGNOSTICO+")"
                + "VALUES ( '"+nombre+"', '"+rut+"', '" +medicamento+"', '"+diagnostico+"' )" ;

        db.execSQL(insert);

        Toast.makeText(getApplicationContext(), "Receta Lista, Imprimiendo...", Toast.LENGTH_SHORT).show();

        db.close(); // MIRA FELIX CULIAO PA QUE VEIA QUE CIERRO LAS CONEXIONES A LAS BASES DE DATOS
    }

}
