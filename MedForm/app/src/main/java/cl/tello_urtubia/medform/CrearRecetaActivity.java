package cl.tello_urtubia.medform;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.icu.util.Calendar;
import android.os.Build;
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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

import static cl.tello_urtubia.medform.R.id.campoDireccion;
import static cl.tello_urtubia.medform.R.id.campoFecha;
import static cl.tello_urtubia.medform.R.id.campoRut;

public class CrearRecetaActivity extends AppCompatActivity {

    EditText campoMedicamento, campoDiagnostico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crearReceta);
        setSupportActionBar(toolbar);

        campoMedicamento = (EditText) findViewById(R.id.campoMedicamento);
        campoDiagnostico = (EditText) findViewById(R.id.campoDiagnostico);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crear_receta, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_user_settings:
                break;
            case R.id.action_settings:
                break;
            case R.id.action_print:
                break;
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick (View view) {
        String nombre = getIntent().getStringExtra("nombre");
        String rut = getIntent().getStringExtra("rut");
        String medicamento = campoMedicamento.getText().toString();
        String diagnostico = campoDiagnostico.getText().toString();
        registrarRecetasSQL(nombre, rut, medicamento, diagnostico);
        crearArchivoReceta(nombre,rut,medicamento,diagnostico);

        Intent intent = new Intent(this,MainActivity.class); // Luego de crear la receta, volvemos al inicio
        startActivity(intent);
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


        private void drawPage(PdfDocument.Page page,
                              int pagenumber) {
            Canvas canvas = page.getCanvas();

            pagenumber++; // Make sure page numbers start at 1

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            canvas.drawText("Dr/a Nombre del Doctor ",350, 50, paint);
            paint.setTextSize(15);
            canvas.drawText("Titulo Medico", 450, 75, paint);
            canvas.drawText("RUT 9.999.999-9", 450, 95, paint);
            canvas.drawText("Dirección Consulta", 450, 115, paint);

            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(50,150, 575, 275 , paint);

            paint.setTextSize(18);
            canvas.drawText("Nombre: ", 65, 175, paint);
            canvas.drawLine(140, 175, 560, 175, paint);
            canvas.drawText("José Tello", 140, 175, paint);

            canvas.drawText("RUT:", 65, 200, paint);
            canvas.drawLine(105, 200, 290, 200, paint);
            canvas.drawText("19241676-0", 105, 200, paint);

            canvas.drawText("Fecha: ", 300, 200, paint);
            canvas.drawLine(360, 200, 560, 200, paint);
            canvas.drawText("09/03/96", 360, 200, paint);

            canvas.drawText("Sexo: ", 65, 225, paint);
            canvas.drawLine(115, 225, 210, 225, paint);
            canvas.drawText("Masculino", 115, 225, paint);

            canvas.drawText("Dirección: ", 220, 225, paint);
            canvas.drawLine(305, 225, 560, 225, paint);
            canvas.drawText("Calle Siempre Viva 123", 305, 225, paint);


            canvas.drawText("Diagnóstico: ", 65, 250, paint);
            canvas.drawLine(175, 250, 560, 250, paint);
            canvas.drawText("Flojitis Aguda", 175, 250, paint);

            canvas.drawText("Medicamentos: ", 65, 310, paint);
            canvas.drawLine(65, 310, 100, 310, paint);

            //Aqui debiera ir un for, ya que son 1...* medicamentos, pero para saber como irá haremos solo un medicamento
            //El sgte. codigo se repeteria por cada iteracion y se sumaria un delta a cada canvas respecto a la variable y
            canvas.drawCircle(80, 330, 5, paint);
            canvas.drawText("Paracetamol 500 mg c/8 horas", 90, 335, paint);

           //Calendar rightNow = Calendar.getInstance(); Nose porque explota con estooooo
            //int day = rightNow.DAY_OF_MONTH;
            //int month = rightNow.MONTH;
            //int year = rightNow.YEAR;


            canvas.drawText("Fecha: ", 65, 720, paint);
            canvas.drawText("6/", 120, 720, paint  ); //Dia del Mes
            canvas.drawText("11/", 140, 720, paint  ); // Mes
            canvas.drawText("17", 170, 720, paint  ); // Año, pensaba tambien poner la hora pero siento que seria desperdicio de espacio :s
            canvas.drawLine(400, 720, 560, 720, paint  );
            canvas.drawText("Firma Médico", 420, 740, paint  );

        }

    }

    public void printDocument(View view)
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            printManager.print(jobName, new MyPrintDocumentAdapter(this),
                    null);
        }
    }

    public void crearArchivoReceta(String nombre, String rut, String medicamento, String diagnostico) {

    }

    public void registrarRecetasSQL(String nombre, String rut, String medicamento, String diagnostico) {

        RecetaSQLHelper conn = new RecetaSQLHelper(this, "bd_recetas", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        String insert = "INSERT INTO "+ Utilidades.TABLA_RECETA+" ( "+Utilidades.CAMPO_NOMBRE+","
                +Utilidades.CAMPO_RUT+","+Utilidades.CAMPO_MEDICAMENTO+","+Utilidades.CAMPO_DIAGNOSTICO+")"
                + "VALUES ( '"+nombre+"', '"+rut+"', '" +medicamento+"', '"+diagnostico+"' )" ;

        db.execSQL(insert);

        Toast.makeText(getApplicationContext(), "Receta Lista, Imprimiendo...", Toast.LENGTH_SHORT).show();

        db.close(); // MIRA FELIX CULIAO PA QUE VEIA QUE CIERRO LAS CONEXIONES A LAS BASES DE DATOS
    }

}
