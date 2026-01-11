"use client";

import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import { API_URL, API_ENDPOINTS } from "@/config/api";

export function useLogout() {
  const { setAccessToken } = useAuth();
  const router = useRouter();

  return async () => {
    await fetch(`${API_URL}${API_ENDPOINTS.logout}`, {
      method: "POST",
      credentials: "include",
    });

    setAccessToken(null);
    router.replace("/login");
  };
}
