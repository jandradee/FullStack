package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnBookActionListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private FloatingActionButton fabAddBook;

    private ActivityResultLauncher<Intent> addBookLauncher;

    //Busqueda y ordenamiento
    private List<Book> allBooks;
    private boolean isAscending = true;

    private SearchView searchView;
    private Button btnSort;

    private TextView tvNoResults;


    // ActivityResultLauncher para manejar el resultado de EditBookActivity
    private ActivityResultLauncher<Intent> editBookLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initActivityResultLauncher();
        loadBooks();
        setupListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fabAddBook = findViewById(R.id.fabAddBook);

        //Busqueda y ordenamiento
        searchView = findViewById(R.id.seaarchView);
        btnSort = findViewById(R.id.btnSort);
        tvNoResults = findViewById(R.id.tvNoResults);
    }

    private void initActivityResultLauncher() {
        editBookLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Book updatedBook = (Book) result.getData().getSerializableExtra(EditBookActivity.EXTRA_BOOK);
                        int position = result.getData().getIntExtra(EditBookActivity.EXTRA_POSITION, -1);

                        if (updatedBook != null && position != -1) {
                            bookAdapter.updateBook(position, updatedBook);
                        }
                    }
                }
        );

        addBookLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
           if (result.getResultCode() == Activity.RESULT_OK) {
               Intent data = result.getData();
                       if(data != null && data.hasExtra(AddBookActivity.EXTRA_NEW_BOOK)) {
                           Book newBook = (Book) data.getSerializableExtra(AddBookActivity.EXTRA_NEW_BOOK);
                           if (newBook != null) {
                               bookAdapter.addBook(newBook);
                               recyclerView.scrollToPosition(bookAdapter.getItemCount() -1);
                               Log.d(TAG, "Libro agregado a la lista: " + newBook.getTitle());
                           }
                       }

           }
        });
    }

    private void setupListeners() {
        fabAddBook.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            addBookLauncher.launch(intent);
        });
        // Listener para el campo de búsqueda
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Si el texto está vacío, recarga la lista completa de libros desde la API
                if (newText == null || newText.isEmpty()) {
                    loadBooks(); // Llama al método que recarga la lista
                } else {
                    filterBooks(newText);
                }
                return true;
            }
        });

        // Listener para el botón de ordenar
        btnSort.setOnClickListener(view -> {
            sortBooks();
        });
    }
    private void loadBooks() {
        BookApiService apiService = ApiClient.getBookApiService();
        Call<List<Book>> call = apiService.getBooks();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful() && response.body() != null) {
/*                    List<Book> books = response.body();
                    Log.d(TAG, "Libros obtenidos: " + books.size());
                    // Mostrar libros en el log
                    for (Book book : books) {
                        Log.d(TAG, book.toString());
                    }
                    // Configurando el adapter con el listener
                    bookAdapter = new BookAdapter(books, MainActivity.this);
                    recyclerView.setAdapter(bookAdapter);

                    Toast.makeText(MainActivity.this, "Se cargaron " + books.size() + " libros", Toast.LENGTH_SHORT).show(); */

                    allBooks = response.body();
                    bookAdapter = new BookAdapter(allBooks, MainActivity.this);
                    recyclerView.setAdapter(bookAdapter);
                    Log.d(TAG, "Libros cargados exitosamente: "+ allBooks.size());
                } else {
                    Log.e(TAG, "Error en la respuesta: " + response.code());
                    Toast.makeText(MainActivity.this, "Error al cargar libros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e(TAG, "Error en la llamada: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterBooks (String query) {
        if (allBooks == null) return;
        List<Book> filteredList = new ArrayList<>();
        for(Book book : allBooks) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) || book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(book);
            }
        }
        bookAdapter.setBooks(filteredList);
    }

    private void sortBooks() {
        if(bookAdapter == null || bookAdapter.getItemCount() == 0) return;

        List<Book> currentBooks = new ArrayList<>(bookAdapter.getBooks());
        Collections.sort(currentBooks, (b1, b2) -> {
            if (isAscending) {
                return b1.getTitle().compareToIgnoreCase(b2.getTitle());
            } else {
                return b2.getTitle().compareToIgnoreCase(b1.getTitle());
            }
        });

        isAscending = !isAscending;
        bookAdapter.setBooks(currentBooks);
    }
    public void onEditBook(Book book, int position) {
        Intent intent = new Intent(this, EditBookActivity.class);
        intent.putExtra("extra_book", book);
        intent.putExtra("extra_position", position);
        editBookLauncher.launch(intent);
    }

    @Override
    public void onDeleteBook(Book book, int position) {
        // Mostrar diálogo de confirmación
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar \"" + book.getTitle() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteBookFromServer(book, position))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteBookFromServer(Book book, int position) {
        BookApiService apiService = ApiClient.getBookApiService();
        Call<Void> call = apiService.deleteBook(book.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Libro eliminado exitosamente");

                    // Remover el libro de la lista
                    bookAdapter.removeBook(position);

                    Toast.makeText(MainActivity.this, "Libro eliminado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Error al eliminar libro: " + response.code());
                    Toast.makeText(MainActivity.this, "Error al eliminar libro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error en la llamada: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}