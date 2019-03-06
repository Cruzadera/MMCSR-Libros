package es.iessaladillo.maria.mmcsr_libros.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import es.iessaladillo.maria.mmcsr_libros.base.Resource;
import es.iessaladillo.maria.mmcsr_libros.data.local.model.Libro;

public interface Repository {

    LiveData<List<Libro>> queryAllLibros();

    LiveData<Resource<Long>> insertLibro(Libro libro);

    LiveData<Resource<Integer>> deleteLibro(Libro libro);
}
