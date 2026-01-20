"use client"

import { useAuth } from "@/context/AuthContext"
import { API_URL, API_ENDPOINTS } from "@/config/api"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { MealType } from "@/types/meals"

export interface NewIngredient {
  name: string
  calories: number
  protein: number
  carbs: number
  fat: number
  mealType: MealType
  date: Date
}

export function useAddCustomIngredient(mealId: number) {
  const { accessToken } = useAuth()
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (ingredient: NewIngredient) => {
      const res = await fetch(`${API_URL}${API_ENDPOINTS.mealIngredients}/add-custom?mealId=${mealId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify(ingredient),
      })

      if (!res.ok) throw new Error("Nie udało się dodać składnika")
      return res.json()
    },
    onSuccess: (_, ingredient) => {
      queryClient.invalidateQueries({
        queryKey: ['dailyLog', ingredient.date.toISOString().slice(0, 10)]
      })

    },
  })
}
