<template>
  <div>
    <h1 class="title">Bienvenue dans la Bibliothèque</h1>
    <div class="bar-container">
      <SearchBar @search-results="handleSearchResults"/>
      <SearchAdvancedBar @search-advanced-results="handleSearchAdvancedResults"/>
    </div>
    <ListBook :books="books"/>
  </div>
</template>

<script>
import SearchBar from "@/components/SearchBar.vue";
import ListBook from "@/components/ListBook.vue";
import SearchAdvancedBar from "@/components/SearchAdvancedBar.vue";

export default {
  name: "Home",
  components: {SearchAdvancedBar, SearchBar, ListBook},
  data() {
    return {
      books: [], // Liste des livres
    };
  },
  methods: {
    handleSearchResults(results) {
      // Redirige vers la page des résultats de recherche avec les données
      this.$router.push({name: "Search", query: {results: JSON.stringify(results)}});
    },
    handleSearchAdvancedResults(results) {
      // Redirige vers la page des résultats de recherche avec les données
      this.$router.push({name: "SearchAdvanced", query: {results: JSON.stringify(results)}});
    }
  },
  async created() {
    // Récupère la liste des livres au chargement
    try {
      const response = await fetch("/api/livres");
      this.books = await response.json();
    } catch (error) {
      console.error("Erreur lors du chargement des livres :", error);
    }
  },
};
</script>

<style scoped>
.bar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.title {
  font-size: 3rem;
  font-weight: bold;
  text-align: left;
  color: #fff;
  background: linear-gradient(90deg, #36b138, #9765cd 70%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 20px;
  letter-spacing: 2px;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>
