<template>
  <div>
    <div>
      <div style="display: flex; justify-content: space-between;">
        <div>
          <el-input style="width: 300px;margin-right: 10px" v-model="empName" @keydown.enter.native="initEmps"
                    placeholder="请输入员工名进行搜索..."
                    :disabled="showAdvanceSearchVisible" prefix-icon="el-icon-search" clearable
                    @clear="initEmps"></el-input>
          <el-button type="primary" icon="el-icon-search" @click="initEmps" :disabled="showAdvanceSearchVisible">搜索
          </el-button>
          <el-button type="primary" @click="showAdvanceSearchVisible = !showAdvanceSearchVisible">
            <!--font awesome图标-->
            <i :class="showAdvanceSearchVisible ? 'fa fa-angle-double-up' : 'fa fa-angle-double-down'"
               aria-hidden="true"></i>
            高级搜索
          </el-button>
        </div>
        <div>
          <el-upload style="display: inline-flex; margin-right: 10px;" :headers="headers" :show-file-list="false"
                     :before-upload="beforeUpload"
                     :on-success="onSuccess" :on-error="onError" :disabled="importDataDisabled"
                     action="/employee/basic/import">
            <el-button type="success" :icon="importDataBtnIcon" :disabled="importDataDisabled">
              {{ importDataBtnText }}
            </el-button>
          </el-upload>
          <el-button type="success" @click="exportData" icon="el-icon-download">
            导出数据
          </el-button>
          <el-button type="primary" icon="el-icon-plus" @click="showAddView">
            添加员工
          </el-button>
        </div>
      </div>
    </div>
    <!--展开动画-->
    <transition name="slide-fade">
      <div v-show="showAdvanceSearchVisible"
           style="border: 1px solid dodgerblue; border-radius: 5px; box-sizing: border-box; padding: 5px; margin: 10px 0px">
        <el-row>
          <el-col :span="5">
            政治面貌
            <el-select size="mini" style="width: 140px;" v-model="searchValue.politicId"
                       placeholder="请选择政治面貌">
              <el-option v-for="item in politicsStatus" :key="item.id" :label="item.name"
                         :value="item.id"></el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            民族
            <el-select size="mini" style="width: 140px;" v-model="searchValue.nationId"
                       placeholder="请选择民族">
              <el-option v-for="item in nations" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            职位
            <el-select size="mini" style="width: 140px;" v-model="searchValue.posId" placeholder="请选择职位">
              <el-option v-for="item in positions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            职称
            <el-select size="mini" style="width: 140px;" v-model="searchValue.jobLevelId" placeholder="请选择职称">
              <el-option v-for="item in joblevels" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-col>
          <el-col :span="7">
            聘用形式
            <el-radio-group v-model="searchValue.engageForm">
              <el-radio label="劳动合同">劳动合同</el-radio>
              <el-radio label="劳务合同">劳务合同</el-radio>
            </el-radio-group>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="5">
            所属部门
            <el-popover placement="bottom" title="请选择部门" width="200" trigger="manual" v-model="visible2">
              <el-tree default-expand-all :data="departments" :props="defaultProps"
                       @node-click="searchHandleNodeClick"></el-tree>
              <div slot="reference"
                   style="width: 140px; display: inline-flex; border: 1px solid #dedede; height: 24px; border-radius: 5px; cursor: pointer; align-items: center; font-size: 13px; padding-left: 8px; box-sizing: border-box"
                   @click="showDepView2">
                {{ inputDepName }}
              </div>
            </el-popover>
          </el-col>
          <el-col :span="10">
            入职日期
            <el-date-picker v-model="searchValue.beginDateScope" size="mini" type="daterange" range-separator="至"
                            start-placeholder="开始日期" unlink-panels
                            end-placeholder="结束日期" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-col>
          <el-col :span="5" :offset="4"><!--偏移4格-->
            <el-button size="mini" @click="showAdvanceSearchVisible = !showAdvanceSearchVisible">取消</el-button>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="initEmps('advanced')">搜索</el-button>
          </el-col>
        </el-row>
      </div>
    </transition>
    <div style="margin-top: 10px;">
      <el-table :data="emps" stripe border style="width: 100%"
                v-loading="loading"
                element-loading-text="加载中..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)">
        <el-table-column type="selection" width="60"></el-table-column>
        <el-table-column prop="name" label="姓名" width="60" fixed></el-table-column>
        <el-table-column prop="workID" label="工号" width="100"></el-table-column>
        <el-table-column prop="gender" label="性别" width="60"></el-table-column>
        <el-table-column prop="birthday" label="生日" width="100"></el-table-column>
        <el-table-column prop="idCard" label="身份证号" width="150"></el-table-column>
        <el-table-column prop="wedlock" label="婚姻状况" width="80"></el-table-column>
        <el-table-column prop="nation.name" label="民族" width="60"></el-table-column>
        <el-table-column prop="nativePlace" label="籍贯" width="100"></el-table-column>
        <el-table-column prop="politicsStatus.name" label="政治面貌" width="120"></el-table-column>
        <el-table-column prop="email" label="电子邮箱" align="left" width="180"></el-table-column>
        <el-table-column prop="phone" label="电话号码" align="left" width="100"></el-table-column>
        <el-table-column prop="address" label="联系地址" align="left" width="300"></el-table-column>
        <el-table-column prop="department.name" label="所属部门" width="100"></el-table-column>
        <el-table-column prop="joblevel.name" label="职称" width="100"></el-table-column>
        <el-table-column prop="position.name" label="职位" width="100"></el-table-column>
        <el-table-column prop="engageForm" label="聘用形式" width="100"></el-table-column>
        <el-table-column prop="tiptopDegree" label="学历" width="100"></el-table-column>
        <el-table-column prop="school" label="毕业院校" width="150"></el-table-column>
        <el-table-column prop="specialty" label="专业" width="150"></el-table-column>
        <el-table-column prop="beginDate" label="入职日期" width="100"></el-table-column>
        <el-table-column prop="conversionTime" label="转正日期" width="100"></el-table-column>
        <el-table-column prop="beginContract" label="合同起始日期" width="100"></el-table-column>
        <el-table-column prop="endContract" label="合同截止日期" width="100"></el-table-column>
        <el-table-column label="合同期限" width="100">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.contractTerm }}</el-tag>
            年
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button @click="showEditView(scope.row)" style="padding: 3px">编辑</el-button>
            <el-button style="padding: 3px" type="primary">查看高级资料</el-button>
            <el-button style="padding: 3px" type="danger" @click="deleteEmp(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex; justify-content: flex-end;">
        <el-pagination background @current-change="currentChange" @size-change="sizeChange"
                       layout="sizes, prev, pager, next, jumper, ->, total"
                       :total="total"></el-pagination>
      </div>
    </div>
    <!--编辑框-->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="80%">
      <div>
        <el-form ref="empForm" :model="emp" :rules="rules">
          <!--layout布局-->
          <el-row>
            <el-col :span="6">
              <el-form-item label="姓名" prop="name">
                <el-input size="mini" prefix-icon="el-icon-edit" style="width: 150px; margin-left: 28px;"
                          v-model="emp.name"
                          placeholder="请输入员工姓名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="emp.gender" style="margin-left: 40px; margin-top: 8px;">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出生日期" prop="birthday">
                <el-date-picker size="mini" style="width: 150px; margin-left: 28px;" v-model="emp.birthday" type="date"
                                value-format="yyyy-MM-dd" placeholder="请选择出生日期"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="政治面貌" prop="politicId">
                <el-select size="mini" style="width: 200px; margin-left: 28px;" v-model="emp.politicId"
                           placeholder="请选择政治面貌">
                  <el-option v-for="item in politicsStatus" :key="item.id" :label="item.name"
                             :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="民族" prop="nationId">
                <el-select size="mini" style="width: 150px; margin-left: 28px;" v-model="emp.nationId"
                           placeholder="请选择民族">
                  <el-option v-for="item in nations" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="籍贯" prop="nativePlace">
                <el-input size="mini" prefix-icon="el-icon-edit" style="width: 150px; margin-left: 28px;"
                          v-model="emp.nativePlace"
                          placeholder="请输入籍贯"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电子邮箱" prop="email">
                <el-input size="mini" prefix-icon="el-icon-message" style="width: 150px; margin-left: 28px;"
                          v-model="emp.email"
                          placeholder="请输入邮箱"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="联系地址" prop="address">
                <el-input size="mini" prefix-icon="el-icon-edit" style="width: 200px; margin-left: 28px;"
                          v-model="emp.address"
                          placeholder="请输入地址"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="职位" prop="posId">
                <el-select size="mini" style="width: 150px; margin-left: 28px;" v-model="emp.posId" placeholder="请选择职位">
                  <el-option v-for="item in positions" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="职称" prop="jobLevelId">
                <el-select size="mini" style="width: 150px; margin-left: 28px;" v-model="emp.jobLevelId"
                           placeholder="请选择职称">
                  <el-option v-for="item in joblevels" :key="item.id" :label="item.name" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属部门" prop="departmentId">
                <el-popover placement="bottom" title="请选择部门" width="200" trigger="manual"
                            v-model="visible">
                  <el-tree default-expand-all :data="departments" :props="defaultProps"
                           @node-click="handleNodeClick"></el-tree>
                  <div slot="reference"
                       style="width: 150px; margin-left: 28px;margin-top: 3px; display: inline-flex; border: 1px solid #dedede; height: 24px; border-radius: 5px; cursor: pointer; align-items: center; font-size: 13px; padding-left: 8px; box-sizing: border-box"
                       @click="showDepView">
                    {{ inputDepName }}
                  </div>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="电话号码" prop="phone">
                <el-input size="mini" prefix-icon="el-icon-phone" style="width: 200px; margin-left: 28px;"
                          v-model="emp.phone"
                          placeholder="请输入电话号码"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="工号" prop="workID">
                <el-input size="mini" style="width: 150px; margin-left: 28px;"
                          v-model="emp.workID" disabled></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="学历" prop="tiptopDegree">
                <el-select size="mini" style="width: 150px; margin-left: 28px;" v-model="emp.tiptopDegree"
                           placeholder="请选择学历">
                  <el-option v-for="item in tiptopDegrees" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="毕业院校" prop="school">
                <el-input size="mini" prefix-icon="el-icon-edit" style="width: 150px; margin-left: 28px;"
                          v-model="emp.school"
                          placeholder="请输入毕业院校"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="专业名称" prop="specialty">
                <el-input size="mini" prefix-icon="el-icon-edit" style="width: 200px; margin-left: 28px;"
                          v-model="emp.specialty"
                          placeholder="请输入专业名称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="入职日期" prop="beginDate">
                <el-date-picker size="mini" style="width: 150px" v-model="emp.beginDate" type="date"
                                value-format="yyyy-MM-dd" placeholder="请选择入职日期"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="转正日期" prop="conversionTime">
                <el-date-picker size="mini" style="width: 150px" v-model="emp.conversionTime" type="date"
                                value-format="yyyy-MM-dd" placeholder="请选择转正日期"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同起始日期" prop="beginContract">
                <el-date-picker size="mini" style="width: 150px" v-model="emp.beginContract" type="date"
                                value-format="yyyy-MM-dd" placeholder="请选择合同起始日期"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="合同截止日期" prop="endContract">
                <el-date-picker size="mini" style="width: 200px" v-model="emp.endContract" type="date"
                                value-format="yyyy-MM-dd" placeholder="请选择合同截止日期"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="身份证号" prop="idCard">
                <el-input size="mini" prefix-icon="el-icon-edit" style="width: 150px"
                          v-model="emp.idCard"
                          placeholder="请输入身份证号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="聘用形式" prop="engageForm">
                <el-radio-group v-model="emp.engageForm" style="margin-left: 40px; margin-top: 8px;">
                  <el-radio label="劳动合同">劳动合同</el-radio>
                  <el-radio label="劳务合同">劳务合同</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="婚姻状况" prop="wedlock">
                <el-radio-group v-model="emp.wedlock" style="margin-left: 40px; margin-top: 8px;">
                  <el-radio label="已婚">已婚</el-radio>
                  <el-radio label="未婚">未婚</el-radio>
                  <el-radio label="离异">离异</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addEmp">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data() {
    return {
      title: '',
      emps: [],
      loading: false,
      total: 0,
      currentPage: 1,
      size: 10,
      empName: '',
      dialogVisible: false,
      visible: false,
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      emp: {
        id: null,
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '在职',
        workID: '',
        contractTerm: null,
        conversionTime: '',
        notWorkDate: null,
        beginContract: '',
        endContract: '',
        workAge: null,
        salaryId: null
      },
      nations: [],
      joblevels: [],
      politicsStatus: [],
      positions: [],
      tiptopDegrees: ['博士', '硕士', '本科', '大专', '高中', '初中', '小学', '其他'],
      departments: [],
      inputDepName: '',
      rules: {
        name: [{required: true, message: '请输入员工姓名', trigger: 'blur'}],
        gender: [{required: true, message: '请输入性别', trigger: 'blur'}],
        birthday: [{required: true, message: '请输入出生日期', trigger: 'blur'}],
        idCard: [{required: true, message: '请输入身份证号', trigger: 'blur'}, {
          pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
          message: '身份证号格式不正确',
          trigger: 'blur'
        }],
        wedlock: [{required: true, message: '请输入婚姻状况', trigger: 'blur'}],
        nationId: [{required: true, message: '请输入民族', trigger: 'blur'}],
        nativePlace: [{required: true, message: '请输入籍贯', trigger: 'blur'}],
        politicId: [{required: true, message: '请输入政治面貌', trigger: 'blur'}],
        email: [{required: true, message: '请输入电子邮箱', trigger: 'blur'}, {
          type: 'email',
          message: '电子邮箱格式不正确',
          trigger: 'blur'
        }],
        phone: [{required: true, message: '请输入电话号码', trigger: 'blur'}],
        address: [{required: true, message: '请输入联系地址', trigger: 'blur'}],
        departmentId: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
        jobLevelId: [{required: true, message: '请输入职称', trigger: 'blur'}],
        posId: [{required: true, message: '请输入职位', trigger: 'blur'}],
        engageForm: [{required: true, message: '请输入聘用形式', trigger: 'blur'}],
        tiptopDegree: [{required: true, message: '请输入学历', trigger: 'blur'}],
        specialty: [{required: true, message: '请输入专业名称', trigger: 'blur'}],
        school: [{required: true, message: '请输入毕业院校', trigger: 'blur'}],
        beginDate: [{required: true, message: '请输入入职日期', trigger: 'blur'}],
        workID: [{required: true, message: '请输入工号', trigger: 'blur'}],
        contractTerm: [{required: true, message: '请输入合同期限', trigger: 'blur'}],
        conversionTime: [{required: true, message: '请输入转正日期', trigger: 'blur'}],
        notWorkDate: [{required: true, message: '请输入离职日期', trigger: 'blur'}],
        beginContract: [{required: true, message: '请输入合同起始日期', trigger: 'blur'}],
        endContract: [{required: true, message: '请输入合同截止日期', trigger: 'blur'}],
      },
      importDataDisabled: false,
      importDataBtnText: '导入数据',
      importDataBtnIcon: 'el-icon-upload2',
      headers: {
        Authorization: window.sessionStorage.getItem('tokenStr')
      },
      showAdvanceSearchVisible: false,
      visible2: false,
      searchValue: {
        nationId: null,
        politicId: null,
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        beginDateScope: null
      }
    }
  },
  mounted() {
    this.initEmps();
    this.initData();
    this.initPositions();
  },
  methods: {
    initEmps(type) {
      this.loading = true;
      let url = '/employee/basic/?currentPage=' + this.currentPage + '&size=' + this.size;
      //判断是否是高级搜索
      if (type && type == 'advanced') {
        if (this.searchValue.nationId) {
          url += '&nationId=' + this.searchValue.nationId;
        }
        if (this.searchValue.politicId) {
          url += '&politicId=' + this.searchValue.politicId;
        }
        if (this.searchValue.departmentId) {
          url += '&departmentId=' + this.searchValue.departmentId;
        }
        if (this.searchValue.jobLevelId) {
          url += '&jobLevelId=' + this.searchValue.jobLevelId;
        }
        if (this.searchValue.posId) {
          url += '&posId=' + this.searchValue.posId;
        }
        if (this.searchValue.engageForm) {
          url += '&engageForm=' + this.searchValue.engageForm;
        }
        if (this.searchValue.beginDateScope) {
          url += '&beginDateScope=' + this.searchValue.beginDateScope;
        }
      } else {
        url += '&name=' + this.empName;
      }
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
      })
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.initEmps();
    },
    sizeChange(size) {
      this.size = size;
      this.initEmps();
    },
    showAddView() {
      this.title = '添加员工';
      //初始化窗口信息
      this.emp = {
        id: null,
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '在职',
        workID: '',
        contractTerm: null,
        conversionTime: '',
        notWorkDate: null,
        beginContract: '',
        endContract: '',
        workAge: null,
        salaryId: null
      };
      this.inputDepName = '';
      this.getMaxWorkID();
      this.dialogVisible = true;
    },
    showEditView(data) {
      this.title = '编辑员工信息';
      this.emp = data;
      this.inputDepName = data.department.name;
      this.initPositions();
      this.dialogVisible = true;
    },
    initData() {
      //不经常变动的数据放在sessionStorage中
      if (!window.sessionStorage.getItem('nations')) {
        this.getRequest('/employee/basic/nations').then(resp => {
          if (resp) {
            this.nations = resp;
            window.sessionStorage.setItem('nations', JSON.stringify(resp));
          }
        })
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem('nations'));
      }
      if (!window.sessionStorage.getItem('joblevels')) {
        this.getRequest('/employee/basic/joblevels').then(resp => {
          if (resp) {
            this.joblevels = resp;
            window.sessionStorage.setItem('joblevels', JSON.stringify(resp));
          }
        })
      } else {
        this.joblevels = JSON.parse(window.sessionStorage.getItem('joblevels'));
      }
      if (!window.sessionStorage.getItem('politicsStatus')) {
        this.getRequest('/employee/basic/politicsStatus').then(resp => {
          if (resp) {
            this.politicsStatus = resp;
            window.sessionStorage.setItem('politicsStatus', JSON.stringify(resp));
          }
        })
      } else {
        this.politicsStatus = JSON.parse(window.sessionStorage.getItem('politicsStatus'));
      }
      if (!window.sessionStorage.getItem('departments')) {
        this.getRequest('/employee/basic/departments').then(resp => {
          if (resp) {
            this.departments = resp;
            window.sessionStorage.setItem('departments', JSON.stringify(resp));
          }
        })
      } else {
        this.departments = JSON.parse(window.sessionStorage.getItem('departments'));
      }
    },
    initPositions() {
      this.getRequest('/employee/basic/positions').then(resp => {
        if (resp) {
          this.positions = resp;
        }
      })
    },
    getMaxWorkID() {
      this.getRequest(' /employee/basic/maxWorkID').then(resp => {
        if (resp) {
          this.emp.workID = resp.obj;
        }
      })
    },
    showDepView() {
      this.visible = !this.visible;
    },
    showDepView2() {
      this.visible2 = !this.visible2;
    },
    handleNodeClick(data) {
      this.emp.departmentId = data.id;
      this.inputDepName = data.name;
      this.visible = !this.visible;
    },
    addEmp() {
      //id存在则为更新操作，不存在则为添加操作
      if (this.emp.id) {
        this.$refs['empForm'].validate(valid => {
          if (valid) {
            this.putRequest('/employee/basic/', this.emp).then(resp => {
              if (resp) {
                this.initEmps();
                this.dialogVisible = false;
              }
            })
          }
        })
      } else {
        this.$refs['empForm'].validate(valid => {
          if (valid) {
            this.postRequest('/employee/basic/', this.emp).then(resp => {
              if (resp) {
                this.initEmps();
                this.dialogVisible = false;
              }
            })
          }
        })
      }
    },
    deleteEmp(data) {
      this.$confirm('此操作将永久删除[' + data.name + ']员工，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/employee/basic/' + data.id).then(resp => {
          if (resp) {
            this.initEmps();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除！！'
        });
      });
    },
    exportData() {
      this.downloadRequest('/employee/basic/export');
    },
    beforeUpload() {
      this.importDataDisabled = true;
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataBtnText = '正在导入...';
    },
    onSuccess() {
      this.importDataDisabled = false;
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataBtnText = '导入数据';
      this.initEmps();
    },
    onError() {
      this.importDataDisabled = false;
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataBtnText = '导入数据';
    },
    searchHandleNodeClick(data) {
      this.searchValue.departmentId = data.id;
      this.inputDepName = data.name;
      this.visible2 = !this.visible2;
    }
  }
}

</script>

<style>

.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to {
  transform: translateX(10px);
  opacity: 0;
}

</style>