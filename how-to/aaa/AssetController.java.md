<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>

# AssetController.java

我已找到 `AssetController.java` 的主要 API 行為，這份 controller 不是典型 REST 風格，而是以 `POST + JSON body + session login` 為主，且部分回傳格式是 `success / rows / total / message` 的混合型。[^1][^2]

## 你這支 Controller 的重點

這個 `AssetController` 主要提供：

- `getInventoryDate`：查盤點日期清單。[^2][^1]
- `getAssetInventory`：依 `inventorydate` 查資產明細，支援 `sidx/sord` 排序。[^1][^2]
- `getAssetType`：回傳 HTML `<select>` 字串，不是 JSON。[^2][^1]
- `uploadExcel`：上傳 Excel 匯入資產資料。[^1][^2]
- `saveImport`：把 Excel 預覽資料寫入系統。[^2][^1]
- `saveAsset`：新增/儲存資產盤點資料。[^1][^2]
- `deleteAll`：刪除某個 inventoryDate 全部資料。[^2][^1]
- `delete`：刪除單筆資產。[^1][^2]
- `scrapped`：報廢單筆資產。[^2][^1]
- `newAsset`：建立新資產盤點批次，並寄信通知。[^1][^2]
- `getStaffAsset`：查詢登入者自己的資產。[^2][^1]
- `getAssetScrapped`：查詢報廢清單。[^1][^2]
- `submit`：送出審核 / 提交流程。[^2][^1]
- `inform`：寄發通知信。[^1][^2]
- `genOffPunchExcel`：產生 Excel 檔案，回傳檔案路徑與名稱。[^2][^1]


## 對前端的影響

你的 Vue 前端不能完全照一般 REST API 假設來寫，因為這支 controller 有幾個特徵：

1. 很多 API 是 `POST`，而且直接讀 `request.getInputStream()`。[^1][^2]
2. 驗證依賴 session login，未登入會有 `401` 或錯誤訊息。[^2][^1]
3. 回傳格式不是統一 DTO，有些是 `{ success, rows }`，有些是 `{ success, message }`，有些是 `{ success, errorMessage }`。[^1][^2]
4. `getAssetType` 回的是 HTML `<select>`，前端其實不一定要直接呼叫，建議改成 JSON。[^2][^1]

## 建議前端 API 封裝

我建議你在前端做一層 adapter，把後端這些舊式 API 包起來，對 Vue 元件暴露乾淨的介面。

### `src/api/asset.ts`

```ts
import http from './http'

export const assetApi = {
  getInventoryDate: () => http.post('/asset/getInventoryDate', {}),
  getAssetInventory: (payload: { inventorydate: string; sidx?: string; sord?: string }) =>
    http.post('/asset/getAssetInventory', payload),
  uploadExcel: (file: File) => {
    const form = new FormData()
    form.append('file', file)
    return http.post('/asset/uploadExcel', form, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  saveImport: (payload: { assetinventorydata: any[] }) =>
    http.post('/asset/saveImport', payload),
  saveAsset: (payload: { inventorydate: string; assetinventorydata: any[] }) =>
    http.post('/asset/saveAsset', payload),
  deleteAll: (inventorydate: string) =>
    http.post('/asset/deleteAll', { inventorydate }),
  deleteOne: (inventorydate: string, selfno: string) =>
    http.post('/asset/delete', { inventorydate, selfno }),
  scrapped: (inventorydate: string, selfno: string) =>
    http.post('/asset/scrapped', { inventorydate, selfno }),
  newAsset: () => http.post('/asset/newAsset', {}),
  getStaffAsset: (payload?: { sidx?: string; sord?: string }) =>
    http.post('/asset/getStaffAsset', payload ?? {}),
  getAssetScrapped: (payload: {
    salesdate?: string
    saleedate?: string
    selfno?: string
    sidx?: string
    sord?: string
  }) => http.post('/asset/getAssetScrapped', payload),
  submit: () => http.post('/asset/submit', {}),
  inform: () => http.post('/asset/inform', {})
}
```


## 前端資料模型建議

依照 controller 實際欄位，你的資產列表至少應該對應這些欄位：

```ts
export interface AssetInventoryRow {
  inventorydate?: string
  selfno?: string
  modelno?: string
  type?: string
  proddesc?: string
  ownerid?: string
  ownername?: string
  title?: string
  deptname?: string
  confirmid?: string
  confirmdate?: string
  maintainhist?: string
  memohist?: string
  scrappedid?: string
  scrappeddate?: string
}
```


## 建議的前端頁面對應

### 1. 資產盤點主頁

- 先查 `getInventoryDate`。
- 選一個 inventoryDate 後查 `getAssetInventory`。
- 顯示表格、查詢、匯出、刪除、報廢、提交。


### 2. 我的資產頁

- 呼叫 `getStaffAsset`。
- 顯示自己的資產狀態與確認日期。


### 3. 報廢管理頁

- 呼叫 `getAssetScrapped`。
- 支援日期區間、資產編號查詢。


### 4. Excel 匯入頁

- 上傳 Excel → `uploadExcel`
- 預覽資料 → `saveImport`
- 確認入庫 → `saveAsset`


## 路由設計建議

```ts
export const routes = [
  { path: '/login', component: () => import('@/views/auth/LoginView.vue'), meta: { public: true } },
  { path: '/', component: () => import('@/layouts/MainLayout.vue'), children: [
    { path: 'dashboard', component: () => import('@/views/dashboard/DashboardView.vue') },
    { path: 'inventory', component: () => import('@/views/inventory/InventoryView.vue') },
    { path: 'inventory/import', component: () => import('@/views/inventory/ImportView.vue') },
    { path: 'inventory/staff', component: () => import('@/views/inventory/StaffAssetView.vue') },
    { path: 'scrapped', component: () => import('@/views/scrapped/ScrappedView.vue') },
    { path: 'system/logs', component: () => import('@/views/system/AuditLogView.vue') }
  ]}
]
```


## 我下一步可以直接幫你做的

我可以接著直接產出以下任一種：

1. **完全對應這支 `AssetController` 的 Vue 前端完整專案骨架**。
2. **資產盤點主頁完整 SFC**，含 inventory date 切換、查詢表格、操作按鈕。
3. **Excel 匯入頁完整 SFC**。
4. **按這支 controller API 寫好的 `assetApi.ts` 完整版本**。
5. **把這支 controller 的欄位整理成前端型別檔 `types/asset.ts`**。

如果你要，我建議下一則我直接幫你輸出：

**「對應 `AssetController.java` 的完整 Vue3 Vite 專案骨架 + 盤點首頁 + 資產列表頁 + API 層」**

我可以一次給你可貼進專案的完整程式碼。

<div align="center">⁂</div>

[^1]: AssetController.java

[^2]: AssetController.java

