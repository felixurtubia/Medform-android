package cl.tello_urtubia.medform.Entidades;


public class Receta {

    private String nombre;
    private String rut;
    private String sexo;
    private String fecha;
    private String direccion;
    private String diagnostico;
    private String DateActual;

    public Receta(){

    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getDateActual() {
        return DateActual;
    }

    public void setDateActual(String dateActual) {
        DateActual = dateActual;
    }
}
