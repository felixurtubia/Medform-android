package cl.tello_urtubia.medform;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

import static cl.tello_urtubia.medform.Utilidades.Utilidades.CAMPO_FECHA_ACTUAL;
import static cl.tello_urtubia.medform.Utilidades.Utilidades.CAMPO_ID_RECETA;
import static cl.tello_urtubia.medform.Utilidades.Utilidades.TABLA_MEDICAMENTOS;
import static cl.tello_urtubia.medform.Utilidades.Utilidades.TABLA_RECETA;

public class DatosRecetaActivity extends AppCompatActivity {

    String rut;
    String nombre;
    String sexo;
    String fecha ;
    String direccion ;
    String diagnostico ;
    String DateActual ;
    ListView listViewMedicamentos;
    ArrayList<String> medicamentos = new ArrayList<String>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        rut = getIntent().getStringExtra("rut");
        nombre = getIntent().getStringExtra("nombre");
        sexo = getIntent().getStringExtra("sexo");
        fecha = getIntent().getStringExtra("fecha");
        direccion = getIntent().getStringExtra("direccion");
        diagnostico = getIntent().getStringExtra("diagnostico");
        DateActual = getIntent().getStringExtra("DateActual");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_receta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_datosReceta);
        setSupportActionBar(toolbar);

        obtenerMedicamentos();


        TextView rut_tv = (TextView) findViewById(R.id.rutPaciente_tv);
        TextView nombre_tv = (TextView) findViewById(R.id.nombrePaciente_tv);
        TextView diagnostico_tv = (TextView) findViewById(R.id.diagnostico_receta);
        TextView DateActual_tv = (TextView) findViewById(R.id.datehourcreated_receta);

        rut_tv.setText(rut);
        nombre_tv.setText(nombre);
        diagnostico_tv.setText(diagnostico);
        DateActual_tv.setText(DateActual);

        listViewMedicamentos = (ListView) findViewById(R.id.listViewMedicamentos);
        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,medicamentos);
        listViewMedicamentos.setAdapter(adaptador);


    }

    public void obtenerMedicamentos() {
        MedicamentoSQLHelper monn = new MedicamentoSQLHelper(this, "bd_medicamentos", null,1 );
        RecetaSQLHelper ronn = new RecetaSQLHelper(this, "bd_recetas", null, 1);
        SQLiteDatabase db = monn.getReadableDatabase() ;
        SQLiteDatabase rb = ronn.getReadableDatabase();

        Cursor cursor1 = rb.rawQuery("SELECT * FROM "+TABLA_RECETA+" WHERE "+CAMPO_FECHA_ACTUAL+"= '"+DateActual+"'", null);
        while (cursor1.moveToNext()){

            Cursor cursor2 = db.rawQuery("SELECT * FROM "+TABLA_MEDICAMENTOS+" WHERE "+CAMPO_ID_RECETA+"= "+cursor1.getInt(0)+"",null);
            while(cursor2.moveToNext()){
                String med = cursor2.getString(2);
                medicamentos.add(med);
            }

            cursor2.close();

        }

        db.close();
        rb.close();
        cursor1.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_datos_receta, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_eliminar_receta:
                eliminarReceta();
                break;
            case R.id.action_generar_receta:
                PrintManager printManager = (PrintManager) this
                        .getSystemService(Context.PRINT_SERVICE);

                String jobName = this.getString(R.string.app_name) +
                        " Document";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    printManager.print(jobName, new DatosRecetaActivity.MyPrintDocumentAdapter(this),
                            null);
                }
                break;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
        Context context;
        private int pageHeight;
        private int pageWidth;
        public PdfDocument myPdfDocument;
        public int totalpages = 1;

        public MyPrintDocumentAdapter(Context context){
            this.context = context;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle metadata) {

            myPdfDocument = new PrintedPdfDocument(context, newAttributes);

            pageHeight = newAttributes.getMediaSize().getHeightMils()/1000 * 72;
            pageWidth = newAttributes.getMediaSize().getWidthMils()/1000 * 72;

            if (cancellationSignal.isCanceled() ) {
                callback.onLayoutCancelled();
                return;
            }

            if (totalpages > 0) {
                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                        .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalpages);

                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onWrite(final PageRange[] pageRanges,
                            final ParcelFileDescriptor destination,
                            final CancellationSignal cancellationSignal,
                            final WriteResultCallback callback) {

            for (int i = 0; i < totalpages; i++) {
                if (pageInRange(pageRanges, i))
                {
                    PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                            pageHeight, i).create();

                    PdfDocument.Page page =
                            myPdfDocument.startPage(newPage);

                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                        myPdfDocument.close();
                        myPdfDocument = null;
                        return;
                    }
                    drawPage(page, i);
                    myPdfDocument.finishPage(page);
                }
            }

            try {
                myPdfDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                myPdfDocument.close();
                myPdfDocument = null;
            }

            callback.onWriteFinished(pageRanges);
        }

        private boolean pageInRange(PageRange[] pageRanges, int page)
        {
            for (int i = 0; i<pageRanges.length; i++)
            {
                if ((page >= pageRanges[i].getStart()) &&
                        (page <= pageRanges[i].getEnd()))
                    return true;
            }
            return false;
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        private void drawPage(PdfDocument.Page page,
                              int pagenumber) {
            Canvas canvas = page.getCanvas();

            pagenumber++; // Make sure page numbers start at 1
            SharedPreferences prefs = getSharedPreferences(getString(R.string.preferencias_medico), MODE_PRIVATE);
            String nombre_medico =prefs.getString("nombre", null);
            String rut_medico =prefs.getString("rut", null);
            String titulo_medico =prefs.getString("titulo", null);
            String direccion_medico =prefs.getString("direccion", null);

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            canvas.drawText("Dr/a "+nombre_medico,350, 50, paint);
            paint.setTextSize(15);
            canvas.drawText(titulo_medico, 450, 75, paint);
            canvas.drawText("RUT "+rut_medico, 450, 95, paint);
            canvas.drawText(direccion_medico, 450, 115, paint);

            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(50,150, 575, 275 , paint);

            paint.setTextSize(18);
            canvas.drawText("Nombre: ", 65, 175, paint);
            canvas.drawLine(140, 175, 560, 175, paint);
            canvas.drawText(nombre, 140, 175, paint);

            canvas.drawText("RUT:", 65, 200, paint);
            canvas.drawLine(105, 200, 290, 200, paint);
            canvas.drawText(rut, 105, 200, paint);



            canvas.drawText("Edad: ", 300, 200, paint);
            canvas.drawLine(360, 200, 560, 200, paint);
            canvas.drawText(calculaEdad(fecha)+" años", 360, 200, paint);

            canvas.drawText("Sexo: ", 65, 225, paint);
            canvas.drawLine(115, 225, 210, 225, paint);
            canvas.drawText(sexo, 115, 225, paint);

            canvas.drawText("Dirección: ", 220, 225, paint);
            canvas.drawLine(305, 225, 560, 225, paint);
            canvas.drawText(direccion, 305, 225, paint);


            canvas.drawText("Diagnóstico: ", 65, 250, paint);
            canvas.drawLine(175, 250, 560, 250, paint);
            try {
                canvas.drawText(diagnostico, 175, 250, paint);
            }
            catch (Exception e){
                canvas.drawText("Sin Diagnóstico", 175, 250, paint);
            }

            canvas.drawText("Medicamentos: ", 65, 310, paint);
            canvas.drawLine(65, 310, 100, 310, paint);
            try{
                for (int cnt=0;cnt <medicamentos.size(); cnt++) {
                    String med = medicamentos.get(cnt);
                    canvas.drawCircle(80, 330 + (cnt * 20), 5, paint);
                    canvas.drawText(med, 90, 335 + (cnt * 20), paint);
                }
            }
            catch(Exception e){
                canvas.drawText("Sin Medicamentos", 90, 335 , paint);
            }

            String ano = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) ;
            String mes = Integer.toString(Calendar.getInstance().get(Calendar.MONTH)+1) ;
            String dia = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) ;



            canvas.drawText("Fecha: ", 65, 720, paint);
            canvas.drawText(dia+"/", 120, 720, paint  );
            canvas.drawText(mes+"/", 140, 720, paint  );
            canvas.drawText(ano, 170, 720, paint  );
            canvas.drawLine(400, 720, 560, 720, paint  );
            canvas.drawText("Firma Médico", 420, 740, paint  );

        }

    }

    public void eliminarReceta() {

        // Luego de eliminar al paciente, se envia un toast y se envia al mainActivity

        ConexionSQLHelper conn = new ConexionSQLHelper(this, "bd_recetas", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        String delete = "DELETE FROM "+ TABLA_RECETA+" WHERE "+Utilidades.CAMPO_FECHA_ACTUAL+"= '"+DateActual+"' ;" ;

        db.execSQL(delete);

        Toast.makeText(getApplicationContext(), "Receta Eliminada, Volviendo al inicio", Toast.LENGTH_SHORT).show();

        db.close();

        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }


    public String calculaEdad(String fechaNac) {
        String[] fecha = fechaNac.split("-");
        Calendar today = Calendar.getInstance();

        int diff_year = today.get(Calendar.YEAR) -  Integer.parseInt(fecha[2]);
        int diff_month = today.get(Calendar.MONTH) - Integer.parseInt(fecha[1]);
        int diff_day = today.get(Calendar.DAY_OF_MONTH) - Integer.parseInt(fecha[0]);

        //Si está en ese año pero todavía no los ha cumplido
        if (diff_month < 0 || (diff_month == 0 && diff_day < 0)) {
            diff_year = diff_year - 1; //no aparecían los dos guiones del postincremento :|
        }
        return Integer.toString(diff_year);
    }
}
