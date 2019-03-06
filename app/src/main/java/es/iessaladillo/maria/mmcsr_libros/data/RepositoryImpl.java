package es.iessaladillo.maria.mmcsr_libros.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import es.iessaladillo.maria.mmcsr_libros.data.local.LibroDAO;
import es.iessaladillo.maria.mmcsr_libros.data.local.model.Libro;

public class RepositoryImpl implements Repository {

    private final LibroDAO libroDAO;

    public RepositoryImpl(LibroDAO libroDAO) {
        this.libroDAO = libroDAO;
    }

    @Override
    public LiveData<List<Libro>> queryAllLibros() {
        return libroDAO.queryAllLibros();
    }
}
