package es.iessaladillo.maria.mmcsr_libros.ui.lista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.iessaladillo.maria.mmcsr_libros.R;
import es.iessaladillo.maria.mmcsr_libros.base.EventObserver;
import es.iessaladillo.maria.mmcsr_libros.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_libros.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_libros.databinding.FragmentListaBinding;
import es.iessaladillo.maria.mmcsr_libros.utils.SnackbarUtils;

public class ListaFragment extends Fragment {

    private ListaFragmentViewModel viewModel;
    private FragmentListaBinding b;
    private NavController navController;
    private ListaFragmentAdapter listAdapter;

    public static ListaFragment newInstance() {
        return new ListaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_lista, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        viewModel = ViewModelProviders.of(this, new ListaFragmentViewModelFactory(requireActivity().getApplication(),
                new RepositoryImpl(appDatabase.libroDao()))).get(ListaFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setupViews();
        observeLibros();
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
        inflater.inflate(R.menu.fragment_lista_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    private void setupViews() {
        b.lblEmptyLibros.setOnClickListener(l->navController.navigate(R.id.action_listaFragment_to_agregarFragment));
        setupToolbar();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        listAdapter = new ListaFragmentAdapter();
        b.lstLibros.setHasFixedSize(true);
        b.lstLibros.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.libros_lstLibros_columns)));
        b.lstLibros.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        b.lstLibros.setItemAnimator(new DefaultItemAnimator());
        b.lstLibros.setAdapter(listAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        viewModel.borrarLibro(listAdapter.getItem(viewHolder.getAdapterPosition()));
                    }
                });

        itemTouchHelper.attachToRecyclerView(b.lstLibros);
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
    }

    private void observeLibros() {
        viewModel.getCompanies().observe(this, libros -> {
            listAdapter.submitList(libros);
            b.lblEmptyLibros.setVisibility(libros.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        });
    }

    private void observeSuccessMessage() {
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::showMessage));
    }

    private void observeErrorMessage() {
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::showMessage));
    }

    private void showMessage(String message) {
        SnackbarUtils.snackbar(b.lblEmptyLibros, message);
    }

}
