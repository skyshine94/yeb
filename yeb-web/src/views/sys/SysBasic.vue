<template>
  <div>
    <el-tabs v-model="activeName" type="card"> <!--绑定默认激活的组件-->
      <el-tab-pane label="部门管理" name="DepMana">
        <DepMana></DepMana>
      </el-tab-pane>
      <el-tab-pane label="职位管理" name="PosMana">
        <PosMana></PosMana>
      </el-tab-pane>
      <el-tab-pane label="职称管理" name="JoblevelMana">
        <JoblevelMana></JoblevelMana>
      </el-tab-pane>
      <el-tab-pane label="奖惩规则" name="EcMana">
        <EcMana></EcMana>
      </el-tab-pane>
      <el-tab-pane label="权限组" name="PermissionMana">
        <PermissionMana></PermissionMana>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>

import DepMana from "@/components/sys/basic/DepMana";
import EcMana from "@/components/sys/basic/EcMana";
import JoblevelMana from "@/components/sys/basic/JoblevelMana";
import PermissionMana from "@/components/sys/basic/PermissionMana";
import PosMana from "@/components/sys/basic/PosMana";

export default {
  name: "SysBasic",
  data() {
    return {
      activeName: 'DepMana'
    }
  },
  components: {
    DepMana,
    EcMana,
    JoblevelMana,
    PermissionMana,
    PosMana
  }
}
</script>

<style scoped>

</style>