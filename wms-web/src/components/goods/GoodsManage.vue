<template>
  <div>
    <!--    查询栏（+新增）-->
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="storage" placeholder="请选择仓库" style="margin-left: 5px;">
        <el-option
            v-for="(item, i) in storageData" :key="i"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodsType" placeholder="请选择分类" style="margin-left: 5px;">
        <el-option
            v-for="(item, i) in goodsTypeData" :key="i"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px;" @click="loadPost" round>查询</el-button>
      <el-button type="success" style="margin-left: 5px;" @click="resetParam" round>重置</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="add" v-if="user.roleId!==2" round>新增</el-button>
      <el-button type="warning" style="margin-left: 160px" @click="inGoods" v-if="user.roleId!==2" round>入库</el-button>
      <el-button type="danger" style="margin-left: 5px" @click="outGoods" v-if="user.roleId!==2" round>出库</el-button>
    </div>

    <!--    数据表格-->
    <el-table :data="tableData" border stripe :header-cell-style="{background:'#f2f5fc',color:'#555555'}"
              border
              highlight-current-row
              @current-change="selectCurrentChange">
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="name" label="物品名" width="180">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" width="180" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="goodsType" label="分类" width="180">
        <template v-slot:default="scope">
          <el-tag type="success" disable-transitions>{{ findGoodsType(scope.row.goodsType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="count" label="数量" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>
      <el-table-column label="操作" v-if="user.roleId!==2">
        <template v-slot:default="scope">
          <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)" style="margin-left: 5px;">
            <el-button slot="reference" size="small" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--    分页栏-->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <!--    新增对话框-->
    <el-dialog title="新增物品" :visible.sync="centerDialogVisible" width="30%" center>
      <!--      内部对话框——用户选择-->
      <el-dialog title="用户选择" :visible.sync="innerVisible" width="75%" append-to-body>
        <SelectUser @doSelectUser="selectedUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmUser">确 定</el-button>
        </span>
      </el-dialog>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="物品名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人" prop="applicant">
          <el-col :span="20">
            <el-input v-model="form.applicant"  @click.native="innerVisible = true"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="仓库" prop="storage">
          <el-col :span="20">
            <el-select v-model="form.storage" placeholder="请选择仓库">
              <el-option
                 v-for="(item, i) in storageData" :key="i"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="分类" prop="goodsType">
          <el-col :span="20">
            <el-select v-model="form.goodsType" placeholder="请选择分类">
              <el-option
                 v-for="(item, i) in goodsTypeData" :key="i"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remark" readonly></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addConfirm">确 定</el-button>
      </span>
    </el-dialog>

    <!--    出入库对话框-->
    <el-dialog :title="dialogTitle" :visible.sync="inOutDialogVisible" width="30%" center>
      <!--      内部对话框——用户选择-->
      <el-dialog
          width="75%"
          title="用户选择"
          :visible.sync="innerVisible"
          append-to-body>
        <SelectUser @doSelectUser="selectedUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmUser">确 定</el-button>
        </span>
      </el-dialog>
      <el-form ref="recordForm" :rules="recordRules" :model="recordForm" label-width="80px">
        <el-form-item label="物品名">
          <el-col :span="20">
            <el-input v-model="recordForm.recordName" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人" prop="applicant">
          <el-col :span="20">
            <el-input v-model="recordForm.applicant"  @click.native="innerVisible = true"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="recordForm.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="recordForm.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="inOutDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRecord">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import SelectUser from "@/components/user/SelectUser.vue";

export default {
  name: "GoodsManage",
  components: {SelectUser},
  data() {
/*    let checkDuplicate = (rule, value, callback) => {
      if (this.recordForm.action === 3 || this.recordForm.action === 4 || this.form.name || this.form.storage) {//出库或入库
        return callback();
      }
      this.$axios.get(this.$httpUrl + "/wms/goods/findByNameAndStorage?name=" + this.form.name
          + "&storage=" + this.form.storage).then(res => res.data).then(res => {
        // console.log(res.data)
        if (res.code === 400) callback();
        else callback(new Error('该物品已在仓库中，请选择入库'));
      })
    };*/
    let checkCount = (rule, value, callback) => {
      if (value > 9999) {
        callback(new Error('数量输入过大'));
      } else {
        callback();
      }
    };
    return {
      newGoodsId: '',
      currentRow: {},
      user: JSON.parse(sessionStorage.getItem('CurUser')),
      storageData: [], goodsTypeData: [],
      //查询的返回值
      tableData: [],
      //查询的参数
      total: 0, pageNum: 1, pageSize: 10, name: '', storage: '', goodsType: '',
      //对话框
      dialogTitle: '', centerDialogVisible: false, inOutDialogVisible: false, innerVisible: false,
      form: {id: '', name: '', storage: '', goodsType: '', count: '', remark: '新增', applicant: ''},
      //表单数据检查
      rules: {
        name: [
          {required: true, message: '请输入物品字', trigger: 'blur'},
          // {validator: checkDuplicate, trigger: 'blur'}
        ],
        applicant: [
          {required: true, message: '请选择申请人', trigger: 'change'}
        ],
        storage: [
          {required: true, message: '请选择仓库', trigger: 'change'},
          // {validator: checkDuplicate, trigger: 'change'}
        ],
        goodsType: [
          {required: true, message: '请选择分类', trigger: 'change'}
        ],
        count: [
          {required: true, message: '请输入数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数字', trigger: "blur"},
          {validator: checkCount, trigger: 'blur'}
        ],
      },
      //出入库表单和数据校验规则
      recordForm: {recordName: '', applicant: '', goods: '', userId: '', adminId: '', count: '', remark: '', action: '', storageId: ''},
      recordRules: {
        applicant: [
          {required: true, message: '请选择申请人', trigger: 'change'}
        ],
        count: [
          {required: true, message: '请输入数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数字', trigger: "blur"},
          {validator: checkCount, trigger: 'blur'}
        ],
      },
      tempUser: {}, tempStorage: '', tempGoodsType: ''
    }
  },
  methods: {
    selectedUser(val) {
      this.tempUser = val
    },
    confirmUser() {
      this.form.applicant = this.tempUser.name
      this.recordForm.applicant = this.tempUser.name
      this.recordForm.userId = this.tempUser.id
      this.innerVisible = false
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.form.id = ''//莫名其妙的id没有被重置
    },
    resetRecordForm() {
      this.$refs.recordForm.resetFields();
    },
    add() {//新增按钮触发的方法
      this.centerDialogVisible = true
      this.$nextTick(() => this.resetForm())
      this.recordForm.action = 0
    },
    addConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.recordForm.count = this.form.count
          this.recordForm.remark = this.form.remark
          this.recordForm.adminId = this.user.id
          this.doAdd()
        } else {
          console.log('error submit!!')
          return false
        }
      });
    },
    doAdd() {
      this.$axios.post(this.$httpUrl + '/wms/goods/save', this.form).then(res => res.data).then(res => {
        if (res.code === 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          //保存记录
          this.saveRecord()
          // 关闭对话框
          this.centerDialogVisible = false
          this.resetForm()
          this.loadPost()
        } else {
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }
      });
    },
    del(id) {
      this.$axios.get(this.$httpUrl + '/wms/goods/delete?id=' + id).then(res => res.data).then(res => {
        if (res.code === 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.loadPost();
        } else {
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }
      });
    },
    selectCurrentChange(val) {
      this.currentRow = val;
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.pageNum = 1
      this.pageSize = val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.pageNum = val
      this.loadPost()
    },
    resetParam() {
      this.name = ''
      this.storage = ''
      this.goodsType = ''
    },
    loadPost() {
      this.$axios.post(this.$httpUrl + '/wms/goods/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param: {
          name: this.name,
          storage: this.storage + '',
          goodsType: this.goodsType + ''
        }
      }).then(res => res.data).then(res => {
        if (res.code === 200) {
          this.tableData = res.data
          this.total = res.total
        } else {
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }
      });
    },
    listStorage() {
      this.$axios.get(this.$httpUrl + '/wms/storage/list').then(res => res.data).then(res => {
        if (res.code === 200) {
          this.storageData = res.data
        } else {
          alert('获取数据失败')
        }

      })
    },
    listGoodsType() {
      this.$axios.get(this.$httpUrl + '/wms/goodsType/list').then(res => res.data).then(res => {
        if (res.code === 200) {
          this.goodsTypeData = res.data
        } else {
          alert('获取数据失败')
        }
      })
    },
    formatStorage(row) {
      let temp = this.storageData.find(item => {
        return item.id === row.storage
      })
      return temp && temp.name//防止temp是空值或undefined
    },
    findStorage(id) {
      let storage = this.storageData.find(element => {
        return id === element.id
      })
      return storage && storage.name
    },
    findGoodsType(id) {
      let goodsType =  this.goodsTypeData.find(element => {
        return id === element.id
      })
      return goodsType && goodsType.name
    },
    resetInOutDialog() {
      if (!this.currentRow.id) {
        alert('请选择记录')
        return
      }
      this.inOutDialogVisible = true
      this.$nextTick(() => {
        this.resetRecordForm()
      })
      this.recordForm.goods = this.currentRow.id
      this.recordForm.recordName = this.currentRow.name
      this.recordForm.adminId = this.user.id
    },
    inGoods() {
      this.recordForm.action = '3'
      this.dialogTitle = '入库'
      this.resetInOutDialog()
    },
    outGoods() {
      this.recordForm.action = '4'
      this.dialogTitle = '出库'
      this.resetInOutDialog()
    },
    saveRecord() {
      this.$axios.post(this.$httpUrl + '/wms/record/save', this.recordForm).then(res => res.data).then(res => {
        if (res.code === 200) {
/*          this.$message({
            message: '操作成功！',
            type: 'success'
          });*/
          this.inOutDialogVisible = false
          this.loadPost()
        } else {
          this.$message({
            message: '保存记录失败！',
            type: 'error'
          });
        }
      })
    },
  },
  beforeMount() {
    this.loadPost()
    this.listStorage()
    this.listGoodsType()
  }
}
</script>

<style scoped>

</style>