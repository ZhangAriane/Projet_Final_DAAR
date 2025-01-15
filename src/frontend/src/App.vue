<template>
  <div id="app" class="app-container">
    <header class="app-header">
      <nav class="nav-bar">
        <router-link :to="{name: 'Home'}" class="nav-link" @click.native="refreshHome">Accueil</router-link>
      </nav>
    </header>
    <main class="app-main">
      <router-view/>
    </main>
    <footer class="app-footer">
      <p>&copy; 2025 Bibliothèque</p>
    </footer>
  </div>
</template>


<script>
export default {
  name: "App",
  methods: {
    refreshHome() {
      if (this.$route.name === 'Home') {
        this.$router.go(0); // Force le rafraîchissement de la page
      }
    }
  }
};
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(to bottom, #f5f5f5, #e0f7fa);
  font-family: 'Arial', sans-serif;
}

.app-header {
  background: linear-gradient(90deg, #9765cd, #81c784);
  color: white;
  padding: 15px;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.nav-bar {
  display: flex;
  justify-content: left;
  gap: 20px;
  font-size: 0.9rem;
}

.nav-link {
  text-decoration: none;
  color: white;
  font-size: 1.2rem;
  font-weight: bold;
  padding: 5px 10px;
  transition: background-color 0.3s, transform 0.3s;
  border-radius: 8px;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.app-main {
  flex: 1;
  padding: 30px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  background: linear-gradient(to bottom, #ffffff, #e8f5e9);
  box-shadow: inset 0 4px 10px rgba(0, 0, 0, 0.1);
}

.app-footer {
  background: linear-gradient(90deg, #81c784, #9765cd);
  color: white;
  text-align: center;
  padding: 10px;
  font-size: 1rem;
  box-shadow: 0 -4px 10px rgba(0, 0, 0, 0.1);
}

.app-footer p {
  margin: 0;
  font-weight: bold;
}
</style>
