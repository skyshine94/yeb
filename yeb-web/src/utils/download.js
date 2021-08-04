import axios from "axios"

//请求下载工具类

const service = axios.create({
    responseType: 'arraybuffer'
})

//请求拦截器
service.interceptors.request.use(config => {
    config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr');
    return config;
}, error => {
    console.log(error);
});

//响应拦截器
service.interceptors.response.use(resp => {
    const headers = resp.headers;
    let reg = RegExp(/application\/json/);
    if (headers['content-type'].match(reg)) {
        resp.data = unitToString(resp.data);
    } else {
        let fileDownload = require('js-file-download');
        let fileName = headers['content-disposition'].split(';')[1].split('filename=')[1];
        let contentType = headers['content-type'];
        fileName = decodeURIComponent(fileName);
        fileDownload(resp.data, fileName, contentType);
    }
}, error => {
    console.log(error);
});

function unitToString(unitArray) {
    let encodedString = String.fromCharCode.apply(null, new Uint8Array(unitArray));
    let decodedString = decodeURIComponent(escape(encodedString));
    return JSON.parse(decodedString);
}

let base = '';

export const downloadRequest = (url, params) => {
    return service({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}

export default service;