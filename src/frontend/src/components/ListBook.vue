<template>
  <div class="listeBook">
    <Book
        v-for="book in books"
        :key="book.id"
        :name="book.title"
        :authors="book.authors"
        :image="book.image"
        :txt="book.txt"
    />
  </div>
</template>

<script>
import Book from "@/components/Book.vue";

export default {
  name: "ListBook",
  components: { Book },
  props: {
    books: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.listeBook {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}
</style>
