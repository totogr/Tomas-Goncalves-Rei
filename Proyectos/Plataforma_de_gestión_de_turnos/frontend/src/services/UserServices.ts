import { useMutation } from "@tanstack/react-query";

import { BASE_API_URL } from "@/config/app-query-client";
import { AuthResponseSchema, LoginRequest,  ClientSignupRequest, ProfessionalSignupRequest, } from "@/models/Login";
import { useToken } from "@/services/TokenContext";

export function useLogin() {
  const [, setToken] = useToken();

  return useMutation({
    mutationFn: async (req: LoginRequest) => {
      const tokens = await auth("POST", "/sessions", req);
      setToken({ state: "LOGGED_IN", tokens });

      return tokens;
    },
  });
}

export function useRefresh() {
  const [tokenState, setToken] = useToken();

  return useMutation({
    mutationFn: async () => {
      if (tokenState.state !== "LOGGED_IN") {
        return;
      }

      try {
        const refreshToken = tokenState.tokens.refreshToken;
        const tokenPromise = auth("PUT", "/sessions", { refreshToken });
        setToken({ state: "REFRESHING", tokenPromise });
        setToken({ state: "LOGGED_IN", tokens: await tokenPromise });
      } catch (err) {
        setToken({ state: "LOGGED_OUT" });
        throw err;
      }
    },
  });
}

export function useClientSignup() {
  const [, setToken] = useToken();

  return useMutation({
    mutationFn: async (req: ClientSignupRequest) => {
      const tokens = await auth("POST", "/clients/signup", req);
      setToken({ state: "LOGGED_IN", tokens });
      return tokens;
    },
  });
}

export function useProfessionalSignup() {
  const [, setToken] = useToken();

  return useMutation({
    mutationFn: async (req: ProfessionalSignupRequest) => {
      const tokens = await auth("POST", "/professionals/signup", req);
      setToken({ state: "LOGGED_IN", tokens });
      return tokens;
    },
  });
}

async function auth(method: "PUT" | "POST", endpoint: string, data: object) {
  const response = await fetch(BASE_API_URL + endpoint, {
    method,
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (response.ok) {
    return AuthResponseSchema.parse(await response.json());
  } else {
    if (response.status === 401) {
        throw new Error("El email o la contraseña son incorrectos.");
    }
    throw new Error(`Failed with status ${response.status}: ${await response.text()}`);
  }
}

export function useForgotPassword() {
  return useMutation({
    mutationFn: async (req: { email: string }) => {
      const response = await fetch(BASE_API_URL + "/sessions/forgot-password", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(req),
      });

      if (!response.ok) {
        throw new Error(`Failed with status ${response.status}`);
      }
    },
  });
}

export function useResetPassword() {
  return useMutation({
    mutationFn: async (req: { token: string; newPassword: string }) => {
      const response = await fetch(BASE_API_URL + "/sessions/reset-password", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(req),
      });

      if (!response.ok) {
        throw new Error(`Failed with status ${response.status}: ${await response.text()}`);
      }
    },
  });
}