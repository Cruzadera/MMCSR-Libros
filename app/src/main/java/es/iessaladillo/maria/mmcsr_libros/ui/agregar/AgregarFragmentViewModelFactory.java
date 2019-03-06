package es.iessaladillo.maria.mmcsr_libros.ui.agregar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import es.iessaladillo.maria.mmcsr_libros.data.Repository;



public class AgregarFragmentViewModelFactory  implements ViewModelProvider.Factory{

    private final Application application;
    private final Repository repository;

    AgregarFragmentViewModelFactory(@NonNull Application application, @NonNull Repository repository) {
        this.application = application;
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AgregarFragmentViewModel.class)) {
            return (T) new AgregarFragmentViewModel(application, repository);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
