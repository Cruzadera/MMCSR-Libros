package es.iessaladillo.maria.mmcsr_libros.data;

import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import es.iessaladillo.maria.mmcsr_libros.base.Resource;
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


    @Override
    public LiveData<Resource<Long>> insertLibro(Libro libro) {
        MutableLiveData<Resource<Long>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                long id = libroDAO.insert(libro);
                result.postValue(Resource.success(id));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<Resource<Integer>> deleteLibro(Libro libro) {
        MutableLiveData<Resource<Integer>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                int deleted = libroDAO.delete(libro);
                result.postValue(Resource.success(deleted));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }
}
