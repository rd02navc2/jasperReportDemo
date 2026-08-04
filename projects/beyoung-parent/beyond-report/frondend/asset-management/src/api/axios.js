import axios from 'axios'

const apiClient = axios.create({
  // 1. 指定 Spring Boot 的完整位址（含 Port 8095）
  baseURL: 'http://localhost:8095/Report/rest/Asset',
  timeout: 30000,
  headers: { 
    'Content-Type': 'application/json;charset=UTF-8' 
  }
})

// 2. 響應攔截器：直接取出後端回傳的 JSON 物件
apiClient.interceptors.response.use(
  response => response.data, // 這裡已經幫你剥掉外層的 Axios Response 物件
  error => {
    console.error('API Error:', error)
    return Promise.reject(error.response?.data || error)
  }
)

const service = axios.create({
  baseURL: 'http://localhost:8095', // 或你的 API Base URL
  timeout: 5000,
  withCredentials: true // 關鍵：必須開啟，這樣發送 API 時才會自動附帶 Cookie/Session
})

axios.defaults.withCredentials = true; // 允許 Axios 跨域攜帶 Session Cookie

export default apiClient