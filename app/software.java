public class software {
    int id;
    String nombre;
    String versionsoft;
    int espaciomb;
    double precio;

    // Constructor
    public software(int id, String nombre, String versionsoft, int espaciomb, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.versionsoft = versionsoft;
        this.espaciomb = espaciomb;
        this.precio = precio;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getVersionsoft() {
        return versionsoft;
    }

    public void setVersionsoft(String versionsoft) {
        this.versionsoft = versionsoft;
    }

    public int getEspaciomb() {
        return espaciomb;
    }

    public void setEspaciomb(int espaciomb) {
        this.espaciomb = espaciomb;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
