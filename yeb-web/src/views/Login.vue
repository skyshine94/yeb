<template>
  <div>
    <el-form :rules="rules"
             ref="loginForm"
             :model="loginForm"
             class="loginContainer"
             v-loading="loading"
             element-loading-text="正在登录..."
             element-loading-spinner="el-icon-loading"
             element-loading-background="rgba(0, 0, 0, 0.8)"> <!--加载动画-->
      <h3 class="loginTitle">系统登录</h3>
      <!--校验规则的参数对应prop属性中的参数-->
      <el-form-item prop="username">
        <el-input type="text" auto-complete="false" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" auto-complete="false" v-model="loginForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input type="text" auto-complete="false" v-model="loginForm.code" @keydown.enter.native="submitLogin"
                  placeholder="点击图片更换验证码"
                  style="width: 250px;margin-right: 5px"></el-input>
        <img :src="captchaUrl" @click="updateCaptcha">
      </el-form-item>
      <el-checkbox v-model="checked" class="loginRemember">记住我</el-checkbox>
      <el-button type="primary" style="width: 100%;" @click="submitLogin">登录</el-button>
    </el-form>
  </div>
</template>

<script>

export default {
  name: "Login",
  data() {
    return {
      captchaUrl: '/captcha?time=' + new Date(),
      loginForm: {
        username: 'admin',
        password: '123',
        code: ''
      },
      loading: false,
      checked: true,
      rules: { //校验规则，标签中加入prop属性
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}], //不为空，失去焦点时触发提示信息。
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
        code: [{required: true, message: '请输入验证码', trigger: 'blur'}]
      }
    }
  },
  methods: {
    updateCaptcha() {
      this.captchaUrl = '/captcha?time=' + new Date();
    },
    submitLogin() {
      //校验失败时不提交。
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true; //显示加载动画
          this.postRequest('/login', this.loginForm).then(resp => {
            this.loading = false; //禁用加载动画
            if (resp) {
              //存储用户token
              const tokenStr = resp.obj.tokenHead + resp.obj.token;
              //将token存入sessionStorage中，在请求拦截器中读取token
              window.sessionStorage.setItem('tokenStr', tokenStr);
              //未登录用户输入的重定向路径
              let path = this.$route.query.redirect;
              //如果用户输入的不是登录页地址，登录后直接跳转到用户输入的地址
              this.$router.replace((path == '/' || path == undefined) ? '/home' : path); //使用push支持浏览器的后退按钮，使用replace不支持。
            }
          })
        } else {
          this.$message.error('请输入所有字段');
          return false;
        }
      });
    }
  }
}
</script>

<style>
.loginContainer {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 15px 35px 15px 35px;
  background: #fff;
  border: 1px solid #aaa;
  box-shadow: 0 0 25px #ccc;
}

.loginTitle {
  margin: 0px auto 40px auto;
  text-align: center;
}

.loginRemember {
  text-align: left;
  margin: 0px 0px 15px 0px;
}

.el-form-item__content {
  display: flex;
  align-items: center;
}
</style>