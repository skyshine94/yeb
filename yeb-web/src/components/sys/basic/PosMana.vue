<template>
  <div>
    <div>
      <el-input class="addPosInput" placeholder="添加职位" suffix-icon="el-icon-plus"
                v-model="pos.name" @keydown.enter.native="addPosition"></el-input>
      <el-button type="primary" icon="el-icon-plus" @click="addPosition">添加</el-button>
    </div>
    <div class="posManaMain">
      <el-table stripe border :data="positions" style="width: 70%"
                @selection-change="handlerSelectionChange">
        <el-table-column type="selection" width="60"></el-table-column>
        <el-table-column prop="id" label="编号" width="60"></el-table-column>
        <el-table-column prop="name" label="职位名称" width="120"></el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="150"></el-table-column>
        <el-table-column prop="enabled" label="是否启用" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <!--模板-->
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditView(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deletePosition(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button style="margin-top: 8px" type="danger" :disabled="this.multipleSelection.length == 0"
               @click="deletePositions">批量删除
    </el-button>
    <!--编辑框-->
    <el-dialog title="编辑职位" :visible.sync="dialogVisible" width="30%">
      <table>
        <tr>
          <td>
            <el-tag>职位名称</el-tag>
          </td>
          <td>
            <el-input v-model="updatedPos.name" class="updatePosInput"></el-input>
          </td>
        </tr>
        <tr>
          <td>
            <el-tag>是否启用</el-tag>
          </td>
          <td>
            <el-switch v-model="updatedPos.enabled" active-text="已启用" inactive-text="未启用"
                       class="updatePosInput"></el-switch>
          </td>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updatePosition">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PosMana",
  data() {
    return {
      pos: {
        name: ''
      },
      positions: [],
      dialogVisible: false,
      updatedPos: {
        name: '',
        enabled: true
      },
      multipleSelection: [] //接收多选对象
    }
  },
  mounted() { //生命周期函数
    this.initPositions();
  },
  methods: {
    addPosition() {
      if (this.pos.name) {
        this.postRequest('/system/basic/pos/', this.pos).then(resp => {
          if (resp) {
            //刷新表格
            this.initPositions();
          }
        })
      } else {
        this.$message.error('职位名称不能为空！');
      }
    },
    initPositions() {
      this.getRequest('/system/basic/pos/').then(resp => {
        if (resp) {
          this.positions = resp;
          this.pos.name = '';
        }
      })
    },
    showEditView(data) {
      Object.assign(this.updatedPos, data); //如果直接赋值给编辑框，修改编辑框时，表格中的原数据会同步修改，需要使用assign将值复制给编辑框
      this.updatedPos.createDate = ''; //创建时间由后端获取，设置为空
      this.dialogVisible = true; //显示编辑框
    },
    deletePosition(data) {
      this.$confirm('此操作将永久删除[' + data.name + ']职位，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/pos/' + data.id).then(resp => {
          if (resp) {
            //刷新表格
            this.initPositions();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除！！'
        });
      });
    },
    deletePositions() {
      this.$confirm('此操作将永久删除[' + this.multipleSelection.length + ']条职位记录，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = '?';
        //循环选中职位
        this.multipleSelection.forEach(item => {
          ids += 'ids=' + item.id + '&';
        })
        this.deleteRequest('/system/basic/pos/' + ids).then(resp => {
          if (resp) {
            //刷新表格
            this.initPositions();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除！！'
        });
      });
    },
    updatePosition() {
      this.putRequest('/system/basic/pos/', this.updatedPos).then(resp => {
        if (resp) {
          //刷新表格
          this.initPositions();
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
.addPosInput {
  width: 300px;
  margin-right: 8px;
}

.posManaMain {
  margin-top: 10px;
}

.updatePosInput {
  width: 200px;
  margin-left: 8px;
}
</style>