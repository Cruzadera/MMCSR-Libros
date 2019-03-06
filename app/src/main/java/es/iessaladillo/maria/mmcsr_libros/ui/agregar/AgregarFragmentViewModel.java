package es.iessaladillo.maria.mmcsr_libros.ui.agregar;

import android.app.Application;

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

class AgregarFragmentViewModel extends ViewModel {
    private final Application application;
    private final Repository repository;
    private boolean dialogo;
    private final MutableLiveData<Libro> insertTrigger = new MutableLiveData<>();
    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();
    private final LiveData<Resource<Long>> insertResult;

    AgregarFragmentViewModel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
        insertResult = Transformations.switchMap(insertTrigger, repository::insertLibro);
        setupSuccessMessage();
        setupErrorMessage();
    }

    private void setupSuccessMessage() {
        successMessage.addSource(insertResult, resource -> {
            if (resource.hasSuccess() && resource.getData() >= 0) {
                successMessage.setValue(new Event<>(application.getString(R.string.libro_inserted_successfully)));
            }
        });
    }

    private void setupErrorMessage() {
        errorMessage.addSource(insertResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(resource.getException().getMessage()));
            } else if (resource.hasSuccess() && resource.getData() <= 0) {
                errorMessage.setValue(new Event<>(application.getString(R.string.libro_error_inserting)));
            }
        });
    }


    LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }

    void insertarLibro(Libro libro) {
        insertTrigger.setValue(libro);
    }

    public boolean isDialogo() {
        return dialogo;
    }

    public void setDialogo(boolean dialogo) {
        this.dialogo = dialogo;
    }
}
