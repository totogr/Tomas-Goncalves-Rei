import { useFormContext } from "@/config/form-context";
import styles from "./SubmitButton.module.css";

type Props = {
  label?: string;
};

export const SubmitButton = ({ label = "Crear cuenta" }: Props) => {
  const form = useFormContext();

  return (
    <form.Subscribe
      selector={(state) => [state.canSubmit, state.isSubmitting]}
      children={([canSubmit, isSubmitting]) => (
        <button type="submit" className={styles.button} disabled={!canSubmit}>
          {isSubmitting ? "..." : label}
        </button>
      )}
    />
  );
};