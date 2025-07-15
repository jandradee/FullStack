import Vue from 'vue'
import Router from 'vue-router'
import ListBook from '@/components/Book/ListBook.vue'
import EditBook from '@/components/Book/EditBook.vue'
import DeleteBook from '@/components/Book/DeleteBook.vue'
import NewBook from '@/components/Book/NewBook.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'ListBook',
      component: ListBook
    },
    {
      path: '/books/:bookId/edit',
      name: 'EditBook',
      component: EditBook
    },
    {
      path: '/books/:bookId/delete',
      name: 'DeleteBook',
      component: DeleteBook
    },
    {
      path: '/books/new',
      name: 'NewBook',
      component: NewBook
    }
  ]
})
