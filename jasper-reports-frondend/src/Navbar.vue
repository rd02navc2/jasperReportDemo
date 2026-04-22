<template>
  <nav class="navbar">
    <div class="container navbar-content">
      <!-- Logo -->
      <router-link to="/" class="logo" @click="closeAllMenus">
        <Building2 class="icon" />
        <span>LuxeStays India</span>
      </router-link>

      <!-- Desktop Nav -->
      <div :class="['nav-links', { active: isMenuOpen }]">
        <router-link to="/" @click="closeAllMenus">Home</router-link>

        <router-link
          v-if="user"
          to="/bookings"
          @click="closeAllMenus"
        >
          My Bookings
        </router-link>

        <!-- User Menu -->
        <div v-if="user" class="user-menu-wrapper">
          <button class="user-menu-btn" @click="toggleUserMenu">
            <span class="user-avatar">
              {{ userInitial }}
            </span>
            <span class="user-name">{{ user?.name }}</span>
            <ChevronDown :size="14" />
          </button>

          <div v-if="isUserMenuOpen" class="user-dropdown">
            <div class="dropdown-item user-info">
              <small>{{ user?.email }}</small>
            </div>

            <button @click="handleLogout" class="dropdown-item text-red">
              <LogOut :size="16" />
              Logout
            </button>
          </div>
        </div>

        <!-- Login -->
        <router-link
          v-else
          to="/login"
          class="btn-primary"
          @click="closeAllMenus"
        >
          <User :size="18" />
          Sign In
        </router-link>
      </div>

      <!-- Mobile Menu Button -->
      <button class="mobile-menu" @click="toggleMobileMenu">
        <Menu />
      </button>
    </div>
  </nav>

  <!-- Backdrop -->
  <div
    v-if="isMenuOpen || isUserMenuOpen"
    class="backdrop"
    @click="closeAllMenus"
  />
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import {
  Building2,
  User,
  Menu,
  LogOut,
  ChevronDown
} from 'lucide-vue-next'
import { useAuth } from '../stores/auth'

const router = useRouter()
const { user, logout } = useAuth()

const isMenuOpen = ref(false)
const isUserMenuOpen = ref(false)

/** computed safe initial */
const userInitial = computed(() => {
  return user?.name?.charAt(0)?.toUpperCase() || 'U'
})

/** toggle mobile menu */
const toggleMobileMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
  isUserMenuOpen.value = false
}

/** toggle user dropdown */
const toggleUserMenu = () => {
  isUserMenuOpen.value = !isUserMenuOpen.value
  isMenuOpen.value = false
}

/** close everything */
const closeAllMenus = () => {
  isMenuOpen.value = false
  isUserMenuOpen.value = false
}

/** logout */
const handleLogout = async () => {
  await logout()
  closeAllMenus()
  router.push('/login')
}

/** ESC close */
const handleKeydown = (e) => {
  if (e.key === 'Escape') {
    closeAllMenus()
  }
}

onMounted(() => {
  window.addEventListener('keydown', handleKeydown)
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleKeydown)
})
</script>