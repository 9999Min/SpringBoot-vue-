<template>
  <div style="display: flex;height: 60px; line-height: 60px">
    <i :class="icon" style="font-size: 20px ;line-height: 60px" @click="collapse"></i>
    <div style="flex: 1;text-align: center;font-size: 34px; line-height: 60px">
      <span>物品管理系统</span>
    </div>
    <span>{{user.name}}</span>
    <el-dropdown>
      <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      <el-dropdown-menu>
        <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser'))
    }
  },
  props: {
    icon: String
  },
  methods: {
    toUser() {
      alert("个人中心详情")
      this.$router.push('/Home')
    },
    logout() {
      this.$confirm('此操作将退出登录，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '退出成功!'
        });
        this.$router.replace('/')
        sessionStorage.clear()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        });
      });
    },
    collapse() {
      this.$emit('doCollapse')
      this.icon_isFold = 'el-icon-s-unfold'
    }
  },
  created() {
    this.$router.push("/Home")
  }
}
</script>

<style scoped>

</style>