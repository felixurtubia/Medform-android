package cl.tello_urtubia.medform;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    ListView listViewpacientes;
    ArrayList<Paciente> listaPaciente;
    ArrayList<String> listaInfo;
    ConexionSQLHelper conn;
    EditText campoRut;
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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_dl, R.string.close_dl);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();





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

    private void consultarListaPacientes() {
        SQLiteDatabase db = conn.getReadableDatabase() ;

        Paciente paciente = null;
        listaPaciente = new ArrayList<Paciente>();
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

        obtenerLista();

        cursor.close();
        db.close();

    }

    private void obtenerLista() {

        listaInfo = new ArrayList<String>();

        for (int i =0; i<listaPaciente.size(); i++){
            listaInfo.add(listaPaciente.get(i).getRut()+" - "+ listaPaciente.get(i).getNombre());

        }
    }

}
