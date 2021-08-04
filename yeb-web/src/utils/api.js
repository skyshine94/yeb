import axios from "axios"
import {Message} from 'element-ui' //js文件中不能直接使用this.$message，需要单独引入Message
import router from "@/router";

//axios工具类
//请求拦截器
axios.interceptors.request.use(config => {
    //如果存在token，请求携带这个token。
    if (window.sessionStorage.getItem('tokenStr')) {
        config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr');
    }
    return config;
}, error => {
    console.log(error);
});

//响应拦截器
axios.interceptors.response.use(success => { //成功访问后端接口
    if (success.status && success.status == 200) {
        //判断业务逻辑错误
        if (success.data.code == 500 || success.data.code == 401 || success.data.code == 403) {
            Message.error({message: success.data.message});
            return;
        }
        if (success.data.message) {
            Message.success({message: success.data.message});
        }
    }
    return success.data;
}, error => {
    if (error.response.code == 504 || error.response.code == 404) {
        Message.error({message: '访问失败！'});
    } else if (error.response.code == 403) {
        Message.error({message: '权限不足！'});
    } else if (error.response.code == 401) {
        Message.error({message: '请先登录！'});
        router.replace('/'); //路由到登录页面
    } else {
        if (error.response.data.message) {
            Message.error({message: error.response.data.message})
        } else {
            Message.error({message: '未知错误！'})
        }
    }
    return;
});

let base = ''; //前置路径，方便以后修改路径

//在main.js中引入组件
//发送json格式的post请求。
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params
    })
}

//发送json格式的get请求。
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}

//发送json格式的put请求。
export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
}

//发送json格式的delete请求。
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        data: params
    })
}