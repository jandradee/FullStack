<template lang="html">
    <div class="container">
        <div class="row">
            <div class="col text-left">
                <h2>¿Está seguro que desea eliminar el libro?</h2>
                <p>Titulo : {{ this.element.title }}</p>
                <p>Autor : {{ this.element.author }}</p>
                <p>Descripcion : {{ this.element.description }}</p>
            </div>
        </div>
        <div class="row">
            <div class="col text-left">
                <b-button  v-on:click="DeleteBook" variant="danger">Eliminar</b-button>
                <b-button type="submit" class="btn-large-space" :to="{  name:'ListBook' }">Cancelar</b-button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import swal from 'sweetalert';
export default {
    data() {
        return {
            bookID: this.$route.params.bookID,
            element: {
                title: '',
                author: '',
                description: ''
            }
        }
    },
    methods: {
        getBook () {
            const path = `http://127.0.0.1:8000/book/books/${this.bookID}/`
            axios.get(path).then((res) => {
                this.element.title = res.data.title
                this.element.author = res.data.author
                this.element.description = res.data.description

            })
            .catch((error) => {
                console.log(error)
            })
        },
        DeleteBook () {
            const path = `http://127.0.0.1:8000/book/books/${this.bookID}/`
            axios.delete(path).then(() => {
                swal("Libro Eliminado", "Libro Eliminado Correctamente", "success");
                this.$router.push({name: 'ListBook'})
            })
                .catch((error) => {
                    console.log(error)
            })
        }
    },
    created() {
        this.getBook()
    }
}
</script>

<style lang="css">
</style>