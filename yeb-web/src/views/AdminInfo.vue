<template>
  <div>
    <el-card class="box-card" style="width: 400px;">
      <div slot="header" class="clearfix">
        <span>{{ admin.name }}</span>
      </div>
      <div>
        <div style="display: flex; justify-content: center;">
          <el-upload action="/admin/userface" :headers="headers" :data="admin" :on-success="onSuccess"
                     :show-file-list="false">
            <img title="点击修改用户头像" :src="admin.userFace" style="height: 100px; width: 100px; border-radius: 50px;"
                 alt="">
          </el-upload>

        </div>
        <div>
          电话号码
          <el-tag>{{ admin.telephone }}</el-tag>
        </div>
        <div>
          手机号码
          <el-tag>{{ admin.phone }}</el-tag>
        </div>
        <div>
          联系地址
          <el-tag>{{ admin.address }}</el-tag>
        </div>
        <div>
          用户角色
          <el-tag type="success" v-for="(r, index) in admin.roles" :key="index">{{ r.nameZh }}</el-tag>
        </div>
        <div style="display: flex; justify-content: space-around; margin-top: 10px;">
          <el-button size="mini" type="primary" @click="showAdminView">修改信息</el-button>
          <el-button size="mini" type="danger" @click="showPasswordView">修改密码</el-button>
        </div>
      </div>
    </el-card>
    <el-dialog title="编辑用户信息" :visible.sync="dialogVisible" width="30%">
      <table>
        <tr>
          <td>
            <el-tag>用户昵称</el-tag>
          </td>
          <td>
            <el-input v-model="updatedAdmin.name" placeholder="请输入用户昵称..."></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>电话号码</el-tag>
          </td>
          <td>
            <el-input v-model="updatedAdmin.telephone" placeholder="请输入电话号码..."></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>手机号码</el-tag>
          </td>
          <td>
            <el-input v-model="updatedAdmin.phone" placeholder="请输入手机号码..."></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>联系地址</el-tag>
          </td>
          <td>
            <el-input v-model="updatedAdmin.address" placeholder="请输入联系地址..."></el-input>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateAdmin">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="修改密码" :visible.sync="passwordDialogVisible" width="30%">
      <div>
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px">
          <el-form-item label="旧密码" prop="oldPass">
            <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="pass">
            <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "AdminInfo",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      admin: null,
      updatedAdmin: null,
      dialogVisible: false,
      passwordDialogVisible: false,
      ruleForm: {
        pass: '',
        checkPass: '',
        oldPass: ''
      },
      rules: {
        oldPass: [
          {validator: validatePass, trigger: 'blur'}
        ],
        pass: [
          {validator: validatePass, trigger: 'blur'}
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'blur'}
        ]
      },
      headers: {
        Authorization: window.sessionStorage.getItem('tokenStr')
      }
    };
  },
  mounted() {
    this.initAdmin();
  }
  ,
  methods: {
    initAdmin() {
      this.getRequest('/admin/info').then(resp => {
        if (resp) {
          this.admin = resp;
          this.updatedAdmin = Object.assign({}, this.admin);
          window.sessionStorage.setItem('user', JSON.stringify(resp));
          this.$store.commit('initAdmin', resp);
        }
      })
    }
    ,
    showAdminView() {
      this.dialogVisible = true;
    }
    ,
    showPasswordView() {
      this.passwordDialogVisible = true;
    }
    ,
    updateAdmin() {
      this.putRequest('/admin/info', this.updatedAdmin).then(resp => {
        if (resp) {
          this.initAdmin();
          this.dialogVisible = false;
        }
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.ruleForm.adminId = this.admin.id;
          this.putRequest('/admin/pass', this.ruleForm).then(resp => {
            if (resp) {
              //注销登录
              this.postRequest('/logout');
              window.sessionStorage.removeItem('user');
              window.sessionStorage.removeItem('tokenStr');
              this.$store.commit('initRoutes', []);
              this.$router.replace('/');
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    onSuccess() {
      this.initAdmin();
    }
  }
}
</script>

<style>

</style>