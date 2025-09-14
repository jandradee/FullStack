package com.example.app;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookApiService {
    @GET("book/books/")
    Call<List<Book>> getBooks();

    @GET("book/books/{id}/")
    Call<Book> getBook(@Path("id") int id);

    @POST("book/books/")
    Call<Book> createBook(@Body Book book);

    @PUT("book/books/{id}/")
    Call<Book> updateBook(@Path("id") int id, @Body Book book);

    @DELETE("book/books/{id}/")
    Call<Void> deleteBook(@Path("id") int id);
}
