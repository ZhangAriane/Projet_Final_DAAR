import Vue from "vue";
import App from "./App.vue";
import router from "./router";

Vue.config.productionTip = false;

new Vue({
    router, // Injecte Vue Router dans l'application
    render: (h) => h(App)
}).$mount("#app");
