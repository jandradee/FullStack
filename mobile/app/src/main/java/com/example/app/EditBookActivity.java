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

public class EditBookActivity extends AppCompatActivity {
    private static final String TAG = "EditBookActivity";
    public static final String EXTRA_BOOK = "extra_book";
    public static final String EXTRA_POSITION = "extra_position";

    private EditText etTitle, etAuthor, etDescription;
    private Button btnSave, btnCancel;
    private Book currenBook;

    private int position;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        initViews();
        getIntentData();
        setupListeners();
    }

    private void initViews(){
        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_BOOK) && intent.hasExtra(EXTRA_POSITION)){
            currenBook = (Book) intent.getSerializableExtra(EXTRA_BOOK);
            position = intent.getIntExtra(EXTRA_POSITION, -1);

            if(currenBook != null){
                etTitle.setText(currenBook.getTitle());
                etAuthor.setText(currenBook.getAuthor());
                etDescription.setText(currenBook.getDescription());

            }
        }
    }

    private void setupListeners(){
        btnSave.setOnClickListener(v -> saveBook());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty()){
            Toast.makeText(this, "Título y Autor son Obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Book updatedBook = new Book(currenBook.getId(), title, author, description);

        BookApiService apiService = ApiClient.getBookApiService();
        Call<Book> call = apiService. updateBook(currenBook.getId(), updatedBook);

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(response.isSuccessful() && response.body() !=null) {
                    Log.d(TAG, "Libro Actualizado Exitosamente");

                    //Devolver libro al mainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_BOOK, response.body());
                    resultIntent.putExtra(EXTRA_POSITION, position);
                    setResult(Activity.RESULT_OK, resultIntent);

                    Toast.makeText(EditBookActivity.this, "Libro actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Log.e(TAG, "Error al actualizar el libro" + response.code());
                    Toast.makeText(EditBookActivity.this, "Error al actualizar libro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e(TAG, "Error en la llamada: " + t.getMessage());
                Toast.makeText(EditBookActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
