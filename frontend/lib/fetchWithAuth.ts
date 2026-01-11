import { useAuth } from "@/context/AuthContext";

let isRefreshing = false;
let queue: (() => void)[] = [];

export async function fetchWithAuth(
  input: RequestInfo,
  init: RequestInit = {},
  getToken: () => string | null,
  setToken: (t: string | null) => void
) {
  const res = await fetch(input, {
    ...init,
    headers: {
      ...init.headers,
      Authorization: `Bearer ${getToken()}`,
    },
  });

  if (res.status !== 401) return res;

  if (isRefreshing) {
    return new Promise((resolve) => {
      queue.push(() => resolve(fetchWithAuth(input, init, getToken, setToken)));
    });
  }

  isRefreshing = true;

  try {
    const refresh = await fetch("/api/auth/refresh", {
      method: "POST",
      credentials: "include",
    });

    if (!refresh.ok) throw new Error();

    const data = await refresh.json();
    setToken(data.accessToken);

    queue.forEach((cb) => cb());
    queue = [];

    return fetchWithAuth(input, init, getToken, setToken);
  } catch {
    setToken(null);
    throw new Error("Unauthorized");
  } finally {
    isRefreshing = false;
  }
}
