import Vue from "vue";
import Router from "vue-router";
import Home from "@/views/Home.vue";

Vue.use(Router);

export default new Router({
    mode: "hash", // URLs propres sans #
    routes: [
        {
            path: "/",
            name: "Home",
            component: Home // Page d'accueil
        }
    ],
});
