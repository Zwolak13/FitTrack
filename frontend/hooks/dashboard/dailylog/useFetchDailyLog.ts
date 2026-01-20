"use client";

import { API_URL, API_ENDPOINTS } from "@/config/api";
import { useAuth } from "@/context/AuthContext";

export function useFetchDailyLog() {
  const { accessToken } = useAuth();

  return async (date: string) => {
    if (!accessToken) throw new Error("No access token");

    const res = await fetch(`${API_URL}${API_ENDPOINTS.dailyLog}/${date}`, {
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${accessToken}`,
      },
    });

    if (!res.ok) throw new Error("Failed to fetch daily log");
    return res.json();
  };
}
