package es.iessaladillo.maria.mmcsr_libros.data.local.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "libro")
public class Libro {
    @ColumnInfo(name = "titulo")
    private String titulo;
    @ColumnInfo(name = "autor")
    private String autor;
    @ColumnInfo(name = "fechaPublicacion")
    private String fechaPublicacion;
    @ColumnInfo(name = "urlPortada")
    private String urlPortada;
    @ColumnInfo(name = "sinopsis")
    private String sinopsis;

    public Libro(String titulo, String autor, String fechaPublicacion, String urlPortada, String sinopsis) {
        this.titulo = titulo;
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.urlPortada = urlPortada;
        this.sinopsis = sinopsis;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getUrlPortada() {
        return urlPortada;
    }

    public void setUrlPortada(String urlPortada) {
        this.urlPortada = urlPortada;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }
}
