"use client"

import { useQuery } from "@tanstack/react-query"
import { useAuth } from "@/context/AuthContext"
import { API_URL, API_ENDPOINTS } from "@/config/api"

export interface Ingredient {
  id: number
  name: string
  unit: string
  caloriesPer100g: number
  proteinPer100g: number
  carbsPer100g: number
  fatPer100g: number
  gramsPerUnit: number
  isGlobal: boolean
}

export function useFetchIngredients(type: "all" | "mine", query?: string) {
  const { accessToken } = useAuth()

  return useQuery<Ingredient[], Error>({
    queryKey: ["ingredients", type, query],
    queryFn: async () => {
      if (!accessToken) throw new Error("No access token")

      const base =
        type === "mine"
          ? `${API_URL}${API_ENDPOINTS.ingredients}/mine`
          : `${API_URL}${API_ENDPOINTS.ingredients}`

      const url = query ? `${base}?q=${encodeURIComponent(query)}` : base

      const res = await fetch(url, {
        headers: { Authorization: `Bearer ${accessToken}` },
      })

      if (!res.ok) throw new Error("Fetch failed")
      return res.json()
    },
    staleTime: 1000 * 60,
  })
}
