let proxyObj = {}

//配置代理
proxyObj['/'] = {
    ws: false, //websocket
    target: 'http://localhost:8081',
    changeOrigin: true, //发送请求时请求头会被设置成target
    pathRewrite: { //不重写请求地址
        '^/': '/'
    }
}

module.exports = {
    devServer: {
        host: 'localhost',
        port: 8080,
        proxy: proxyObj //将发送到8080端口的请求通过代理发送到后端的8081端口
    }
}