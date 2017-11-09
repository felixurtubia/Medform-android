package cl.tello_urtubia.medform;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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
//import java.time.LocalDate;
//import java.time.Period;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import cl.tello_urtubia.medform.Utilidades.Utilidades;

import static cl.tello_urtubia.medform.R.id.campoDireccion;
import static cl.tello_urtubia.medform.R.id.campoFecha;
import static cl.tello_urtubia.medform.R.id.campoRut;
import static cl.tello_urtubia.medform.Utilidades.Utilidades.*;
import static cl.tello_urtubia.medform.Utilidades.Utilidades.CAMPO_NOMBRE;

public class CrearRecetaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText campoMedicamento, campoDiagnostico;
    ArrayList<String> medicamentos = new ArrayList<String>();
    String diagnostico;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crearReceta);
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
        campoMedicamento = (EditText) findViewById(R.id.campoMedicamento);
        campoDiagnostico = (EditText) findViewById(R.id.campoDiagnostico);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crear_receta, menu);
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
            case R.id.action_print:
                String nombre = getIntent().getStringExtra("nombre");
                String rut = getIntent().getStringExtra("rut");
                String sexo = getIntent().getStringExtra("sexo");
                String fecha = getIntent().getStringExtra("fecha");
                String direccion = getIntent().getStringExtra("direccion");

                registrarRecetasSQL(nombre, rut, sexo, fecha, direccion, medicamentos, diagnostico);

                PrintManager printManager = (PrintManager) this
                        .getSystemService(Context.PRINT_SERVICE);

                String jobName = this.getString(R.string.app_name) +
                        " Document";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    printManager.print(jobName, new MyPrintDocumentAdapter(this),
                            null);
                }
                break;

            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void agregarDiagnostico (View view){
        diagnostico = campoDiagnostico.getText().toString();
        Toast.makeText(getApplicationContext(), "Diagnostico agregado, Si quiere puede editarlo.", Toast.LENGTH_LONG).show();
        campoDiagnostico.setText("");
    }

    public void agregarMedicamento(View view){
        medicamentos.add(campoMedicamento.getText().toString());
        Toast.makeText(getApplicationContext(), "Medicamento agregado, Si quiere puede agregar otro.", Toast.LENGTH_LONG).show();
        campoMedicamento.setText("");
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
            String nombre = getIntent().getStringExtra("nombre");
            String rut = getIntent().getStringExtra("rut");
            String sexo = getIntent().getStringExtra("sexo");
            String fecha = getIntent().getStringExtra("fecha");
            String direccion = getIntent().getStringExtra("direccion");

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

    public void registrarRecetasSQL(String nombre, String rut, String sexo, String fecha, String direccion, ArrayList<String> medicamentos, String diagnostico) {

        RecetaSQLHelper conn = new RecetaSQLHelper(this, "bd_recetas", null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();


        String insertR = "INSERT INTO "+ TABLA_RECETA+" ( "+ CAMPO_NOMBRE+","
                + CAMPO_RUT+","+CAMPO_FECHA+","+CAMPO_SEXO+","+CAMPO_DIRECCION+","+CAMPO_DIAGNOSTICO+")"
                + "VALUES ( '"+nombre+"', '"+rut+"', '"+fecha+"', '"+sexo+"', '"+direccion+"', '"+diagnostico+"' )" ;

        db.execSQL(insertR);



        db.close();


        Toast.makeText(getApplicationContext(), "Receta Lista, Imprimiendo...", Toast.LENGTH_SHORT).show();


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
