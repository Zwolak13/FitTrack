"use client";

import { API_URL, API_ENDPOINTS } from "@/config/api";
import { useAuth } from "@/context/AuthContext";

export function useCreateIngredient() {
  const { accessToken } = useAuth();

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  return async (ingredient: any) => {
    if (!accessToken) throw new Error("No access token");

    const res = await fetch(`${API_URL}${API_ENDPOINTS.ingredients}`, {
      method: "POST",
      headers: { "Content-Type": "application/json", "Authorization": `Bearer ${accessToken}` },
      body: JSON.stringify(ingredient),
    });

    if (!res.ok) throw new Error("Failed to create ingredient");
    return res.json();
  };
}
