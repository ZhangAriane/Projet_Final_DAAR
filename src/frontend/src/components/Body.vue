<script>

import ListBook from "@/components/ListBook.vue";

export default {
  name: "Body",
  components: {ListBook, }
};
</script>

<template>
  <div class="corps">
    <h3 class="title">Bibliothèque</h3>
    <ListBook/>
  </div>
</template>

<style scoped>
.corps {
  max-width: 100%;
  margin: 0 auto;
  background-color: rgb(233, 233, 233);
  padding: 30px;
}

.title {
  font-size: 2.5rem;
  font-weight: bold;
  text-transform: uppercase;
  text-align: center;
  color: #fff;
  background: linear-gradient(90deg, #ff7a18, #af002d 70%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 3px;
  padding: 10px 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: rgba(255, 122, 24, 0.2);
  display: inline-block;
  transition: transform 0.3s ease;
}

.title:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}

</style>
