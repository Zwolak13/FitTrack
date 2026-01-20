"use client"

import { useEffect, useState } from "react"
import { API_URL, API_ENDPOINTS } from "@/config/api"
import { useAuth } from "@/context/AuthContext"
import { fetchWithAuth } from "@/lib/fetchWithAuth"

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
  const { accessToken, setAccessToken } = useAuth()
  const [ingredients, setIngredients] = useState<Ingredient[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!accessToken) return

    const controller = new AbortController()

    const run = async () => {
      try {
        setLoading(true)
        setError(null)

        const base =
          type === "mine"
            ? `${API_URL}${API_ENDPOINTS.ingredients}/mine`
            : `${API_URL}${API_ENDPOINTS.ingredients}`

        const url = query ? `${base}?q=${encodeURIComponent(query)}` : base

        const res = (await fetchWithAuth(
          url,
          { signal: controller.signal },
          () => accessToken,
          setAccessToken
        )) as Response

        if (!res.ok) throw new Error("Fetch failed")

        setIngredients(await res.json())
      } catch (e: any) {
        if (e.name !== "AbortError") setError(e.message)
      } finally {
        setLoading(false)
      }
    }

    run()
    return () => controller.abort()
  }, [type, query, accessToken, setAccessToken])

  return { ingredients, loading, error }
}
