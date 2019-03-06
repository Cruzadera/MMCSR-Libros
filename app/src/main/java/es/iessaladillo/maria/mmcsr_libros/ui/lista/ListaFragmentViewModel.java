package es.iessaladillo.maria.mmcsr_libros.ui.lista;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import es.iessaladillo.maria.mmcsr_libros.R;
import es.iessaladillo.maria.mmcsr_libros.base.Event;
import es.iessaladillo.maria.mmcsr_libros.base.Resource;
import es.iessaladillo.maria.mmcsr_libros.data.Repository;
import es.iessaladillo.maria.mmcsr_libros.data.local.model.Libro;

class ListaFragmentViewModel extends ViewModel {
    private final Application application;
    private final LiveData<List<Libro>> libros;
    private final MutableLiveData<Libro> deleteTrigger = new MutableLiveData<>();
    private final LiveData<Resource<Integer>> deletionResult;
    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();

    ListaFragmentViewModel(Application application, Repository repository) {
        this.application = application;
        libros = repository.queryAllLibros();
        deletionResult = Transformations.switchMap(deleteTrigger, repository::deleteLibro);
        setupSuccessMessage();
        setupErrorMessage();
    }
    private void setupSuccessMessage() {
        successMessage.addSource(deletionResult, resource -> {
            if (resource.hasSuccess()) {
                successMessage.setValue(new Event<>(application.getString(R.string.list_deleted_successfully)));
            }
        });
    }

    private void setupErrorMessage() {
        errorMessage.addSource(deletionResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(application.getString(R.string.list_error_deleting)));
            }
        });
    }

    LiveData<List<Libro>> getCompanies() {
        return libros;
    }

    LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }

    void borrarLibro(Libro libro){
        deleteTrigger.setValue(libro);
    }
}
