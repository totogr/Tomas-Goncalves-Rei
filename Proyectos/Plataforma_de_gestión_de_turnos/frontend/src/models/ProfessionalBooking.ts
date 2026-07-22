export type ProfessionalBooking = {
  id: number;
  status: "CONFIRMED" | "CANCELLED" | "COMPLETED";
  cancelled_by: string | null;
  service_name: string;
  client_name: string;
  client_email: string;
  date: string;        // "YYYY-MM-DD"
  time: string;        // "HH:mm"
  end: string;         // "HH:mm"
  duration_minutes: number;
  marked_absent_at?: string | null;
};