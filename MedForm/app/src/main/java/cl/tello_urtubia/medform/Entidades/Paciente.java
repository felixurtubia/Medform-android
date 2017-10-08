package cl.tello_urtubia.medform.Entidades;



public class Paciente {
    private String nombre;
    private String rut;
    private String sexo;
    private String fecha;
    private String direccion;

    public Paciente(String nombre, String rut, String sexo, String fecha, String direccion) {
        this.nombre = nombre;
        this.rut = rut;
        this.sexo = sexo;
        this.fecha = fecha;
        this.direccion = direccion;
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
}