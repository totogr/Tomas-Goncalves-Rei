import { ProfessionalLayout } from "@/components/ProfessionalLayout/ProfessionalLayout";
import { ProfessionalAgenda } from "@/components/ProfessionalAgenda/ProfessionalAgenda";
import { useProfessionalBookings } from "@/services/ProfessionalBookingService";

export const ProfessionalAgendaScreen = () => {
  const { data: bookings = [], isLoading } = useProfessionalBookings("all");

  return (
    <ProfessionalLayout>
      <ProfessionalAgenda bookings={bookings} isLoading={isLoading} />
    </ProfessionalLayout>
  );
};