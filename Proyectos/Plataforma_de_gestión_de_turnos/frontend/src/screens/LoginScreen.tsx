import { Login } from "@/components/Login/Login";
import { useLogin } from "@/services/UserServices";
import { useLocation } from "wouter";

export const LoginScreen = () => {
  const { mutate, error } = useLogin();
  const [, navigate] = useLocation();

  return (
      <Login
          onSubmit={(value) =>
              mutate(value, {
                onSuccess: (tokens) => {
                  if (tokens.role === "PROFESSIONAL") {
                    navigate("/professional/agenda");
                  } else {
                    navigate("/professionals");
                  }
                },
              })
          }
          submitError={error}
      />
  );
};