<template>
  <el-menu
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd84b"
      default-active="/Home"
      style="height: 100vh"
      :collapse="isCollapse"
      :collapse-transition="false"
      router>

    <el-menu-item index="/Home">
      <i class="el-icon-s-fold"></i>
      <span slot="title">首页</span>
    </el-menu-item>

    <el-menu-item :index="'/' + item.menuClick" v-for="(item, i) in menu" v-bind:key="i">
      <i :class="item.menuIcon"></i>
      <span slot="title">{{item.menuName}}</span>
    </el-menu-item>
  </el-menu>

</template>

<script>

export default {
  name: "Aside",
  computed: {
    "menu": {
      get() {
        // return this.$store.state.menu
        return JSON.parse(sessionStorage.getItem('CurMenu'))
      }
    }
  },
  props: {
    isCollapse: Boolean
  }
}
</script>

<style scoped>

</style>