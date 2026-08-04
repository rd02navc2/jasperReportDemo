import { defineStore } from 'pinia'
import { assetApi } from '@/api/asset'

export const useAssetStore = defineStore('asset', {
  state: () => ({
    inventoryDates: [],
    currentInventoryDate: '',
    assetList: [],
    myAssets: [],
    scrappedList: [],
    loading: false
  }),
  actions: {
    async fetchInventoryDates() {
      this.loading = true
      try {
        const res = await assetApi.getInventoryDate()
        if (res.Success === 'Y') {
          this.inventoryDates = res.rows || res.data || []
        }
      } catch (error) {
        console.error('获取盘点日期失败:', error)
      } finally { 
        this.loading = false 
      }
    },

    async fetchAssetInventory(inventoryDate, sidx = 'self_no', sord = 'ASC') {
      // 容錯機制：如果傳進來的是物件，自動解析出 date
      const dateStr = typeof inventoryDate === 'object' ? inventoryDate.inventory_date : inventoryDate

      if (!dateStr) {
        console.warn('fetchAssetInventory: 未提供有效的 inventoryDate')
        return
      }

      this.loading = true
      try {
        // 請確認 assetApi.getAssetInventory 是接收物件還是單一字串
        const res = await assetApi.getAssetInventory({ 
          inventory_date: dateStr, 
          sidx, 
          sord 
        })

        console.log('API 回傳結果:', res) // 💡 建議加上 console 觀察後端回傳結構

        if (res.Success === 'Y') {
          // 防護措施：支援 res.rows 或 res.data，若無則給予空陣列 []
          this.assetList = res.rows || res.data || []
          this.currentInventoryDate = dateStr
        } else {
          this.assetList = []
        }
      } catch (error) {
        console.error('获取资产明细失败:', error)
        this.assetList = []
        throw error // 拋出異常讓 View 層可以 catch 處理
      } finally { 
        this.loading = false 
      }
    },

    async fetchMyAssets(sidx = 'self_no', sord = 'ASC') {
      this.loading = true
      try {
        const res = await assetApi.getStaffAsset({ sidx, sord })
        if (res.Success === 'Y') {
          this.myAssets = res.rows || res.data || []
        }
      } catch (error) {
        console.error('获取个人资产失败:', error)
      } finally { 
        this.loading = false 
      }
    }
  }
})