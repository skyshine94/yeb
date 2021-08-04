import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.css' //导入font-awesome图标模板

import {postRequest} from "./utils/api"
import {getRequest} from "./utils/api"
import {putRequest} from "./utils/api"
import {deleteRequest} from "./utils/api"
import {initMenu} from "./utils/menus";
import {downloadRequest} from "./utils/download";

Vue.config.productionTip = false //关闭浏览器控制台关于环境的提示
Vue.use(ElementUI, {size: 'small'});

//以插件形式使用请求
Vue.prototype.postRequest = postRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.downloadRequest = downloadRequest;

//全局前置路由导航守卫，to表示目标路由，from表示来离开的路由，调用next方法resolve钩子，跳转目标路由
router.beforeEach((to, from, next) => {
    //判断用户是否登录
    if (window.sessionStorage.getItem('tokenStr')) {
        initMenu(router, store);
        //判断用户信息是否存在
        if (!window.sessionStorage.getItem('user')) {
            return getRequest('/admin/info').then(resp => {
                if (resp) {
                    //存入用户信息
                    window.sessionStorage.setItem('user', JSON.stringify(resp)); //转为字符串
                    //用户信息更新时调用store中的方法
                    store.commit('initAdmin', resp);
                    next();
                }
            })
        }
        next();
    } else {
        //如果目标路由是登录页，直接跳转
        if (to.path == '/') {
            next();
        } else {
            //重定向到登录页
            next('/?redirect=' + to.path);
        }
    }
})

new Vue({
    router,
    store,
    render: h => h(App) //渲染App组件中的内容
}).$mount('#app')
