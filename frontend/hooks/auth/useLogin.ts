"use client";

import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import { API_URL, API_ENDPOINTS } from "@/config/api";

export function useLogin() {
  const { setAccessToken } = useAuth();
  const router = useRouter();

  return async (email: string, password: string) => {
    const res = await fetch(`${API_URL}${API_ENDPOINTS.login}`, {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (!res.ok) throw new Error("Login failed");

    const data = await res.json();
    setAccessToken(data.accessToken);
    router.replace("/dashboard");
  };
}
