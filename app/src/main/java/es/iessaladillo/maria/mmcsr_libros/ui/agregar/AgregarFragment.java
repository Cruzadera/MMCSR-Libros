package es.iessaladillo.maria.mmcsr_libros.ui.agregar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import es.iessaladillo.maria.mmcsr_libros.R;
import es.iessaladillo.maria.mmcsr_libros.base.EventObserver;
import es.iessaladillo.maria.mmcsr_libros.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_libros.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_libros.data.local.model.Libro;
import es.iessaladillo.maria.mmcsr_libros.databinding.FragmentAgregarBinding;
import es.iessaladillo.maria.mmcsr_libros.ui.dialog.YesNoDialogFragment;
import es.iessaladillo.maria.mmcsr_libros.utils.KeyboardUtils;
import es.iessaladillo.maria.mmcsr_libros.utils.SnackbarUtils;
import es.iessaladillo.maria.mmcsr_libros.utils.TextWatcherUtils;

public class AgregarFragment extends Fragment implements YesNoDialogFragment.Listener, SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG_DIALOG_FRAGMENT = "TAG_DIALOG_FRAGMENT";
    private static final int RC_DIALOG_FRAGMENT = 1;
    private AgregarFragmentViewModel viewModel;
    private FragmentAgregarBinding b;
    private NavController navController;
    private SharedPreferences sharedPreferences;

    public static AgregarFragment newInstance() {
        return new AgregarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_agregar, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        viewModel = ViewModelProviders.of(this, new AgregarFragmentViewModelFactory(requireActivity().getApplication(),
                new RepositoryImpl(appDatabase.libroDao()))).get(AgregarFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        setupViews();
        observeSuccessMessage();
        observeErrorMessage();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_agregar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Conflicts navigation lib with de action in the item menu
        if(item.getItemId() == R.id.menuAdd) {
            showConfirmationDialog();
        }else{
            requireActivity().onBackPressed();
        }
        return true;
    }

    private boolean save() {
        boolean estaGuardado = true;
        if (isValidForm()) {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            SnackbarUtils.snackbar(b.tilAutor, getString(R.string.main_saved_succesfully));
            Libro libro = getDataLibro();
            viewModel.insertarLibro(libro);
        } else {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            SnackbarUtils.snackbar(b.tilAutor, getString(R.string.main_error_saving));
            checkField(b.tilAutor, b.txtAutor);
            checkField(b.tilTitulo, b.txtTitulo);
            checkField(b.tilFecha, b.txtFecha);
            estaGuardado = false;
        }
        return  estaGuardado;
    }

    @SuppressWarnings("ConstantConditions")
    private Libro getDataLibro() {
        Libro libro = new Libro();
        libro.setIdLibro(0);
        libro.setAutor(b.txtAutor.getText().toString());
        libro.setTitulo(b.txtTitulo.getText().toString());
        libro.setFechaPublicacion(b.txtFecha.getText().toString());
        libro.setUrlPortada(b.txtUrlPortada.getText().toString());
        return libro;
    }

    private boolean isValidForm() {
        return checkField(b.tilAutor, b.txtAutor) && checkField(b.tilTitulo, b.txtTitulo)
                && checkField(b.tilFecha, b.txtFecha);
    }

    private void observeSuccessMessage() {
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::showMessageAndFinish));
    }

    private void observeErrorMessage() {
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::showMessage));
    }

    private void showMessage(String message) {
        SnackbarUtils.snackbar(b.tilAutor, message);
    }

    private void showMessageAndFinish(String message) {
        Snackbar.make(b.tilAutor, message, Snackbar.LENGTH_SHORT).addCallback(
                new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        requireActivity().onBackPressed();
                    }
                }).show();
    }


    private void setupViews() {
        setupToolbar();
        TextWatcherUtils.setAfterTextChangeListener(b.txtTitulo, s -> checkField(b.tilTitulo, b.txtTitulo));
        TextWatcherUtils.setAfterTextChangeListener(b.txtAutor, s -> checkField(b.tilAutor, b.txtAutor));
        TextWatcherUtils.setAfterTextChangeListener(b.txtFecha, s -> checkField(b.tilFecha, b.txtFecha));
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean checkField(TextInputLayout til, TextInputEditText txt) {
        boolean esValido;
        if(TextUtils.isEmpty(txt.getText().toString())){
            esValido = invalidateField(txt);
        }else{
            til.setEnabled(true);
            esValido = true;
        }
        return esValido;
    }

    private boolean invalidateField(TextInputEditText txt) {
        txt.setError(getString(R.string.main_invalid_data));
        return false;
    }

    private void showConfirmationDialog() {
        YesNoDialogFragment dialogFragment = YesNoDialogFragment.newInstance(
                getString(R.string.main_fragment_confirm_deletion),
                getString(R.string.main_fragment_sure), getString(R.string.main_fragment_yes),
                getString(R.string.main_fragment_no), this, 1);
        dialogFragment.setTargetFragment(this, RC_DIALOG_FRAGMENT);
        dialogFragment.show(getFragmentManager(), TAG_DIALOG_FRAGMENT);
    }

    @Override
    public void onPositiveButtonClick(DialogInterface dialog) {
        if(save()){
            navController.navigate(R.id.listaFragment);
        }
    }

    @Override
    public void onNegativeButtonClick(DialogInterface dialog) {
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (TextUtils.equals(key, getString(R.string.prefSync_key))) {
//            showConfirmationDialog();
//        }else{
//            save();
//        }
    }


    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
