package cl.tello_urtubia.medform;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cl.tello_urtubia.medform.Entidades.Receta;
import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class HistorialRecetasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listViewrecetas;
    ArrayList<Receta> listaReceta;
    ArrayList<String> listaInfo;
    ConexionSQLHelper conn;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_recetas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_historialRecetas);
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

        conn = new ConexionSQLHelper(getApplicationContext(), "bd_recetas", null, 1);
        listViewrecetas = (ListView) findViewById(R.id.listViewRecetas);

        consultarListaRecetas();

        ArrayAdapter adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaInfo);
        listViewrecetas.setAdapter(adaptador);

        listViewrecetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                Intent intent = new Intent();

                intent.setClass(getApplicationContext(), DatosRecetaActivity.class);
                intent.putExtra("nombre", listaReceta.get(pos).getNombre());
                intent.putExtra("rut", listaReceta.get(pos).getRut());
                intent.putExtra("sexo", listaReceta.get(pos).getSexo());
                intent.putExtra("fecha", listaReceta.get(pos).getFecha());
                intent.putExtra("direccion", listaReceta.get(pos).getDireccion());
                intent.putExtra("diagnostico", listaReceta.get(pos).getDiagnostico());
                intent.putExtra("DateActual", listaReceta.get(pos).getDateActual());

                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_pacientes, menu);

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

    private void consultarListaRecetas() {
        SQLiteDatabase db = conn.getReadableDatabase() ;

        Receta receta = null;
        listaReceta = new ArrayList<Receta>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_RECETA, null);

        while (cursor.moveToNext()) {
            receta = new Receta();
            receta.setNombre(cursor.getString(1));
            receta.setRut(cursor.getString(2));
            receta.setFecha(cursor.getString(3));
            receta.setSexo(cursor.getString(4));
            receta.setDireccion(cursor.getString(5));
            receta.setDiagnostico(cursor.getString(6));
            receta.setDateActual(cursor.getString(7));

            listaReceta.add(receta);
        }

        obtenerLista();

        cursor.close();
        db.close();

    }

    private void obtenerLista() {

        listaInfo = new ArrayList<String>();

        for (int i =0; i<listaReceta.size(); i++){
            listaInfo.add(listaReceta.get(i).getRut()+" - "+ listaReceta.get(i).getNombre()+"-"+listaReceta.get(i).getDateActual());

        }
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
