package cl.tello_urtubia.medform;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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

import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class DatosPacienteActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_datospaciente);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_dl, R.string.close_dl);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        String nombre = getIntent().getStringExtra("nombre");
        String rut = getIntent().getStringExtra("rut");
        String sexo = getIntent().getStringExtra("sexo");
        String fecha = getIntent().getStringExtra("fecha");
        String direccion = getIntent().getStringExtra("direccion");

        TextView rut_tv = (TextView) findViewById(R.id.rutPaciente_tv);
        TextView nombre_tv = (TextView) findViewById(R.id.nombrePaciente_tv);
        TextView sexo_tv = (TextView) findViewById(R.id.sexoPaciente_tv);
        TextView edad_tv = (TextView) findViewById(R.id.edadPaciente_tv);
        TextView direccion_tv = (TextView) findViewById(R.id.direccionPaciente_tv);
        rut_tv.setText(rut);
        nombre_tv.setText(nombre);
        sexo_tv.setText(sexo);
        edad_tv.setText(fecha);
        direccion_tv.setText(direccion);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_datos_paciente, menu);
        ActionBar actionBar = getSupportActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        switch (id) {
            case R.id.action_editar_paciente:
                editarPaciente();
                break;
            case R.id.action_eliminar_paciente:
                eliminarPaciente();
                break;
            case R.id.action_nueva_receta:
                vistaCrearReceta();
            case R.id.action_user_settings:
                break;
            case R.id.action_settings:
                break;
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            default:
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void eliminarPaciente() {

        // Luego de eliminar al paciente, se envia un toast y se envia al mainActivity

        String rut = getIntent().getStringExtra("rut");

        ConexionSQLHelper conn = new ConexionSQLHelper(this, "bd_pacientes", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        String delete = "DELETE FROM "+Utilidades.TABLA_PACIENTE+" WHERE "+Utilidades.CAMPO_RUT+"= '"+rut+"' ;" ;

        db.execSQL(delete);

        Toast.makeText(getApplicationContext(), "Paciente Eliminado, Volviendo al inicio", Toast.LENGTH_SHORT).show();

        db.close();

        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }

    public void vistaCrearReceta(){
        String rut = getIntent().getStringExtra("rut");
        String nombre = getIntent().getStringExtra("nombre");
        String sexo = getIntent().getStringExtra("sexo");
        String fecha = getIntent().getStringExtra("fecha");
        String direccion = getIntent().getStringExtra("direccion");

        Intent intent_vistaReceta = new Intent(this, CrearRecetaActivity.class);
        intent_vistaReceta.putExtra("rut", rut);
        intent_vistaReceta.putExtra("nombre", nombre);
        intent_vistaReceta.putExtra("sexo",sexo);
        intent_vistaReceta.putExtra("fecha", fecha);
        intent_vistaReceta.putExtra("direccion",direccion);
        startActivity(intent_vistaReceta);

    }

    public void editarPaciente() {

        String rut = getIntent().getStringExtra("rut");
        String nombre = getIntent().getStringExtra("nombre");
        String sexo = getIntent().getStringExtra("sexo");
        String fecha = getIntent().getStringExtra("fecha");
        String direccion = getIntent().getStringExtra("direccion");


        Intent intent = new Intent();
        intent.setClass(this, EditarPacienteActivity.class);
        intent.putExtra("rut", rut);
        intent.putExtra("nombre", nombre);
        intent.putExtra("sexo", sexo);
        intent.putExtra("fecha", fecha);
        intent.putExtra("direccion", direccion);



        startActivity(intent);

    }
}
