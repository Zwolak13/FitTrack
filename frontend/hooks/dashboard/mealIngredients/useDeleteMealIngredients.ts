"use client"

import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useAuth } from "@/context/AuthContext"
import { API_URL } from "@/config/api"

export function useDeleteMealIngredient() {
  const { accessToken } = useAuth()
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async ({
      mealIngredientId,
      date,
    }: {
      mealIngredientId: number
      date: string
    }) => {
      if (!accessToken) throw new Error("No access token")

      const res = await fetch(
        `${API_URL}/meal-ingredients/${mealIngredientId}`,
        {
          method: "DELETE",
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      )

      if (!res.ok) throw new Error("Failed to delete ingredient")
      return mealIngredientId
    },
    onSuccess: (_data, variables) => {
      queryClient.invalidateQueries({ queryKey: ["dailyLog", variables.date] })
    },
  })
}
