"use client"

import { useState } from "react"
import { useAuth } from "@/context/AuthContext"
import { API_URL, API_ENDPOINTS } from "@/config/api"
import { MealType } from "@/types/meals"

export interface NewIngredient {
  name: string
  calories: number
  protein: number
  carbs: number
  fat: number
  mealType: MealType,
  date: Date,
}

export function useAddCustomIngredient(mealId: number) {
  const { accessToken } = useAuth()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const addCustomIngredient = async (ingredient: NewIngredient) => {
    setLoading(true)
    setError(null)

    try {
      const res = await fetch(`${API_URL}${API_ENDPOINTS.mealIngredients}/add-custom?mealId=${mealId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify(ingredient),
      })

      if (!res.ok) throw new Error("Nie udało się dodać składnika")

      return await res.json()
    } catch (err: unknown) {
      if (err instanceof Error) setError(err.message)
      else setError("Nieznany błąd")
      throw err
    } finally {
      setLoading(false)
    }
  }

  return { addCustomIngredient, loading, error }
}
