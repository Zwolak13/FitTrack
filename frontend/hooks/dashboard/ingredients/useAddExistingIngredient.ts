"use client"

import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useAuth } from "@/context/AuthContext"
import { API_URL } from "@/config/api"

export interface AddExistingIngredientPayload {
  mealId: number
  ingredientId: number
  quantity: number
  date: string
}

export function useAddExistingIngredient() {
  const { accessToken } = useAuth()
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (data: AddExistingIngredientPayload) => {
      if (!accessToken) throw new Error("No access token")

      const res = await fetch(
        `${API_URL}/meal-ingredients/add?mealId=${data.mealId}&ingredientId=${data.ingredientId}&quantity=${data.quantity}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        }
      )

      if (!res.ok) throw new Error("Failed to add ingredient")
      return res.json()
    },
    onSuccess: (_data, variables) => {
      queryClient.invalidateQueries({ queryKey: ['dailyLog', variables.date] })

    },
  })
}
