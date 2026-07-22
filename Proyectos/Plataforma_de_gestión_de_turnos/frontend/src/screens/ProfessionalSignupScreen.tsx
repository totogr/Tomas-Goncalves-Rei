import { ProfessionalSignup } from "@/components/SignUp/ProfessionalSignup";
import { useProfessionalSignup } from "@/services/UserServices";
import { ProfessionalSignupRequest } from "@/models/Login";

export const ProfessionalSignupScreen = () => {
    const { mutateAsync, error } = useProfessionalSignup();

    let displayError = error;
    if (error && error.message.includes("409")) {
        displayError = new Error("Este email ya está registrado. Por favor, iniciá sesión.");
    }

    const handleFormSubmit = async (datosLimpios: ProfessionalSignupRequest) => {
        try {
            await mutateAsync(datosLimpios);
            window.location.href = "/professional/onboarding/perfil";

        } catch (err) {
            console.error("Error al registrar profesional:", err);
        }
    };

    return <ProfessionalSignup onSubmit={handleFormSubmit} submitError={displayError} />;
};