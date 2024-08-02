<template>
  <el-container style="height: 100%">
    <el-aside  :width="aside_width" style="background-color: rgb(238, 241, 246)">
      <Aside :isCollapse="isCollapse"></Aside>
    </el-aside>

    <el-container style="height: 100%">
      <el-header style="text-align: right; font-size: 12px; border-bottom: rgba(168,168,168,0.3) 1px solid">
        <Header @doCollapse="doCollapse" :icon="icon_fold"></Header>
      </el-header>

      <el-main>
<!--        <Main></Main>-->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

export default {
  name: "Index",
  components: {
    Header,
    Aside
  },
  data() {
    return {
      isCollapse: false,
      icon_fold: 'el-icon-s-fold',
      aside_width: '200px'
    }
  },
  methods: {
    doCollapse() {
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {//如果是折叠的
        this.aside_width='64px'
        this.icon_fold='el-icon-s-unfold'
      } else {
        this.aside_width='200px'
        this.icon_fold='el-icon-s-fold'
      }
    }
  },
}
</script>

<style scoped>
.el-header {
  /*background-color: #B3C0D1;*/
  color: #333;
  line-height: 60px;
}

.el-main {
  padding: 5px;
}

.el-aside {
  color: #333;
}
</style>