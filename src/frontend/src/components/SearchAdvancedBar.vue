<script>
import axios from "axios";

export default {
  name: "SearchAdvancedBar",
  data() {
    return {
      searchQuery: "", // Contient le texte saisi par l'utilisateur
      searching: false // Indique si une recherche est en cours
    };
  },
  methods: {
    async onSearch() {
      if (this.searchQuery.trim() === "") {
        console.warn("La requête de recherche est vide.");
        return;
      }

      this.searching = true; // Active l'indicateur de recherche

      try {
        const response = await axios.get(`/api/livres/advancedSearch/${encodeURIComponent(this.searchQuery)}`);
        console.log("Résultats de recherche :", response.data);
        this.$emit("search-advanced-results", response.data); // Émet les résultats au composant parent
      } catch (error) {
        console.error("Erreur lors de la recherche :", error);
        alert("Une erreur est survenue lors de la recherche. Vérifiez votre requête ou réessayez plus tard.");
      } finally {
        this.searching = false; // Désactive l'indicateur de recherche
      }
    }
  }
};
</script>

<template>
  <div class="search-bar">
    <input
        type="text"
        v-model="searchQuery"
        class="search-input"
        placeholder="Recherchez avec une expression régulière "
    />
    <button class="search-button" @click="onSearch">
      🔍
    </button>

    <div v-if="searching" class="searching-message">Recherche en cours...</div>
  </div>
</template>


<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 20px auto;
  width: 100%;
  max-width: 500px;
  background-color: #f9f9f9;
  padding: 10px;
  border-radius: 25px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  flex-direction: column;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 10px 15px;
  border-radius: 25px;
  font-size: 1rem;
}


.search-input::placeholder {
  color: #aaa;
}



.search-button {
  border: none;
  background-color: #6a11cb;
  color: white;
  padding: 10px 15px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 1.2rem;
  margin-left: 10px;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.search-button:hover {
  background-color: #2575fc;
  transform: scale(1.1);
}

.search-button:active {
  background-color: #6a11cb;
  transform: scale(0.95);
}

.searching-message {
  margin-top: 10px;
  font-size: 1rem;
  color: #555;
}
</style>