package com.example.app;

public interface OnBookActionListener {
    void onEditBook(Book book, int position);
    void onDeleteBook(Book book, int position);
}
