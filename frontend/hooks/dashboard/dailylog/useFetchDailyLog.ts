"use client";

import { useQuery } from '@tanstack/react-query'
import { useAuth } from '@/context/AuthContext'
import { API_URL, API_ENDPOINTS } from '@/config/api'

export function useFetchDailyLog(date: string) {
  const { accessToken } = useAuth()

  return useQuery({
    queryKey: ['dailyLog', date],
    queryFn: async () => {
      if (!accessToken) throw new Error('No access token')

      const res = await fetch(`${API_URL}${API_ENDPOINTS.dailyLog}/${date}`, {
        headers: { Authorization: `Bearer ${accessToken}` },
      })
      if (!res.ok) throw new Error('Failed to fetch daily log')
      return res.json()
    },
    staleTime: 1000 * 250, 
  })
}


