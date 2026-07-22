import { ClientSignup } from "@/components/SignUp/ClientSignup";
import { useClientSignup } from "@/services/UserServices";
import { ClientSignupRequest } from "@/models/Login";

export const ClientSignupScreen = () => {
    const { mutateAsync, error } = useClientSignup();

    let displayError = error;
    if (error && error.message.includes("409")) {
        displayError = new Error("Este email ya está registrado. Por favor, iniciá sesión.");
    }

    const handleFormSubmit = async (datosLimpios: ClientSignupRequest) => {
        try {
            await mutateAsync(datosLimpios);
            window.location.href = "/professionals/";
        } catch (err) {
            console.error("Error al registrar cliente:", err);
        }
    };

    return (
        <ClientSignup
            onSubmit={handleFormSubmit}
            submitError={displayError}
        />
    );
};