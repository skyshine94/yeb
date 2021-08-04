<template>
  <div>
    <div style="display: flex; justify-content: center; margin-top: 10px">
      <el-input v-model="keywords" placeholder="通过用户名搜索用户..." prefix-icon="el-icon-search"
                style="width: 400px;margin-right: 10px;"></el-input>
      <el-button type="primary" icon="el-icon-search" @click="searchAdmin">搜索</el-button>
    </div>
    <div class="admin-container">
      <el-card class="admin-card" v-for="(admin, index) in admins" :key="index">
        <div slot="header" class="clearfix">
          <span>{{ admin.name }}</span>
          <el-button style="float: right; padding: 3px 0; color: red" type="text" icon="el-icon-delete"
                     @click="deleteAdmin(admin)"></el-button>
        </div>
        <div>
          <div style="width: 100%; display: flex; justify-content: center">
            <img :src="admin.userFace" :alt="admin.name" :title="admin.name" class="userFace-img">
          </div>
          <div style="font-size: 12px; color: blue">
            <div>用户名：{{ admin.name }}</div>
            <div>手机号码：{{ admin.phone }}</div>
            <div>电话号码：{{ admin.telephone }}</div>
            <div>地址：{{ admin.address }}</div>
            <div>
              用户状态：
              <el-switch v-model="admin.enabled" active-text="已启用" inactive-text="未启用"
                         @change="enabledChange(admin)"></el-switch>
            </div>
            <div>
              用户角色：
              <el-tag type="success" v-for="(role, index1) in admin.roles" :key="index1"
                      style="margin-right: 4px;">
                {{ role.nameZh }}
              </el-tag>
              <!--弹出框-->
              <el-popover placement="right" title="角色列表" width="200" trigger="click" @show="showPop(admin)"
                          @hide="hidePop(admin)">
                <el-select v-model="selectedRoles" multiple placeholder="请选择...">
                  <el-option v-for="(r, index) in roles" :key="index" :label="r.nameZh" :value="r.id"></el-option>
                </el-select>
                <el-button slot="reference" type="text" icon="el-icon-more"></el-button>
              </el-popover>
            </div>
            <div>备注：{{ admin.remark }}</div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "SysAdmin",
  data() {
    return {
      admins: [],
      keywords: '',
      roles: [],
      selectedRoles: []
    }
  },
  mounted() {
    this.initAdmins();
  },
  methods: {
    initAdmins() {
      this.getRequest('/system/admin/?keywords=' + this.keywords).then(resp => {
        if (resp) {
          this.admins = resp;
        }
      })
    },
    searchAdmin() {
      this.initAdmins();
    },
    deleteAdmin(admin) {
      this.$confirm('此操作将永久删除[' + admin.name + ']操作员，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest(' /system/admin/' + admin.id).then(resp => {
          if (resp) {
            this.initAdmins();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除！！'
        });
      });
    },
    enabledChange(admin) {
      this.putRequest(' /system/admin/', admin).then(resp => {
        if (resp) {
          this.initAdmins();
        }
      })
    },
    initRoles() {
      this.getRequest('/system/admin/role').then(resp => {
        if (resp) {
          this.roles = resp;
        }
      })
    },
    showPop(admin) {
      this.initRoles();
      let roles = admin.roles;
      this.selectedRoles = [];
      roles.forEach(r => {
        this.selectedRoles.push(r.id);
      })
    },
    hidePop(admin) {
      //未更改数据时不执行数据库操作
      let roles = [];
      //拷贝原数据
      Object.assign(roles, admin.roles);
      let flag = false;
      if (roles.length != this.selectedRoles.length) {
        flag = true;
      } else {
        //原数据与选中数据的个数相同
        for (let i = 0; i < roles.length; i++) {
          let role = roles[i];
          for (let j = 0; j < this.selectedRoles.length; j++) {
            let sr = this.selectedRoles[j];
            if (role.id == sr) {
              //原数据与选中数据的id相同则删除该数据
              roles.splice(i, 1);
              //删除数据后，数组中下一个元素会前移，下标也需要前移
              i--;
              break;
            }
          }
        }
        //原数据不为0说明更改了数据
        if (roles.length != 0) {
          flag = true;
        }
      }
      if (flag) {
        let url = '/system/admin/role?adminId=' + admin.id;
        this.selectedRoles.forEach(sr => {
          url += '&rids=' + sr;
        });
        this.putRequest(url).then(resp => {
          if (resp) {
            this.initAdmins();
          }
        })
      }
    }
  }
}
</script>

<style>

.admin-card {
  width: 350px;
  margin-bottom: 20px;
}

.admin-container {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin-top: 10px;
}

.userFace-img {
  width: 72px;
  height: 72px;
  border-radius: 72px;
}

</style>