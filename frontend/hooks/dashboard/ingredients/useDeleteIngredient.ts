"use client"

import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useAuth } from "@/context/AuthContext"
import { API_URL, API_ENDPOINTS } from "@/config/api"

export function useDeleteIngredient(type: "all" | "mine") {
  const { accessToken } = useAuth()
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (ingredientId: number) => {
      if (!accessToken) throw new Error("No access token")

      const res = await fetch(`${API_URL}${API_ENDPOINTS.ingredients}/${ingredientId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${accessToken}` },
      })

      if (!res.ok) throw new Error("Nie udało się usunąć składnika")
      return ingredientId
    },
    onSuccess: () => {
      const key = type === "mine" ? [API_ENDPOINTS.ingredients, "mine"] : [API_ENDPOINTS.ingredients]
      queryClient.invalidateQueries({ queryKey: key })
    },
  })
}
