<template>
  <div class="app-shell">
    <!-- 頁首 -->
    <header class="app-header">
      <div class="header-inner">
        <div class="brand">
          <span class="brand-icon">◈</span>
          <div class="brand-text">
            <h1 class="brand-title">Bonus Point</h1>
            <span class="brand-sub">排除櫃台清單查詢系統</span>
          </div>
        </div>
        <div class="header-meta">
          <span class="meta-item">
            <i class="dot" :class="anySuccess ? 'dot--green' : 'dot--gray'"></i>
            {{ anySuccess ? '服務連線中' : '等待查詢' }}
          </span>
          <span class="meta-time" v-if="lastFetchTime">最後更新：{{ lastFetchTime }}</span>
        </div>
      </div>
    </header>

    <main class="app-main">

      <!-- 控制列 -->
      <section class="control-bar">
        <div class="control-inner">
          <div class="server-tabs">
            <button
              v-for="ep in API_ENDPOINTS"
              :key="ep.id"
              class="tab-btn"
              :class="{ 'tab-btn--active': activeTab === ep.id }"
              :style="activeTab === ep.id ? `--tab-color: ${ep.color}` : ''"
              @click="activeTab = ep.id"
            >
              <span class="tab-port">:{{ ep.port }}</span>
              <span class="tab-label">{{ ep.label }}</span>
              <span
                v-if="results[ep.id]"
                class="tab-status"
                :class="results[ep.id].success ? 'tab-status--ok' : 'tab-status--err'"
              >
                {{ results[ep.id].success ? '✓' : '✗' }}
              </span>
            </button>
          </div>

          <div class="action-buttons">
            <button
              class="btn btn--ghost"
              :disabled="loading"
              @click="fetchSingle(activeTab)"
            >
              <span class="btn-icon">↻</span>
              查詢當前
            </button>
            <button
              class="btn btn--primary"
              :disabled="loading"
              @click="fetchAll"
            >
              <span v-if="loading" class="spinner"></span>
              <span v-else class="btn-icon">⟳</span>
              {{ loading ? '查詢中...' : '查詢全部' }}
            </button>
          </div>
        </div>
      </section>

      <!-- API 路徑資訊 -->
      <section class="path-info" v-if="currentEndpoint">
        <div class="path-info-inner">
          <span class="path-method">GET</span>
          <span class="path-url">
            <span class="path-host">http://192.168.5.92:{{ currentEndpoint.port }}</span>
            <span class="path-route">{{ currentEndpoint.path }}</span>
          </span>
          <span class="path-desc">{{ currentEndpoint.description }}</span>
        </div>
      </section>

      <!-- 結果區域 -->
      <section class="results-area">
        <!-- 全部結果摘要 -->
        <div class="summary-bar" v-if="hasAnyResult">
          <div
            v-for="ep in API_ENDPOINTS"
            :key="ep.id"
            class="summary-card"
            :class="{
              'summary-card--active': activeTab === ep.id,
              'summary-card--success': results[ep.id]?.success,
              'summary-card--error': results[ep.id] && !results[ep.id].success,
            }"
            :style="`--card-color: ${ep.color}`"
            @click="activeTab = ep.id"
          >
            <div class="summary-header">
              <span class="summary-port">:{{ ep.port }}</span>
              <span v-if="results[ep.id]" class="summary-badge" :class="results[ep.id].success ? 'badge--success' : 'badge--error'">
                {{ results[ep.id].success ? results[ep.id].status : (results[ep.id].status || 'ERR') }}
              </span>
            </div>
            <div class="summary-stat" v-if="results[ep.id]">
              <span class="stat-duration">{{ results[ep.id].duration }}ms</span>
              <span class="stat-count" v-if="results[ep.id].success && Array.isArray(results[ep.id].data)">
                {{ results[ep.id].data.length }} 筆
              </span>
            </div>
            <div class="summary-loading" v-if="loadingStates[ep.id]">
              <span class="mini-spinner"></span>
            </div>
          </div>
        </div>

        <!-- 詳細結果 -->
        <div class="result-panel" v-if="currentResult">
          <!-- 成功 -->
          <div v-if="currentResult.success" class="result-success">
            <div class="result-header">
              <div class="result-title">
                <span class="status-badge status-badge--ok">{{ currentResult.status }}</span>
                <span>回應資料</span>
                <span class="result-count" v-if="Array.isArray(currentResult.data)">
                  共 {{ currentResult.data.length }} 筆記錄
                </span>
              </div>
              <span class="result-duration">耗時 {{ currentResult.duration }}ms</span>
            </div>

            <!-- 表格顯示（陣列資料）-->
            <div v-if="Array.isArray(currentResult.data) && currentResult.data.length > 0" class="data-table-wrap">
              <table class="data-table">
                <thead>
                  <tr>
                    <th v-for="col in tableColumns" :key="col"
                      @click="sortBy(col)"
                      class="th-sortable"
                    >
                      {{ col }}
                      <span class="sort-icon" v-if="sortColumn === col">
                        {{ sortDir === 'asc' ? '↑' : '↓' }}
                      </span>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(row, idx) in sortedData" :key="idx" class="data-row">
                    <td v-for="col in tableColumns" :key="col" class="data-cell">
                      <span class="cell-value">{{ formatCell(row[col]) }}</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <!-- 空陣列 -->
            <div v-else-if="Array.isArray(currentResult.data) && currentResult.data.length === 0" class="empty-state">
              <span class="empty-icon">◻</span>
              <p>此節點回傳空清單</p>
            </div>

            <!-- 非陣列資料：JSON 展示 -->
            <div v-else class="json-viewer">
              <pre class="json-pre">{{ formatJson(currentResult.data) }}</pre>
            </div>
          </div>

          <!-- 錯誤 -->
          <div v-else class="result-error">
            <div class="result-header">
              <div class="result-title">
                <span class="status-badge status-badge--err">{{ currentResult.status || 'ERROR' }}</span>
                <span>連線失敗</span>
              </div>
              <span class="result-duration">耗時 {{ currentResult.duration }}ms</span>
            </div>
            <div class="error-body">
              <div class="error-message">
                <span class="error-label">錯誤訊息</span>
                <code class="error-code">{{ currentResult.error }}</code>
              </div>
              <div class="error-detail" v-if="currentResult.detail">
                <span class="error-label">詳細內容</span>
                <pre class="json-pre">{{ formatJson(currentResult.detail) }}</pre>
              </div>
              <div class="error-tips">
                <p>💡 請確認：</p>
                <ul>
                  <li>服務 <code>192.168.5.92:{{ currentEndpoint?.port }}</code> 是否可連線</li>
                  <li>API 路徑是否正確</li>
                  <li>CORS 設定是否允許本地開發</li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        <!-- 初始狀態 -->
        <div class="initial-state" v-else-if="!loading">
          <div class="initial-inner">
            <span class="initial-icon">◈</span>
            <h3>尚未查詢</h3>
            <p>點擊「查詢全部」同時呼叫三個服務節點，<br>或選擇單一節點進行查詢。</p>
            <button class="btn btn--primary btn--lg" @click="fetchAll">
              開始查詢
            </button>
          </div>
        </div>

        <!-- 載入中 -->
        <div class="loading-state" v-else-if="loading && !currentResult">
          <div class="loading-inner">
            <span class="loading-ring"></span>
            <p>正在查詢服務節點...</p>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { API_ENDPOINTS, fetchExcludeCounterList, fetchAllEndpoints } from './api/bonusPointApi.js'

const activeTab = ref(API_ENDPOINTS[0].id)
const results = ref({})
const loadingStates = ref({})
const loading = ref(false)
const lastFetchTime = ref('')
const sortColumn = ref('')
const sortDir = ref('asc')

const currentEndpoint = computed(() =>
  API_ENDPOINTS.find(ep => ep.id === activeTab.value)
)

const currentResult = computed(() => results.value[activeTab.value])

const hasAnyResult = computed(() => Object.keys(results.value).length > 0)

const anySuccess = computed(() =>
  Object.values(results.value).some(r => r?.success)
)

const tableColumns = computed(() => {
  const data = currentResult.value?.data
  if (!Array.isArray(data) || data.length === 0) return []
  return Object.keys(data[0])
})

const sortedData = computed(() => {
  const data = currentResult.value?.data
  if (!Array.isArray(data)) return []
  if (!sortColumn.value) return data
  return [...data].sort((a, b) => {
    const va = a[sortColumn.value]
    const vb = b[sortColumn.value]
    const cmp = String(va).localeCompare(String(vb), 'zh-TW', { numeric: true })
    return sortDir.value === 'asc' ? cmp : -cmp
  })
})

function sortBy(col) {
  if (sortColumn.value === col) {
    sortDir.value = sortDir.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortColumn.value = col
    sortDir.value = 'asc'
  }
}

function formatCell(val) {
  if (val === null || val === undefined) return '—'
  if (typeof val === 'object') return JSON.stringify(val)
  return String(val)
}

function formatJson(data) {
  try {
    return JSON.stringify(data, null, 2)
  } catch {
    return String(data)
  }
}

function updateTime() {
  const now = new Date()
  lastFetchTime.value = now.toLocaleTimeString('zh-TW')
}

async function fetchSingle(endpointId) {
  const ep = API_ENDPOINTS.find(e => e.id === endpointId)
  if (!ep) return
  loadingStates.value[endpointId] = true
  const res = await fetchExcludeCounterList(ep)
  results.value[endpointId] = res
  loadingStates.value[endpointId] = false
  updateTime()
}

async function fetchAll() {
  loading.value = true
  loadingStates.value = {}
  API_ENDPOINTS.forEach(ep => { loadingStates.value[ep.id] = true })

  const allResults = await fetchAllEndpoints()
  allResults.forEach(res => {
    results.value[res.endpoint.id] = res
    loadingStates.value[res.endpoint.id] = false
  })

  loading.value = false
  updateTime()
}
</script>

<style>
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

:root {
  --bg: #0D0F14;
  --bg-2: #13161E;
  --bg-3: #1A1F2B;
  --bg-4: #222736;
  --border: #2A3045;
  --border-light: #353D55;
  --text: #E8ECF4;
  --text-muted: #6B7694;
  --text-dim: #3D4560;
  --accent: #4ECDC4;
  --accent-2: #FFD93D;
  --accent-3: #FF6B9D;
  --success: #4ADE80;
  --error: #F87171;
  --font: 'Noto Sans TC', sans-serif;
  --mono: 'JetBrains Mono', monospace;
  --radius: 8px;
  --radius-lg: 14px;
}

body {
  background: var(--bg);
  color: var(--text);
  font-family: var(--font);
  font-size: 14px;
  line-height: 1.6;
  min-height: 100vh;
}

/* ── Header ── */
.app-header {
  background: var(--bg-2);
  border-bottom: 1px solid var(--border);
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-inner {
  max-width: 1300px;
  margin: 0 auto;
  padding: 14px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.brand { display: flex; align-items: center; gap: 12px; }
.brand-icon {
  font-size: 28px;
  color: var(--accent);
  line-height: 1;
  filter: drop-shadow(0 0 8px var(--accent));
}
.brand-title { font-size: 18px; font-weight: 700; letter-spacing: 0.04em; }
.brand-sub { font-size: 11px; color: var(--text-muted); letter-spacing: 0.08em; }
.header-meta { display: flex; align-items: center; gap: 16px; font-size: 12px; color: var(--text-muted); }
.meta-item { display: flex; align-items: center; gap: 6px; }
.dot { display: inline-block; width: 7px; height: 7px; border-radius: 50%; }
.dot--green { background: var(--success); box-shadow: 0 0 6px var(--success); animation: pulse 2s infinite; }
.dot--gray { background: var(--text-dim); }
@keyframes pulse { 0%, 100% { opacity: 1; } 50% { opacity: 0.4; } }

/* ── Main ── */
.app-main { max-width: 1300px; margin: 0 auto; padding: 24px; }

/* ── Control Bar ── */
.control-bar {
  background: var(--bg-2);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  margin-bottom: 16px;
}
.control-inner {
  padding: 12px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
}
.server-tabs { display: flex; gap: 6px; flex-wrap: wrap; }
.tab-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: var(--bg-3);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  color: var(--text-muted);
  cursor: pointer;
  font-family: var(--font);
  font-size: 13px;
  transition: all 0.2s;
}
.tab-btn:hover { border-color: var(--border-light); color: var(--text); }
.tab-btn--active {
  background: color-mix(in srgb, var(--tab-color, var(--accent)) 12%, transparent);
  border-color: var(--tab-color, var(--accent));
  color: var(--tab-color, var(--accent));
}
.tab-port { font-family: var(--mono); font-size: 12px; font-weight: 600; }
.tab-status { font-size: 11px; font-weight: 700; }
.tab-status--ok { color: var(--success); }
.tab-status--err { color: var(--error); }

.action-buttons { display: flex; gap: 8px; }
.btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 18px;
  border-radius: var(--radius);
  font-family: var(--font);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid transparent;
}
.btn:disabled { opacity: 0.45; cursor: not-allowed; }
.btn--ghost { background: transparent; border-color: var(--border-light); color: var(--text-muted); }
.btn--ghost:hover:not(:disabled) { border-color: var(--accent); color: var(--accent); }
.btn--primary { background: var(--accent); color: #0D0F14; font-weight: 700; }
.btn--primary:hover:not(:disabled) { background: color-mix(in srgb, var(--accent) 85%, white); }
.btn--lg { padding: 12px 28px; font-size: 15px; }
.btn-icon { font-size: 15px; }

/* ── Path Info ── */
.path-info {
  background: var(--bg-3);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  margin-bottom: 16px;
  overflow: hidden;
}
.path-info-inner {
  padding: 10px 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}
.path-method {
  font-family: var(--mono);
  font-size: 11px;
  font-weight: 700;
  color: var(--accent-2);
  background: color-mix(in srgb, var(--accent-2) 10%, transparent);
  border: 1px solid color-mix(in srgb, var(--accent-2) 30%, transparent);
  border-radius: 4px;
  padding: 2px 8px;
  flex-shrink: 0;
}
.path-host { color: var(--text-muted); font-family: var(--mono); font-size: 13px; }
.path-route { color: var(--text); font-family: var(--mono); font-size: 13px; }
.path-desc { margin-left: auto; font-size: 12px; color: var(--text-dim); }

/* ── Summary Cards ── */
.summary-bar { display: flex; gap: 12px; margin-bottom: 16px; flex-wrap: wrap; }
.summary-card {
  flex: 1;
  min-width: 160px;
  background: var(--bg-2);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 14px 16px;
  cursor: pointer;
  transition: all 0.2s;
}
.summary-card:hover { border-color: var(--border-light); }
.summary-card--active { border-color: var(--card-color, var(--accent)) !important; background: color-mix(in srgb, var(--card-color, var(--accent)) 6%, var(--bg-2)); }
.summary-card--success .summary-port { color: var(--success); }
.summary-card--error .summary-port { color: var(--error); }
.summary-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 6px; }
.summary-port { font-family: var(--mono); font-weight: 700; font-size: 16px; color: var(--text-muted); }
.summary-badge { font-size: 11px; font-weight: 700; padding: 2px 7px; border-radius: 4px; }
.badge--success { background: color-mix(in srgb, var(--success) 15%, transparent); color: var(--success); }
.badge--error { background: color-mix(in srgb, var(--error) 15%, transparent); color: var(--error); }
.summary-stat { display: flex; gap: 8px; align-items: center; }
.stat-duration { font-family: var(--mono); font-size: 12px; color: var(--text-muted); }
.stat-count { font-size: 12px; color: var(--accent-2); }
.summary-loading { display: flex; align-items: center; }

/* ── Result Panel ── */
.result-panel {
  background: var(--bg-2);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  overflow: hidden;
}
.result-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  border-bottom: 1px solid var(--border);
  background: var(--bg-3);
}
.result-title { display: flex; align-items: center; gap: 10px; font-weight: 500; }
.result-count { font-size: 12px; color: var(--text-muted); }
.result-duration { font-family: var(--mono); font-size: 12px; color: var(--text-muted); }
.status-badge {
  font-family: var(--mono);
  font-size: 11px;
  font-weight: 700;
  padding: 3px 9px;
  border-radius: 5px;
}
.status-badge--ok { background: color-mix(in srgb, var(--success) 15%, transparent); color: var(--success); }
.status-badge--err { background: color-mix(in srgb, var(--error) 15%, transparent); color: var(--error); }

/* ── Table ── */
.data-table-wrap { overflow-x: auto; }
.data-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.data-table th {
  text-align: left;
  padding: 10px 16px;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--text-muted);
  background: var(--bg-3);
  border-bottom: 1px solid var(--border);
  white-space: nowrap;
}
.th-sortable { cursor: pointer; user-select: none; }
.th-sortable:hover { color: var(--accent); }
.sort-icon { color: var(--accent); margin-left: 4px; }
.data-row:hover { background: var(--bg-3); }
.data-row:not(:last-child) td { border-bottom: 1px solid var(--border); }
.data-cell { padding: 10px 16px; vertical-align: middle; }
.cell-value { font-family: var(--mono); font-size: 12px; color: var(--text); }

/* ── JSON ── */
.json-viewer { padding: 20px; }
.json-pre {
  background: var(--bg-3);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 16px;
  font-family: var(--mono);
  font-size: 12px;
  color: var(--accent);
  overflow-x: auto;
  line-height: 1.7;
  white-space: pre;
}

/* ── Error ── */
.result-error {}
.error-body { padding: 20px; display: flex; flex-direction: column; gap: 16px; }
.error-message, .error-detail { display: flex; flex-direction: column; gap: 6px; }
.error-label { font-size: 11px; font-weight: 600; letter-spacing: 0.06em; text-transform: uppercase; color: var(--text-muted); }
.error-code {
  font-family: var(--mono);
  font-size: 13px;
  color: var(--error);
  background: color-mix(in srgb, var(--error) 8%, transparent);
  border: 1px solid color-mix(in srgb, var(--error) 20%, transparent);
  border-radius: var(--radius);
  padding: 10px 14px;
}
.error-tips {
  background: var(--bg-3);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 14px 16px;
  font-size: 13px;
  color: var(--text-muted);
}
.error-tips p { margin-bottom: 8px; color: var(--text); }
.error-tips ul { padding-left: 20px; display: flex; flex-direction: column; gap: 4px; }
.error-tips code {
  font-family: var(--mono);
  font-size: 12px;
  color: var(--accent-2);
  background: color-mix(in srgb, var(--accent-2) 10%, transparent);
  border-radius: 3px;
  padding: 1px 5px;
}

/* ── Empty / Initial / Loading ── */
.empty-state, .initial-state, .loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
}
.empty-state { flex-direction: column; gap: 10px; color: var(--text-dim); padding: 40px; }
.empty-icon { font-size: 36px; }
.initial-inner, .loading-inner { text-align: center; display: flex; flex-direction: column; align-items: center; gap: 12px; }
.initial-icon { font-size: 48px; color: var(--accent); filter: drop-shadow(0 0 12px var(--accent)); }
.initial-inner h3 { font-size: 18px; font-weight: 600; }
.initial-inner p { color: var(--text-muted); font-size: 14px; line-height: 1.7; }
.loading-ring {
  width: 40px; height: 40px;
  border: 3px solid var(--border);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}
.loading-inner p { color: var(--text-muted); }
@keyframes spin { to { transform: rotate(360deg); } }

/* ── Spinners ── */
.spinner {
  width: 14px; height: 14px;
  border: 2px solid rgba(0,0,0,0.3);
  border-top-color: rgba(0,0,0,0.8);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
  display: inline-block;
}
.mini-spinner {
  width: 14px; height: 14px;
  border: 2px solid var(--border);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
  display: inline-block;
}

@media (max-width: 768px) {
  .app-main { padding: 12px; }
  .control-inner { flex-direction: column; align-items: stretch; }
  .action-buttons { justify-content: flex-end; }
  .summary-bar { flex-direction: column; }
  .path-desc { display: none; }
}
</style>
