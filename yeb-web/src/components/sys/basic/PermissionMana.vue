<template>
  <div>
    <div class="permissionTool">
      <el-input placeholder="请输入角色英文名" v-model="role.name">
        <!--输入框前缀-->
        <template slot="prepend">ROLE_</template>
      </el-input>
      <el-input placeholder="请输入角色中文名" v-model="role.nameZh" @keydown.enter.native="addRole"></el-input>
      <el-button type="primary" icon="el-icon-plus" @click="addRole">添加角色</el-button>
    </div>
    <div class="permissionMain">
      <!--折叠板-->
      <el-collapse v-model="activeName" accordion @change="change">
        <el-collapse-item :title="r.nameZh" :name="r.id" v-for="(r, index) in roles" :key="index">
          <!--卡片-->
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>可访问资源</span>
              <el-button style="float: right; padding: 3px 0; color: red" type="text" icon="el-icon-delete"
                         @click="deleteRole(r)"></el-button>
            </div>
            <div>
              <!--树形控件-->
              <el-tree show-checkbox ref="tree" :key="index" :data="allMenus" :props="defaultProps"
                       :default-checked-keys="selectedMenus"
                       node-key="id"></el-tree>
              <div style="display: flex; justify-content: flex-end">
                <el-button size="mini" @click="cancelUpdate">取消修改</el-button>
                <el-button size="mini" type="primary" @click="updatePermission(r.id, index)">确认修改</el-button>
              </div>
            </div>
          </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>

export default {
  name: "PermissionMana",
  data() {
    return {
      role: {
        name: '',
        nameZh: ''
      },
      roles: [],
      allMenus: [],
      defaultProps: { //别名，匹配后端的属性名
        children: 'children',
        label: 'name'
      },
      selectedMenus: [],
      activeName: -1
    }
  },
  mounted() { //生命周期函数
    this.initRoles();
  },
  methods: {
    addRole() {
      if (this.role.name && this.role.nameZh) {
        this.postRequest('/system/basic/permission/role', this.role).then(resp => {
          if (resp) {
            this.initRoles();
            this.role.name = '';
            this.role.nameZh = '';
          }
        })
      } else {
        this.$message.error('角色名不能为空！');
      }
    },
    initRoles() {
      this.getRequest('/system/basic/permission/').then(resp => {
        if (resp) {
          this.roles = resp;
        }
      })
    },
    initAllMenus() {
      this.getRequest('/system/basic/permission/menus').then(resp => {
        if (resp) {
          this.allMenus = resp;
        }
      })
    },
    change(rid) {
      if (rid) {
        this.initAllMenus();
        this.initSelectedMenus(rid);
      }
    },
    initSelectedMenus(rid) {
      this.getRequest('/system/basic/permission/mid/' + rid).then(resp => {
        if (resp) {
          this.selectedMenus = resp;
        }
      })
    },
    deleteRole(role) {
      this.$confirm('此操作将永久删除[' + role.nameZh + ']角色，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/permission/role/' + role.id).then(resp => {
          if (resp) {
            this.initRoles();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除！！'
        });
      });
    },
    updatePermission(rid, index) {
      let tree = this.$refs.tree[index];
      //获取选中的节点
      let selectedKeys = tree.getCheckedKeys(true); //true表示只返回选中的子节点的keys（不返回一级菜单的keys）
      let url = '/system/basic/permission/?rid=' + rid;
      selectedKeys.forEach(key => {
        url += '&mids=' + key;
      });
      this.putRequest(url).then(resp => {
        if (resp) {
          this.activeName = -1; //关闭折叠板
        }
      })
    },
    cancelUpdate() {
      this.activeName = -1;
    }
  }
}
</script>

<style>

.permissionTool {
  display: flex;
  justify-content: flex-start;
}

.permissionTool .el-input {
  width: 300px;
  margin-right: 6px;
}

.permissionMain {
  margin-top: 10px;
  width: 700px;
}

</style>