import apiClient from './axios'

export const assetApi = {
  getInventoryDate() {
    return apiClient.post('/getInventoryDate')
  },
  getAssetInventory(params) {
    return apiClient.post('/getAssetInventory', params)
  },
  getAssetType() {
    return apiClient.get('/getAssetType', { responseType: 'text' })
  },
  uploadExcel(file) {
    const fd = new FormData()
    fd.append('file', file)
    return apiClient.post('/uploadExcel', fd, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  saveImport(data) {
    return apiClient.post('/saveImport', { asset_inventory_data: data })
  },
  saveAsset(inventoryDate, data) {
    return apiClient.post('/saveAsset', {
      inventory_date: inventoryDate,
      asset_inventory_data: data
    })
  },
  deleteAll(inventoryDate) {
    return apiClient.post('/deleteAll', { inventory_date: inventoryDate })
  },
  delete(inventoryDate, selfNo) {
    return apiClient.post('/delete', { inventory_date: inventoryDate, self_no: selfNo })
  },
  scrapped(inventoryDate, selfNo) {
    return apiClient.post('/scrapped', { inventory_date: inventoryDate, self_no: selfNo })
  },
  newAsset() {
    return apiClient.post('/newAsset')
  },
  getStaffAsset(params) {
    return apiClient.post('/getStaffAsset', params)
  },
  getAssetScrapped(params) {
    return apiClient.post('/getAssetScrapped', params)
  },
  submit() {
    return apiClient.post('/submit')
  },
  inform() {
    return apiClient.post('/inform')
  },
  genOffPunchExcel(params) {
    return apiClient.post('/genOffPunchExcel', params)
  }
}
