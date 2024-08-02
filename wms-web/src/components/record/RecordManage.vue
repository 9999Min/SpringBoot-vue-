<template>
  <div>
    <!--    查询栏   -->
    <div style="margin-bottom: 5px;">
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 200px;"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="storage" placeholder="请选择仓库" style="margin-left: 5px;">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodsType" placeholder="请选择分类" style="margin-left: 5px;">
        <el-option
            v-for="item in goodsTypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px;" @click="loadPost" round>查询</el-button>
      <el-button type="success" style="margin-left: 5px;" @click="resetParam" round>重置</el-button>
    </div>

    <!--    数据表格-->
    <el-table :data="tableData"
              :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
              border stripe >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="goodsName" label="物品名" width="140">
      </el-table-column>
      <el-table-column prop="storageName" label="仓库" width="140">
      </el-table-column>
      <el-table-column prop="goodsTypeName" label="分类" width="140">
      </el-table-column>
      <el-table-column prop="operator" label="操作人" width="140">
      </el-table-column>
      <el-table-column prop="applicant" label="申请人" width="140">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="140">
      </el-table-column>
      <el-table-column prop="createTime" label="操作时间" width="200">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
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
  </div>
</template>

<script>
export default {
  name: "RecordManage",
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser')),
      storageData: [], goodsTypeData: [],
      //查询的返回值
      tableData: [],
      //查询的参数
      total: 0, pageNum: 1, pageSize: 10, name: '', storage: '', goodsType: '',
    }
  },
  methods: {
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
      this.$axios.post(this.$httpUrl + '/wms/record/listPage', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param: {
          name: this.name + '',
          storage: this.storage + '',
          goodsType: this.goodsType + '',
          roleId: this.user.roleId,
          userId: this.user.id
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
    findStorage(id) {
      return this.storageData.find(element => {
        return id === element.id
      }).name
    },
    findGoodsType(id) {
      return this.goodsTypeData.find(element => {
        return id === element.id
      }).name
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