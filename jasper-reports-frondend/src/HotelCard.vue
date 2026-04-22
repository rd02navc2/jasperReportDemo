<template>
  <div class="hotel-card" @click="handleClick">
    <!-- Image -->
    <div class="card-image-container">
      <img
        :src="imageUrl"
        :alt="hotel?.name || 'hotel image'"
        class="card-image"
        loading="lazy"
      />

      <div class="rating-badge" v-if="hotel?.rating">
        <Star :size="14" fill="#fbbf24" stroke="none" />
        <span>{{ hotel.rating }}</span>
      </div>
    </div>

    <!-- Content -->
    <div class="card-content">
      <h3 class="card-title">
        {{ hotel?.name || 'Unknown Hotel' }}
      </h3>

      <div class="card-location">
        <MapPin :size="14" class="location-icon" />
        {{ locationText }}
      </div>

      <p class="card-description" v-if="hotel?.description">
        {{ shortDescription }}
      </p>

      <!-- Amenities -->
      <div class="card-amenities" v-if="amenities.length">
        <span
          v-for="(amenity, index) in amenities"
          :key="index"
          class="amenity-tag"
        >
          {{ amenity }}
        </span>
      </div>

      <!-- Footer -->
      <div class="card-footer">
        <div class="price-info">
          <span class="currency">₹</span>
          <span class="amount">{{ hotel?.pricePerNight ?? 0 }}</span>
          <span class="period">/night</span>
        </div>

        <button class="book-btn" @click.stop="handleClick">
          View Details
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Star, MapPin } from 'lucide-vue-next'

const props = defineProps({
  hotel: {
    type: Object,
    required: true,
    default: () => ({})
  }
})

const emit = defineEmits(['click'])

/** image fallback */
const imageUrl = computed(() => {
  const images = props.hotel?.images
  return Array.isArray(images) && images.length > 0
    ? images[0]
    : 'https://images.unsplash.com/photo-1566073771259-6a8506099945'
})

/** safe description */
const shortDescription = computed(() => {
  const desc = props.hotel?.description || ''
  return desc.length > 100 ? desc.slice(0, 100) + '...' : desc
})

/** safe amenities */
const amenities = computed(() => {
  return Array.isArray(props.hotel?.amenities)
    ? props.hotel.amenities.slice(0, 3)
    : []
})

/** location safe text */
const locationText = computed(() => {
  const city = props.hotel?.city || ''
  const state = props.hotel?.state || ''
  return [city, state].filter(Boolean).join(', ') || 'Unknown location'
})

/** click handler */
const handleClick = () => {
  emit('click', props.hotel)
}
</script>

<style scoped>
.hotel-card {
  width: 320px;
  border-radius: 16px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 6px 20px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.hotel-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 28px rgba(0,0,0,0.12);
}

.card-image-container {
  position: relative;
  height: 180px;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.rating-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(0,0,0,0.7);
  color: #fff;
  padding: 4px 8px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.card-content {
  padding: 14px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 6px;
}

.card-location {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
}

.card-description {
  font-size: 13px;
  color: #777;
  margin-bottom: 10px;
}

.card-amenities {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.amenity-tag {
  font-size: 11px;
  background: #f3f4f6;
  padding: 3px 8px;
  border-radius: 999px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price-info {
  font-weight: 600;
}

.currency {
  color: #16a34a;
}

.amount {
  font-size: 16px;
}

.period {
  font-size: 12px;
  color: #666;
}

.book-btn {
  background: #2563eb;
  color: white;
  border: none;
  padding: 6px 10px;
  border-radius: 8px;
  cursor: pointer;
}

.book-btn:hover {
  background: #1d4ed8;
}
</style>