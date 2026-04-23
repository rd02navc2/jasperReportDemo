<template>
  <div class="home-view">
    <h1>系統管理主控台</h1>
    <p>您已成功登入系統。請點擊上方按鈕進入報表系統。</p>
    <div class="action-box">
      <button @click="$router.push('/report')" class="nav-btn">進入報表下載</button>
    </div>
  </div>
</template>

<script setup>
// 這裡不需要複雜邏輯，因為 App.vue 已經處理好了
</script>

<style scoped>
.home-view {
  padding: 40px;
  text-align: center;
}
.action-box {
  margin-top: 20px;
}
/* 這裡不需要定義 .nav-btn，因為 App.vue 已經有全域樣式了 */
</style>