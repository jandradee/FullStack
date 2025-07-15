<template lang= "html">
    <div class="container">
        <div class="row">
            <div class="col text-left">
                <h1>Book List</h1>
                <b-button size="sm" :to="{name:'NewBook'}">Add Book</b-button><br>
                <div class="col-md-12">
                    <b-table striped hover :items="books" :fields="fields">
                        <template v-slot:cell(actions)="data">   
                            <b-button size="sm"  variant="primary" :to="{ name:'EditBook', params: { bookID: data.item.id } }">Edit</b-button>
                            <b-button size="sm"  variant="danger" :to="{ name:'DeleteBook', params: { bookID: data.item.id } }">Eliminar</b-button>
                        </template>
                    </b-table>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    data () {
        return {
            fields: [
                { key: 'title', label: 'Title', sortable: true },
                { key: 'author', label: 'Author', sortable: true },
                { key: 'description', label: 'Description', sortable: true },
                { key: 'actions', label: 'Actions' }
            ],
            books: []
        }
    },
    methods: {
        getBooks () {
            const path = 'http://127.0.0.1:8000/book/books/'
            axios.get(path).then((res) => {
                this.books = res.data
            })
            .catch((error) => {
                console.log(error)
            })
        }
    },
    created () {
        this.getBooks()
    }
}
</script>

<style lang="css" scoped>
</style>