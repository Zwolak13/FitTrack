"use client";

import { API_URL, API_ENDPOINTS } from "@/config/api";
import { useAuth } from "@/context/AuthContext";

export function useDeleteIngredient() {
  const { accessToken } = useAuth();

  return async (id: number) => {
    if (!accessToken) throw new Error("No access token");

    const res = await fetch(`${API_URL}${API_ENDPOINTS.ingredients}/${id}`, {
      method: "DELETE",
      headers: { "Authorization": `Bearer ${accessToken}` },
    });

    if (!res.ok) throw new Error("Failed to delete ingredient");
  };
}
