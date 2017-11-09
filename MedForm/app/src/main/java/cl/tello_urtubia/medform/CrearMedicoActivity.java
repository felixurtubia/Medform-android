package cl.tello_urtubia.medform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class CrearMedicoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText campoNombre, campoRut, campoTitulo, campoDireccion ;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_medico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crearMedico);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_dl, R.string.close_dl);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        campoNombre = (EditText) findViewById(R.id.campoNombre);
        campoTitulo = (EditText) findViewById(R.id.campoTitulo);
        campoDireccion = (EditText) findViewById(R.id.campoDireccion);
        campoRut = (EditText) findViewById(R.id.campoRut);
        Toast toast = Toast.makeText(getApplicationContext(), "Por favor ingresa tus datos para continuar con la aplicación", Toast.LENGTH_LONG);
        toast.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
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
            case R.id.action_user_settings:
                break;
            case R.id.action_settings:
                break;
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick (View view) {
        String nombre = campoNombre.getText().toString();
        String rut = campoRut.getText().toString();
        String titulo = campoTitulo.getText().toString();
        String direccion = campoDireccion.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preferencias_medico),MODE_PRIVATE).edit();
        editor.putString(getString(R.string.nombre_medico), nombre);
        editor.putString(getString(R.string.rut_medico), rut);
        editor.putString(getString(R.string.titulo_medico), titulo);
        editor.putString(getString(R.string.direccion_medico), direccion);
        editor.commit();
        //SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_medico),MODE_PRIVATE);
        //Log.d("OPCIONESSSSS",sharedPref.getAll().toString());
        Toast toast = Toast.makeText(getApplicationContext(), "Sus datos han sido guardados", Toast.LENGTH_LONG);
        toast.show();
        Intent al_inicio = new Intent(this, MainActivity.class);
        startActivity(al_inicio);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_lista_pacientes) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_historial_recetas) {
            intent = new Intent(this, HistorialRecetasActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_datos_medico) {
            /**intent = new Intent(this, DatosMedico.class);
             startActivity(intent);**/

        } else if (id == R.id.nav_ajustes) {
            /**intent = new intent(this, Ajustes.class);
             startActivity(intent);**/

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
