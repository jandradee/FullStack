package com.example.app;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>{
    private List<Book> books;
    private OnBookActionListener listener;

    public BookAdapter(List<Book> books, OnBookActionListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.descriptionTextView.setText(book.getDescription());

        //Configurar listener para los botones
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null){
                listener.onEditBook(book, position);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null){
                listener.onDeleteBook(book, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void addBook (Book newBook) {
        books.add(newBook);
        notifyItemInserted(books.size() -1);

    }
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, descriptionTextView;
        Button btnEdit, btnDelete;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    //Método para Eliminar Libro
    public void removeBook(int position){
        if (position >= 0 && position < books.size()){
            books.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, books.size());
        }
    }

    //Método para actualizar un libro de la lista
    public void updateBook(int position, Book updateBook){
        if (position >= 0 && position < books.size()){
            books.set(position, updateBook);
            notifyItemChanged(position);
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> newBooks) {
        this.books.clear();
        this.books.addAll(newBooks);
        notifyDataSetChanged();
    }
}
