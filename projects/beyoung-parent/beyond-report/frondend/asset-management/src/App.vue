<template>
  <div id="app">
    <el-container>
      <el-header>
        <div class="header">
          <h2>资产管理系统</h2>
          <div class="user-info">
            <span>欢迎，{{ username }}</span>
            <el-button link>退出</el-button>
          </div>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu router default-active="/dashboard">
            <el-menu-item index="/dashboard">盘点总览</el-menu-item>
            <el-menu-item index="/inventory">资产盘点明细</el-menu-item>
            <el-menu-item index="/my-assets">我的资产</el-menu-item>
            <el-menu-item index="/scrapped">报废资产</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const username = ref('测试用户') // 实际可从 session 或 store 获取
const logout = () => {
  // 清除登录态并跳转
  alert('退出登录')
}
</script>

<style scoped>
.el-header {
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
}
.header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
.el-aside {
  background-color: #f5f7fa;
  height: calc(100vh - 60px);
}
.el-menu {
  border-right: none;
}
</style>