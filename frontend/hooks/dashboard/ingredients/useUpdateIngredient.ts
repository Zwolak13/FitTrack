"use client";

import { API_URL, API_ENDPOINTS } from "@/config/api";
import { useAuth } from "@/context/AuthContext";

export function useUpdateIngredient() {
  const { accessToken } = useAuth();

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  return async (id: number, ingredient: any) => {
    if (!accessToken) throw new Error("No access token");

    const res = await fetch(`${API_URL}${API_ENDPOINTS.ingredients}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json", "Authorization": `Bearer ${accessToken}` },
      body: JSON.stringify(ingredient),
    });

    if (!res.ok) throw new Error("Failed to update ingredient");
    return res.json();
  };
}
