<template lang="html">
    <div class="container">
        <div class="row">
            <div class="col text-left">
                <h1>Edit Book</h1>
            </div>
        </div>
        <div class="row">
            <div class="col text-left">
                <div class="card">
                    <div class="card-body">
                        <form @submit="onSubmit">
                            <div class="form-group row">
                                <label for="title" class="col-sm-2 col-form-label">Title</label>
                                <div class="col-sm-10">
                                    <input type="text" placeholder="Title" class="form-control" name="title" v-model="form.title">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="author" class="col-sm-2 col-form-label">Autor</label>
                                <div class="col-sm-10">
                                    <input type="text" placeholder="Author" class="form-control" name="author" v-model="form.author">
                                </div>
                            </div>
                             <div class="form-group row">
                                <label for="description" class="col-sm-2 col-form-label">Description</label>
                                <div class="col-sm-10">
                                    <input type="text" placeholder="Description" class="form-control" name="description" v-model="form.description">
                                </div>
                            </div>
                            <div class="rows">
                                <div class="col text-left">
                                    <b-button type="submit" variant="primary">Editar</b-button>
                                    <b-button type="submit" class="btn-large-space" :to="{  name:'ListBook' }">Cancelar</b-button>
                                </div>
                            </div>
                           
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>  
</template>

<script>
import axios from 'axios';
import swal from 'sweetalert';

export default {
    data () {
        return {
            bookID: this.$route.params.bookID,
            form: {
                title: '',
                author: '',
                description: ''
            }
        }
    },
    methods: {
        onSubmit (evt) {
            evt.preventDefault()
            const path = `http://127.0.0.1:8000/book/books/${this.bookID}/`
            axios.put(path, this.form)
            .then((response) => {
                this.form.title = response.data.title
                this.form.author = response.data.author
                this.form.description = response.data.description
                swal("Libro Actualizado", "Libro Actualizado Correctamente", "success");
                this.$router.push({name: 'ListBook'})
            })
            .catch((error) => {
                console.log(error)
            })
        },
    
        getBook () {
            const path = `http://127.0.0.1:8000/book/books/${this.bookID}/`
            axios.get(path).then((res) => {
                this.form.title = res.data.title
                this.form.author = res.data.author
                this.form.description = res.data.description
            })
            .catch((error) => {
                console.log(error)
            })
        }
    },
    created () {
        this.getBook()
    }
}
</script>

<style lang="css">
</style>
