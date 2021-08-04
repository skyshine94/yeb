import {getRequest} from "@/utils/api"; //单独引入getRequest

//初始化菜单工具类
export const initMenu = (router, store) => {
    //有数据则不需要初始化
    if (store.state.routes.length > 0) {
        return;
    }
    getRequest('/system/cfg/menu').then(data => {
        if (data) {
            //格式化router
            let fmtRoutes = formatRoutes(data);
            //添加到router
            router.addRoutes(fmtRoutes);
            //将数据存入vuex
            store.commit('initRoutes', fmtRoutes);
        }
    })
}

export const formatRoutes = (routes) => {
    let fmtRoutes = [];
    routes.forEach(router => {
        let {
            path,
            component,
            name,
            iconCls,
            children
        } = router;
        //判断子菜单是否存在且是一个数组
        if (children && children instanceof Array) {
            //递归
            children = formatRoutes(children);
        }
        //格式化component，将字符串数据转换成对应组件
        let fmtRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            children: children,
            component(resolve) {
                if (component.startsWith('Home')) {
                    require(['../views/' + component + '.vue'], resolve); //使用懒加载的方式加载组件
                } else if (component.startsWith('Emp')) {
                    require(['../views/emp/' + component + '.vue'], resolve);
                } else if (component.startsWith('Per')) {
                    require(['../views/per/' + component + '.vue'], resolve);
                } else if (component.startsWith('Sal')) {
                    require(['../views/sal/' + component + '.vue'], resolve);
                } else if (component.startsWith('Sta')) {
                    require(['../views/sta/' + component + '.vue'], resolve);
                } else if (component.startsWith('Sys')) {
                    require(['../views/sys/' + component + '.vue'], resolve);
                }
            }
        }
        //将数据存入数组中
        fmtRoutes.push(fmtRouter);
    });
    return fmtRoutes;
}
