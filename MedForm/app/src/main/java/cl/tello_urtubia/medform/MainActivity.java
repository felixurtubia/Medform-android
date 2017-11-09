package cl.tello_urtubia.medform;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cl.tello_urtubia.medform.Entidades.Paciente;
import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listViewpacientes;
    ArrayList<Paciente> listaPaciente = new ArrayList<>();
    //ArrayList<String> listaInfo;
    ListItemAdapter adaptador;
    ConexionSQLHelper conn;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Action bar y drawer layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nuevo = new Intent();
                nuevo.setClass(getApplicationContext(), CrearPacienteActivity.class);
                startActivity(nuevo);
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_dl, R.string.close_dl);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Conexion SQLite y lista de pacientes
        conn = new ConexionSQLHelper(getApplicationContext(), "bd_pacientes", null, 1);
        listViewpacientes = (ListView) findViewById(R.id.listViewPacientes);

        consultarListaPacientes();

        adaptador  = new ListItemAdapter(this, listaPaciente);
        listViewpacientes.setAdapter(adaptador);
        listViewpacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                Intent intent = new Intent();

                intent.setClass(getApplicationContext(), DatosPacienteActivity.class);
                intent.putExtra("nombre", ((Paciente) adaptador.getItem(pos)).getNombre());
                intent.putExtra("rut", ((Paciente) adaptador.getItem(pos)).getRut());
                intent.putExtra("sexo", ((Paciente) adaptador.getItem(pos)).getSexo());
                intent.putExtra("fecha", ((Paciente) adaptador.getItem(pos)).getFecha());
                intent.putExtra("direccion", ((Paciente) adaptador.getItem(pos)).getDireccion());


                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_pacientes, menu);
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setSubmitButtonEnabled(true);
        //searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptador.getFilter().filter(newText);
                return false;
            }
        });
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

    public void consultarListaPacientes() {
        SQLiteDatabase db = conn.getReadableDatabase() ;
        Paciente paciente = null;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_PACIENTE, null);

        while (cursor.moveToNext()) {
            paciente = new Paciente();
            paciente.setNombre(cursor.getString(1));
            paciente.setRut(cursor.getString(2));
            paciente.setFecha(cursor.getString(3));
            paciente.setSexo(cursor.getString(4));
            paciente.setDireccion(cursor.getString(5));
            listaPaciente.add(paciente);
        }

        //obtenerLista();
        cursor.close();
        db.close();

    }

    /**private void obtenerLista() {

        listaInfo = new ArrayList<String>();

        for (int i =0; i<listaPaciente.size(); i++){
            listaInfo.add(listaPaciente.get(i).getRut()+" - "+ listaPaciente.get(i).getNombre());

        }
    }**/

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
