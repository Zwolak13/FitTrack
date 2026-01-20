"use client";

import { API_URL, API_ENDPOINTS } from "@/config/api";
import { useAuth } from "@/context/AuthContext";

export function useAddIngredientToMeal() {
  const { accessToken } = useAuth();

  return async (mealId: number, ingredientId: number, quantity: number) => {
    if (!accessToken) throw new Error("No access token");

    const res = await fetch(
      `${API_URL}${API_ENDPOINTS.mealIngredients}/add?mealId=${mealId}&ingredientId=${ingredientId}&quantity=${quantity}`,
      { method: "POST", headers: { "Authorization": `Bearer ${accessToken}` } }
    );

    if (!res.ok) throw new Error("Failed to add ingredient to meal");
    return res.json();
  };
}
