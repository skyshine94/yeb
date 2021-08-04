<template>
  <div style="width: 500px">
    <el-input placeholder="请输入部门名称进行搜索..." v-model="filterText" prefix-icon="el-icon-search"></el-input>
    <el-tree class="filterTree" ref="tree" :data="deps" :props="defaultProps"
             :filter-node-method="filterNode" :expand-on-click-node="false"><!--只有点击箭头图标时才展开节点-->
      <!--自定义节点内容-->
      <span class="custom-tree-node" slot-scope="{ node, data }"
            style="display: flex; justify-content: space-between; width: 100%;">
        <span>{{ data.name }}</span>
        <span>
          <el-button type="primary" size="mini" class="depBtn" @click="() => showAddView(data)">添加部门</el-button>
          <el-button type="danger" size="mini" class="depBtn" @click="() => deleteDep(data)">删除部门</el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog title="添加部门" :visible.sync="dialogVisible" width="30%">
      <table>
        <tr>
          <td>
            <el-tag>上级部门</el-tag>
          </td>
          <td>
            {{ pname }}
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>部门名称</el-tag>
          </td>
          <td>
            <el-input v-model="dep.name" placeholder="请输入部门名称..."></el-input>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addDep">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "DepMana",
  data() {
    return {
      filterText: '',
      deps: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      dialogVisible: false,
      dep: {
        name: '',
        parentId: -1
      },
      pname: '' //上级部门名称
    }
  },
  watch: { //监控输入框值的变化
    filterText(val) {
      this.$refs.tree.filter(val); //调用filter-node-method
    }
  },
  mounted() {
    this.initDeps();
  },
  methods: {
    initDeps() {
      this.getRequest('/system/basic/department/').then(resp => {
        if (resp) {
          this.deps = resp;
        }
      })
    },
    filterNode(value, data) { //value是输入框输入的内容，data是对应的一条数据
      if (!value) return true; //输入框为空，展开树形结构
      return data.name.indexOf(value) !== -1; //展示节点
    },
    showAddView(data) {
      this.dep.parentId = data.id;
      this.pname = data.name;
      this.dialogVisible = true;
    },
    addDepToDeps(deps, dep) {
      for (let i = 0; i < deps.length; i++) {
        let d = deps[i];
        if (d.id == dep.parentId) {
          d.children = d.children.concat(dep);
          if (d.children.length > 0) {
            d.isParent = true;
          }
          return;
        } else {
          this.addDepToDeps(d.children, dep);
        }
      }
    },
    addDep() {
      this.postRequest('/system/basic/department/', this.dep).then(resp => {
        if (resp) {
          //刷新时不收缩整个树形结构
          this.addDepToDeps(this.deps, resp.obj);
          this.dep = {
            name: '',
            parentId: -1
          }
          this.pname = '';
          this.dialogVisible = false;
        }
      })
    },
    removeDepFromDeps(p, deps, id) {
      for (let i = 0; i < deps.length; i++) {
        let d = deps[i];
        if (d.id == id) {
          deps.splice(i, 1);
          //如果删除部门后父部门下没有其他部门，将父部门的isParent改为false
          if (deps.length == 0) {
            p.isParent = false; //参数p表示父部门
          }
          return;
        } else {
          this.removeDepFromDeps(d, d.children, id);
        }
      }
    },
    deleteDep(data) {
      if (data.isParent) {
        this.$message.error('父部门删除失败！');
      } else {
        this.$confirm('此操作将永久删除[' + data.name + ']部门，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRequest('/system/basic/department/' + data.id).then(resp => {
            if (resp) {
              //刷新时不收缩整个树形结构
              this.removeDepFromDeps(null, this.deps, data.id);
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除！！'
          });
        });
      }
    }
  }
}
</script>

<style>

.depBtn {
  padding: 2px;

}

</style>