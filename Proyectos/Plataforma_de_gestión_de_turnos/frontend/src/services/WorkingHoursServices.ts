import { useQuery, useMutation } from "@tanstack/react-query";
import { BASE_API_URL } from "@/config/app-query-client";
import { WorkingHours, WorkingHoursSchema } from "@/models/WorkingHours";
import { useAccessTokenGetter, useHandleResponse } from "@/services/TokenContext";

export function useWorkingHours() {
    const getAccessToken = useAccessTokenGetter();
    const handleResponse = useHandleResponse();

    return useQuery({
        queryKey: ["pro", "working-hours"],
        queryFn: async () => {
            const token = await getAccessToken();
            const response = await fetch(`${BASE_API_URL}/schedule`, {
                headers: {
                    Accept: "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });
            return handleResponse(response, (json) => WorkingHoursSchema.parse(json));
        },
    });
}

export function useSaveWorkingHours() {
    const getAccessToken = useAccessTokenGetter();
    const handleResponse = useHandleResponse();

    return useMutation({
        mutationFn: async (workingHours: WorkingHours) => {
            const token = await getAccessToken();
            const response = await fetch(`${BASE_API_URL}/schedule`, {
                method: "PUT",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(workingHours),
            });
            return handleResponse(response, (json) => WorkingHoursSchema.parse(json));
        },
    });
}