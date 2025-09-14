package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookActivity extends AppCompatActivity {
    private static final String TAG = "AddBookActivity";
    public static final String EXTRA_NEW_BOOK = "extra_new_book";

    private EditText etTitle, etAuthor, etDescription;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_add_book);

            initViews();
            setupListeners();
        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate: " + e.getMessage());
            Toast.makeText(this, "Error al cargar la actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        Log.d(TAG, "Buscando vistas...");
        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        Log.d(TAG, "Todas las vistas encontradas exitosamente");
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveNewBook());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveNewBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Validación de campos obligatorios
        if (title.isEmpty()) {
            etTitle.setError("El título es obligatorio");
            etTitle.requestFocus();
            return;
        }

        if (author.isEmpty()) {
            etAuthor.setError("El autor es obligatorio");
            etAuthor.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            description = "Sin descripción"; // Valor por defecto
        }

        // Crear nuevo libro (sin ID, el servidor lo asignará)
        Book newBook = new Book(0, title, author, description);

        // Llamada a la API
        BookApiService apiService = ApiClient.getBookApiService();
        Call<Book> call = apiService.createBook(newBook);

        // Deshabilitar botón mientras se procesa
        btnSave.setEnabled(false);
        btnSave.setText("Guardando...");

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                btnSave.setEnabled(true);
                btnSave.setText("Guardar");

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Libro creado exitosamente: " + response.body().toString());

                    // Devolver el libro creado a MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_NEW_BOOK, response.body());
                    setResult(Activity.RESULT_OK, resultIntent);

                    Toast.makeText(AddBookActivity.this, "Libro agregado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e(TAG, "Error al crear libro. Código: " + response.code());
                    String errorMsg = "Error al crear libro";

                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ": " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error al leer error body: " + e.getMessage());
                    }

                    Toast.makeText(AddBookActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                btnSave.setEnabled(true);
                btnSave.setText("Guardar");

                Log.e(TAG, "Error en la llamada: " + t.getMessage());
                Toast.makeText(AddBookActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}