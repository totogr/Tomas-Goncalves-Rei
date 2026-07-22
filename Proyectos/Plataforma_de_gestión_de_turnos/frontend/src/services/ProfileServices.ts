import { useMutation } from "@tanstack/react-query";
import { BASE_API_URL } from "@/config/app-query-client";
import { ProfessionalProfileForm } from "@/models/ProfessionalProfile";
import { useAccessTokenGetter, useHandleResponse, useToken } from "@/services/TokenContext";

export function useSaveProfile() {
    const getAccessToken = useAccessTokenGetter();
    const handleResponse = useHandleResponse();
    const [tokenState] = useToken();

    return useMutation({
        mutationFn: async (data: ProfessionalProfileForm) => {
            if (tokenState.state !== "LOGGED_IN") throw new Error("No autenticado");

            const token = await getAccessToken();
            const id = tokenState.tokens.id;

            const response = await fetch(`${BASE_API_URL}/professionals/${id}/profile`, {
                method: "PATCH",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(data),
            });

            // 204 No Content — no hay body que parsear
            if (response.status === 204) return;
            return handleResponse(response, (json) => json);
        },
    });
}