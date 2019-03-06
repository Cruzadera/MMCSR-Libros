package es.iessaladillo.maria.mmcsr_libros.ui.lista;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import es.iessaladillo.maria.mmcsr_libros.R;
import es.iessaladillo.maria.mmcsr_libros.data.local.model.Libro;

public class ListaFragmentAdapter extends ListAdapter<Libro, ListaFragmentAdapter.ViewHolder> {

    ListaFragmentAdapter() {
        super(new DiffUtil.ItemCallback<Libro>() {
            @Override
            public boolean areItemsTheSame(@NonNull Libro oldItem, @NonNull Libro newItem) {
                return oldItem.getIdLibro() == newItem.getIdLibro();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Libro oldItem, @NonNull Libro newItem) {
                return oldItem.getTitulo().equals(newItem.getTitulo())&&
                        oldItem.getAutor().equals(newItem.getAutor()) &&
                        oldItem.getFechaPublicacion().equals(newItem.getFechaPublicacion());
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    protected Libro getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItem(position).getIdLibro();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView lblTitulo;
        private final TextView lblAutor;
        private final TextView lblFecha;
        private final ImageView imgPortada;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblTitulo = ViewCompat.requireViewById(itemView, R.id.lblTitulo);
            lblAutor = ViewCompat.requireViewById(itemView, R.id.lblAutor);
            lblFecha = ViewCompat.requireViewById(itemView, R.id.lblFecha);
            imgPortada = ViewCompat.requireViewById(itemView, R.id.imgPortada);
        }

        void bind(Libro libro){
            lblTitulo.setText(libro.getTitulo());
            lblFecha.setText(libro.getFechaPublicacion());
            lblAutor.setText(libro.getAutor());
            Picasso.with(itemView.getContext()).load(libro.getUrlPortada())
                    .error(R.mipmap.ic_launcher_round)
                    .resize(200, 200)
                    .into(imgPortada);
        }
    }
}
