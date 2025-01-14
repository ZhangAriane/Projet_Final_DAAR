<template>
  <div>
    <h1 class="title">Résultats de Recherche</h1>
    <SearchBar @search-results="handleSearchResults" />
    <ListBook :books="books" />
  </div>
</template>

<script>
import ListBook from "@/components/ListBook.vue";
import SearchBar from "@/components/SearchBar.vue";

export default {
  name: "Search",
  components: {SearchBar, ListBook},
  data() {
    return {
      books: [], // Résultats des recherches
    };
  },
  methods: {
    handleSearchResults(results) {
      this.books = results;
      // Met à jour l'URL pour persister les résultats
      this.$router.push({name: "Search", query: {results: JSON.stringify(results)}});
    },
  },
  created() {
    // Charger les résultats si disponibles dans l'URL
    if (this.$route.query.results) {
      try {
        this.books = JSON.parse(this.$route.query.results);
      } catch (error) {
        console.error("Erreur lors du chargement des résultats de recherche :", error);
      }
    }
  },
};
</script>

<style scoped>
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
