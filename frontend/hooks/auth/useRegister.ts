"use client";

import { useRouter } from "next/navigation";
import { API_URL, API_ENDPOINTS } from "@/config/api";

export function useRegister() {
  const router = useRouter();

  return async (username: string, email: string, password: string) => {
    const res = await fetch(`${API_URL}${API_ENDPOINTS.register}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, email, password }),
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || "Register failed");
    }

    router.replace("/login");
  };
}
