import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login'
import Home from '../views/Home'
import AdminInfo from '../views/AdminInfo'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login,
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        children: [
            {
                path: '/adminInfo',
                name: '个人中心',
                component: AdminInfo
            }
        ]
    }
]

const router = new VueRouter({
    routes
})

export default router
