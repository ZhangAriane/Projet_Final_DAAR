<script>
import axios from "axios";
import Book from "@/components/Book.vue";
import SearchBar from "@/components/SearchBar.vue";
import SearchAdvancedBar from "@/components/SearchAdvancedBar.vue";

export default {
  name: "ListBook",
  components: {SearchAdvancedBar, SearchBar, Book},
  data: () => {
    return {
      books: [], // Initialisation à une liste vide
      error: null, // Pour gérer les erreurs
      loading: true // Indicateur de chargement
    };
  },
  methods: {
    async fetchBooks() {
      try {
        this.loading = true;
        const response = await axios.get("/api/livres");
        this.books = response.data.map(book => ({
          id: book.id,
          name: book.title,
          authors: book.authors,
          image: book.image,
          txt: book.txt
        }));
      } catch (error) {
        console.error("Error fetching books:", error);
        this.error = "Une erreur s'est produite lors du chargement des livres.";
      } finally {
        this.loading = false;
      }
    },
    handleSearchResults(searchResults) {
      this.books = searchResults; // Met à jour les livres avec les résultats de recherche
    },
    handleSearchAdvancedResults(searchAdvancedResults) {
      this.books = searchAdvancedResults; // Met à jour les livres avec les résultats de recherche
    }
  },
  mounted() {
    this.fetchBooks(); // Appelle la méthode pour récupérer les livres au montage du composant
  }
};
</script>

<template>
  <div>
    <div class="bar-container">
      <SearchBar @search-results="handleSearchResults" />
      <SearchAdvancedBar @search-advanced-results="handleSearchAdvancedResults"/>
    </div>

    <div v-if="loading" class="loading">Chargement des livres...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="listeBook">
      <Book
          v-for="(book) in books"
          :key="book.id"
          :name="book.name"
          :authors="book.authors"
          :image="book.image"
          :txt="book.txt"
      />
    </div>
  </div>
</template>

<style scoped>
.bar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}


.listeBook {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.loading {
  text-align: center;
  font-size: 1.5rem;
  color: #888;
}

.error {
  text-align: center;
  font-size: 1.5rem;
  color: red;
}
</style>
