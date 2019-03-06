package es.iessaladillo.maria.mmcsr_libros.data.local;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import es.iessaladillo.maria.mmcsr_libros.base.BaseDao;
import es.iessaladillo.maria.mmcsr_libros.data.local.model.Libro;

@Dao
public interface LibroDAO extends BaseDao<Libro> {

    @Query("SELECT * FROM libro")
    LiveData<List<Libro>> queryAllLibros();
}
