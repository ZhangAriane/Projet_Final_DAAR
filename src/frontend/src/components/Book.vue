<template>
  <div class="book" @click="openTxt">
    <img :src="image" alt="Image du livre" class="book-image"/>
    <h4 class="book-title">{{ name }}</h4>
    <p class="book-authors">{{ authors }}</p>
  </div>
</template>

<script>
export default {
  name: "Book",
  props: {
    name: {
      type: String,
      default: "Titre par défaut"
    },
    authors: {
      type: String,
      default: "Auteur inconnu"
    },
    image: {
      type: String,
      default: "https://via.placeholder.com/150"
    },
    txt: {
      type: String,
      default: "#" // Lien par défaut si aucun texte n'est fourni
    }
  },
  methods: {
    openTxt() {
      window.open(this.txt, "_blank"); // Ouvre le fichier texte dans un nouvel onglet
    }
  }
};
</script>


<style scoped>
.book {
  flex: 1 1 calc(25% - 10px); /* Chaque livre occupe 25% de la largeur (avec espacement) */
  max-width: 200px; /* Limite la largeur maximale du livre */
  max-height: 350px; /* Limite la hauteur maximale du livre */
  background-color: #f9f9f9; /* Fond clair et doux */
  margin-top: 10px;
  padding: 20px; /* Plus d'espace interne */
  transition: transform 0.3s ease, box-shadow 0.3s ease; /* Transition fluide pour le survol */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Ombre légère */
  border-radius: 10px; /* Coins plus arrondis */
  cursor: pointer;
  text-align: center;
  font-size: 1.1rem; /* Taille du texte légèrement agrandie */
  font-weight: 600; /* Texte en semi-gras */
  color: #333; /* Couleur du texte */
  position: relative; /* Nécessaire pour les effets supplémentaires */
}

.book:before {
  content: ""; /* Ajoute un effet d'accentuation */
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 10px; /* Coin arrondi synchronisé */
  background: linear-gradient(135deg, rgba(255, 182, 193, 0.3), rgba(173, 216, 230, 0.3)); /* Dégradé subtil */
  z-index: -1; /* Derrière le contenu */
  transition: opacity 0.3s ease;
  opacity: 0; /* Masqué par défaut */
}

.book:hover:before {
  opacity: 1; /* Affiche le dégradé au survol */
}

.book:hover {
  transform: translateY(-5px); /* Lève légèrement la carte */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); /* Ombre renforcée */
}

.book:active {
  transform: scale(0.95); /* Réduit légèrement au clic */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Réduction de l'ombre */
}

.book-image {
  width: 100%;
  max-height: 150px; /* Limite la hauteur maximale de l'image */
  object-fit: cover; /* Ajuste l'image pour qu'elle remplisse le conteneur */
  border-radius: 8px;
  margin-bottom: 15px;
}

.book-title {
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.book-authors {
  font-size: 0.9rem;
  color: #666;
  overflow: hidden; /* Masque le texte qui dépasse */
  text-overflow: ellipsis; /* Ajoute des points de suspension si le texte est trop long */
  white-space: nowrap; /* Empêche le retour à la ligne */
}
</style>
