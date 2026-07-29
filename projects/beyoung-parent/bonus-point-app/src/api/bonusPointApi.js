import axios from 'axios'

// API 端點配置
export const API_ENDPOINTS = [
  {
    id: 'server-8080',
    label: 'Server 8080',
    port: 8080,
    proxyBase: '/api-8080',
    path: '/Surrounding/api/bonus/Point/excludeCounter/list',
    color: '#4ECDC4',
    description: '主要服務節點'
  },
  {
    id: 'server-8085',
    label: 'Server 8085',
    port: 8085,
    proxyBase: '/api-8085',
    path: '/Surrounding/api/bonus/Point/excludeCounter/list',
    color: '#FFD93D',
    description: '備援服務節點'
  },
  {
    id: 'server-8095',
    label: 'Server 8095',
    port: 8095,
    proxyBase: '/api-8095',
    path: '/Surrounding/api//bonus/Point/excludeCounter/list',
    color: '#FF6B9D',
    description: '擴充服務節點'
  }
]

// 建立 axios instance
const createApiInstance = (proxyBase) => {
  return axios.create({
    baseURL: proxyBase,
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 呼叫單一 API
export const fetchExcludeCounterList = async (endpoint) => {
  const instance = createApiInstance(endpoint.proxyBase)
  const startTime = Date.now()
  
  try {
    const response = await instance.get(endpoint.path)
    const duration = Date.now() - startTime
    return {
      success: true,
      data: response.data,
      status: response.status,
      duration,
      endpoint
    }
  } catch (error) {
    const duration = Date.now() - startTime
    return {
      success: false,
      error: error.message,
      status: error.response?.status || null,
      duration,
      endpoint,
      detail: error.response?.data || null
    }
  }
}

// 呼叫全部 API
export const fetchAllEndpoints = async () => {
  const promises = API_ENDPOINTS.map(ep => fetchExcludeCounterList(ep))
  return Promise.all(promises)
}
