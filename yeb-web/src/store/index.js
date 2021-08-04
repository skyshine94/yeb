import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const now = new Date();

//vuex状态管理
const store = new Vuex.Store({
    //全局对象
    state: {
        routes: [],
        currentAdmin: JSON.parse(window.sessionStorage.getItem('user'))
    },
    //用于改变state对象（同步执行）
    mutations: {
        initRoutes(state, data) {
            state.routes = data;
        },
        initAdmin(state, admin){
            state.currentAdmin = admin;
        }
    },
    //用于改变state对象（异步执行）
    actions: {}
})

export default store;