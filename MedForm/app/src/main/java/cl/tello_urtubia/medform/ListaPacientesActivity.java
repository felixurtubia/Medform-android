package cl.tello_urtubia.medform;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
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

import cl.tello_urtubia.medform.Entidades.Paciente;
import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class ListaPacientesActivity extends AppCompatActivity {

    ListView listViewpacientes;
    ArrayList<Paciente> listaPaciente;
    ArrayList<String> listaInfo;
    ConexionSQLHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_listaPacientes);
        setSupportActionBar(toolbar);


        conn = new ConexionSQLHelper(getApplicationContext(), "bd_pacientes", null, 1);
        listViewpacientes = (ListView) findViewById(R.id.listViewPacientes);

        consultarListaPacientes();

        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,listaInfo);
        listViewpacientes.setAdapter(adaptador);

        listViewpacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                Intent intent = new Intent();

                intent.setClass(getApplicationContext(), DatosPacienteActivity.class);
                intent.putExtra("nombre", listaPaciente.get(pos).getNombre());
                intent.putExtra("rut", listaPaciente.get(pos).getRut());
                intent.putExtra("sexo", listaPaciente.get(pos).getSexo());
                intent.putExtra("fecha", listaPaciente.get(pos).getFecha());
                intent.putExtra("direccion", listaPaciente.get(pos).getDireccion());

                startActivity(intent);

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_pacientes, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private void consultarListaPacientes() {
        SQLiteDatabase db = conn.getReadableDatabase() ;

        Paciente paciente = null;
        listaPaciente = new ArrayList<Paciente>();
        // SELECT * FROM paciente
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
        /* TODO(5) Falta cerrar el cursor y la base de datos :O !!! Va a exlpotar !!!*/
        obtenerLista();

    }

    private void obtenerLista() {

        listaInfo = new ArrayList<String>();

        for (int i =0; i<listaPaciente.size(); i++){
            listaInfo.add(listaPaciente.get(i).getRut()+" - "+ listaPaciente.get(i).getNombre());

        }
    }
}
