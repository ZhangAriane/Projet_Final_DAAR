<template>
  <div>
    <h1 class="title">{{ title }}</h1>
    <div class="bar-container">
      <SearchBar @search-results="handleSearchResults"/>
      <SearchAdvancedBar @search-advanced-results="handleSearchAdvancedResults"/>
    </div>
    <div v-if="isVisible" class="result-count">
      Nombre de résultats : <span class="number">{{ number }}</span>
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
      title: '',
      number: 0,
      isVisible: false
    };
  },
  methods: {
    handleSearchResults(results) {
      this.title = "Résultats de Recherche";
      this.books = results;
      this.number = this.books.length;
      this.isVisible = true;
    },
    handleSearchAdvancedResults(results) {
      this.title = "Résultats de Recherche avancée";
      this.books = results;
      this.number = this.books.length;
      this.isVisible = true;
    }
  },
  async created() {
    // Récupère la liste des livres au chargement
    try {
      this.title = "Bienvenue dans la Bibliothèque";
      const response = await fetch("/api/livres");
      this.books = await response.json();
    } catch (error) {
      console.error("Erreur lors du chargement des livres :", error);
    }
  }
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
  text-align: center;
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

.result-count {
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
  background: #f9f9f9;
  border: 2px solid #36b138;
  border-radius: 10px;
  padding: 10px 15px;
  margin: 20px auto;
  text-align: center;
  width: 50%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.number {
  color: #36b138;
  font-size: 1.5rem;
}
</style>