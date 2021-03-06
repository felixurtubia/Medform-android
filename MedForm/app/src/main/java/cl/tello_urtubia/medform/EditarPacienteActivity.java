package cl.tello_urtubia.medform;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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

public  class EditarPacienteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Spinner spinner;
    String sexo;
    EditText campoNombre, campoRut, campoFecha, campoDireccion ;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*Toast.makeText(getApplicationContext(), "wena", Toast.LENGTH_LONG).show();*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_paciente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_editarPaciente);
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
        /*campoRut = (EditText) findViewById(R.id.campoRut);*/

        Intent intent = getIntent();
        campoNombre.setText(intent.getStringExtra("nombre"));
        campoDireccion.setText(intent.getStringExtra("direccion"));
        campoFecha.setText(intent.getStringExtra("fecha"));
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
            default:
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void onClick (View view) {
        editarPacientesSQL();
    }

    private void editarPacientesSQL() {

        /*String rut = campoRut.getText().toString();*/
        String rut = getIntent().getStringExtra("rut");
        String nombre = campoNombre.getText().toString();
        String fecha = campoFecha.getText().toString();
        String direccion = campoDireccion.getText().toString();


        ConexionSQLHelper conn = new ConexionSQLHelper(this, "bd_pacientes", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        // UPDATE tablename SET column1 = value1, col2 = val2 WHERE condition

        String update = "UPDATE "+ Utilidades.TABLA_PACIENTE+" SET "+Utilidades.CAMPO_NOMBRE+" = '"+nombre+"' , "
                +Utilidades.CAMPO_FECHA+" = '"+fecha+"' ,"+Utilidades.CAMPO_SEXO+" = '"+sexo+"', "+Utilidades.CAMPO_DIRECCION
                +" = '"+direccion+"' WHERE "+Utilidades.CAMPO_RUT+" = '"+rut+"' ;";

        db.execSQL(update);

        Toast.makeText(getApplicationContext(), "Paciente Editado", Toast.LENGTH_SHORT).show();

        db.close();

        Intent inte = new Intent();

        inte.setClass(this, DatosPacienteActivity.class);

        inte.putExtra("rut", rut);
        inte.putExtra("nombre", nombre);
        inte.putExtra("sexo", sexo);
        inte.putExtra("fecha", fecha);
        inte.putExtra("direccion", direccion);
        startActivity(inte); // Lo redireccionamos a los datos de paciente :D
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
