package cl.tello_urtubia.medform;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

public  class EditarPacienteActivity extends AppCompatActivity{

    Spinner spinner;
    String sexo;
    EditText campoNombre, campoRut, campoFecha, campoDireccion ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*Toast.makeText(getApplicationContext(), "wena", Toast.LENGTH_LONG).show();*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_paciente);


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
}
