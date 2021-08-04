<template>
  <div>
    <!--element ui布局容器-->
    <el-container>
      <el-header class="homeHeader">
        <div class="title">云E办</div>
        <div>
          <el-dropdown class="userInfo" @command="commandHandler">
          <span class="el-dropdown-link">
            {{ user.name }}<i><img :src="user.userFace"></i>
          </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="adminInfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="setting">设置</el-dropdown-item>
              <el-dropdown-item command="logout">注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <!--启动路由模式，禁用多选展开-->
          <el-menu router unique-opened>
            <!--循环获取路由-->
            <el-submenu :index="index + ''" v-for="(item, index) in routes" :key="index">
              <template slot="title"><i :class="item.iconCls" style="color: cornflowerblue;margin-right: 5px"></i>
                <span>{{ item.name }}</span>
              </template>
              <!--循环children数组-->
              <el-menu-item-group>
                <el-menu-item :index="children.path" v-for="(children, index1) in item.children" :key="index1">
                  {{ children.name }}
                </el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <!--面包屑效果-->
          <el-breadcrumb separator-class="el-icon-arrow-right" v-if="this.$router.currentRoute.path != '/home'">
            <el-breadcrumb-item :to="{path: '/home'}">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ this.$router.currentRoute.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div class="homeWelcome" v-else>
            欢迎使用云E办系统！
          </div>
          <router-view class="homeRouterView"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Home",
  methods: {
    commandHandler(command) {
      if (command == 'logout') {
        this.$confirm('是否要注销登录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //注销登录
          this.postRequest('/logout');
          //清空用户信息
          window.sessionStorage.removeItem('tokenStr');
          window.sessionStorage.removeItem('user');
          //清空菜单
          this.$store.commit('initRoutes', [])
          //跳转到登录页
          this.$router.replace('/');
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作！'
          });
        });
      }
      if (command == 'adminInfo'){
        this.$router.push('/adminInfo');
      }
    }
  },
  computed: {
    routes() {
      return this.$store.state.routes;
    },
    user(){
      return this.$store.state.currentAdmin;
    }
  }
}
</script>

<style>
.homeHeader {
  background: dodgerblue;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 15px;
  box-sizing: border-box;
}

.homeHeader .title {
  font-size: 30px;
  font-family: 华文行楷;
  color: white;
}

.homeHeader .userInfo {
  cursor: pointer;
}

.el-dropdown-link img {
  width: 48px;
  height: 48px;
  border-radius: 24px;
  margin-left: 8px;
}

.homeWelcome {
  text-align: center;
  font-size: 30px;
  font-family: 华文楷体;
  color: dodgerblue;
  padding-top: 50px;
}

.homeRouterView {
  margin-top: 10px;
}
</style>