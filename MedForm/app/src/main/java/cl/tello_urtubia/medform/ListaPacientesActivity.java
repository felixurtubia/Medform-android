package cl.tello_urtubia.medform;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

                startActivity(intent);

            }
        });


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
        
        obtenerLista();

    }

    private void obtenerLista() {

        listaInfo = new ArrayList<String>();

        for (int i =0; i<listaPaciente.size(); i++){
            listaInfo.add(listaPaciente.get(i).getRut()+" - "+ listaPaciente.get(i).getNombre());

        }
    }
}
