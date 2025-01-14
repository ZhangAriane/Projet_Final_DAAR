import Vue from "vue";
import Router from "vue-router";
import Home from "@/views/Home.vue";
import Search from "@/views/Search.vue";
import SearchAdvanced from "@/views/SearchAdvanced.vue";

Vue.use(Router);

export default new Router({
    mode: "history", // URLs propres sans #
    routes: [
        {
            path: "/",
            name: "Home",
            component: Home, // Page d'accueil
        },
        {
            path: "/search",
            name: "Search",
            component: Search, // Résultats de recherche
        },
        {
            path: "/searchAdvanced",
            name: "SearchAdvanced",
            component: SearchAdvanced, // Résultats de recherche
        },
    ],
});
