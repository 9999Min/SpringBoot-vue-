<template>
  <div>
    <!--    查询栏（+新增）-->
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入仓库名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 5px;" @click="loadPost" round>查询</el-button>
      <el-button type="success" @click="resetParam" round>重置</el-button>
      <el-button type="primary" style="margin-left: 5px;" @click="add" round>新增</el-button>
    </div>

    <!--    数据表格-->
    <el-table :data="tableData" border stripe :header-cell-style="{background:'#f2f5fc',color:'#555555'}">
      <el-table-column prop="id" label="ID" width="90">
      </el-table-column>
      <el-table-column prop="name" label="仓库名" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="360">
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template v-slot:default="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 5px;">
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

    <!--    对话框-->
    <el-dialog
        :title="dialogTitle"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="仓库名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "StorageManage",
  data() {
    let checkDuplicate = (rule, value, callback) => {
      if (this.form.id && this.nameBeforeEdit === this.form.name) {//编辑模式并且仓库名没有改变
        return callback();
      }
      this.$axios.get(this.$httpUrl + "/wms/storage/findByName?name=" + this.form.name).then(res => res.data).then(res => {
        if (res.code === 400) callback();
        else callback(new Error('仓库名已经存在'));
      })
    };
    return {
      nameBeforeEdit: '',
      //查询的返回值
      tableData: [],
      //查询的参数
      total: 0, pageNum: 1, pageSize: 10, name: '',
      //对话框
      dialogTitle: '',
      centerDialogVisible: false,
      form: {
        id: '', name: '', remark: ''
      },
      //表单数据检查
      rules: {
        name: [
          {required: true, message: '请输入名字', trigger: 'blur'},
          {validator: checkDuplicate, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    resetForm() {
      this.$refs.form.resetFields()
      this.form.id = ''//莫名其妙的id没有被重置
    },
    add() {//新增按钮触发的方法
      this.dialogTitle = '新增'
      this.centerDialogVisible = true
      this.$nextTick(() => this.resetForm())
    },
    mod(row) {//编辑按钮触发的方法
      this.dialogTitle = '编辑'
      this.centerDialogVisible = true
      this.$nextTick(() => {
        //赋值到表单
        this.form.id = row.id + ''
        this.form.name = row.name
        this.nameBeforeEdit = row.name
        this.form.remark = row.remark
      })
    },
    confirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.saveOrUpdate()
        } else {
          console.log('error submit!!')
          return false
        }
      });
    },
    saveOrUpdate() {
      this.$axios.post(this.$httpUrl + '/wms/storage/saveOrUpdate', this.form).then(res => res.data).then(res => {
        if (res.code === 200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
          this.resetForm()
        } else {
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }
      });
    },
    del(id) {
      this.$axios.get(this.$httpUrl + '/wms/storage/delete?id=' + id).then(res => res.data).then(res => {
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
    },
    loadPost() {
      this.$axios.post(this.$httpUrl + '/wms/storage/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param: {
          name: this.name
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
    }
  },
  beforeMount() {
    this.loadPost()
  }
}
</script>

<style scoped>

</style>