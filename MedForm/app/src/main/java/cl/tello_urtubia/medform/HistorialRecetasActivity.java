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

import cl.tello_urtubia.medform.Entidades.Receta;
import cl.tello_urtubia.medform.Utilidades.Utilidades;

public class HistorialRecetasActivity extends AppCompatActivity {

    ListView listViewrecetas;
    ArrayList<Receta> listaReceta;
    ArrayList<String> listaInfo;
    ConexionSQLHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_recetas);

        conn = new ConexionSQLHelper(getApplicationContext(), "bd_recetas", null, 1);
        listViewrecetas = (ListView) findViewById(R.id.listViewRecetas);

        consultarListaRecetas();

        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,listaInfo);
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
}
