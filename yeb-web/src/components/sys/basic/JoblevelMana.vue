<template>
  <div>
    <div>
      <el-input class="addJlInput" placeholder="添加职称等级" prefix-icon="el-icon-plus"
                v-model="jl.name" @keydown.enter.native="addPosition"></el-input>
      <el-select v-model="jl.titleLevel" placeholder="职称等级" style="margin-right: 8px">
        <el-option v-for="item in titleLevels" :key="item" :label="item" :value="item"></el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-plus" @click="addJl">添加</el-button>
    </div>
    <div class="jlManaMain">
      <el-table stripe border :data="jls" style="width: 70%" @selection-change="handlerSelectionChange">
        <el-table-column type="selection" width="60"></el-table-column>
        <el-table-column prop="id" label="编号" width="60"></el-table-column>
        <el-table-column prop="name" label="职称名称" width="120"></el-table-column>
        <el-table-column prop="titleLevel" label="职称等级" width="120"></el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="150"></el-table-column>
        <el-table-column prop="enabled" label="是否启用" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditView(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteJl(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button style="margin-top: 8px" type="danger" :disabled="this.multipleSelection.length == 0"
               @click="deleteJls">批量删除
    </el-button>
    <!--编辑框-->
    <el-dialog title="编辑职称" :visible.sync="dialogVisible" width="30%">
      <table>
        <tr>
          <td>
            <el-tag>职称名称</el-tag>
          </td>
          <td>
            <el-input v-model="updatedJl.name" class="updateJlInput"></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>职称等级</el-tag>
          </td>
          <td>
            <el-select v-model="updatedJl.titleLevel" class="updateJlInput">
              <el-option v-for="item in titleLevels" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>是否启用</el-tag>
          </td>
          <td>
            <el-switch v-model="updatedJl.enabled" active-text="已启用" inactive-text="未启用"
                       class="updateJlInput"></el-switch>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateJl">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "JoblevelMana",
  data() {
    return {
      jl: {
        name: '',
        titleLevel: ''
      },
      titleLevels: [
        '正高级',
        '副高级',
        '中级',
        '初级',
        '员级'
      ],
      jls: [],
      dialogVisible: false,
      updatedJl: {
        name: '',
        titleLevel: '',
        enabled: true
      },
      multipleSelection: []
    }
  },
  mounted() {
    this.initJls();
  },
  methods: {
    addJl() {
      if (this.jl.name && this.jl.titleLevel) {
        this.postRequest('/system/basic/joblevel/', this.jl).then(resp => {
          if (resp) {
            //刷新表格
            this.initJls();
          }
        })
      } else {
        this.$message.error('职称或职称等级不能为空！');
      }
    },
    initJls() {
      this.getRequest('/system/basic/joblevel/').then(resp => {
        if (resp) {
          this.jls = resp;
          this.jl.name = '';
          this.jl.titleLevel = '';
        }
      })
    },
    showEditView(data) {
      Object.assign(this.updatedJl, data);
      this.updatedJl.createDate = '';
      this.dialogVisible = true;
    },
    deleteJl(data) {
      this.$confirm('此操作将永久删除[' + data.name + ']职位，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/joblevel/' + data.id).then(resp => {
          if (resp) {
            //刷新表格
            this.initJls();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除！！'
        });
      });
    },
    deleteJls() {
      this.$confirm('此操作将永久删除[' + this.multipleSelection.length + ']条职称记录，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = '?';
        //循环选中职位
        this.multipleSelection.forEach(item => {
          ids += 'ids=' + item.id + '&';
        })
        this.deleteRequest('/system/basic/joblevel/' + ids).then(resp => {
          if (resp) {
            //刷新表格
            this.initJls();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除！！'
        });
      });
    },
    updateJl() {
      this.putRequest('/system/basic/joblevel/', this.updatedJl).then(resp => {
        if (resp) {
          //刷新表格
          this.initJls();
          this.dialogVisible = false;
        }
      })
    },
    handlerSelectionChange(val) {
      this.multipleSelection = val
    }
  }
}
</script>

<style>
.addJlInput {
  width: 300px;
  margin-right: 8px;
}

.jlManaMain {
  margin-top: 10px;
}

.updateJlInput {
  width: 200px;
  margin-left: 8px;
}
</style>