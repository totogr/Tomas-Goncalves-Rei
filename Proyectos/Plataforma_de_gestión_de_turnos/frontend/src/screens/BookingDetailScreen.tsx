import { useRoute } from "wouter";
import { navigate } from "wouter/use-browser-location";
import { ClientLayout } from "@/components/ClientLayout/ClientLayout";
import { BookingDetail } from "@/components/BookingDetail/BookingDetail";
import { useBookingDetail, useCancelBooking, useRescheduleBooking } from "@/services/BookingServices";

export const BookingDetailScreen = () => {
  const [, params] = useRoute("/bookings/:id");
  const id = params?.id ?? "";

  const { data: booking, isLoading, isError } = useBookingDetail(id);
  const { mutate: cancelBooking, isPending: isCancelling } = useCancelBooking();
  const { mutate: rescheduleBooking, isPending: isRescheduling } = useRescheduleBooking();

  const handleReschedule = (bookingId: string) => {
    if (!booking) return;
    rescheduleBooking(bookingId, {
      onSuccess: () => {
        navigate(`/professionals/${booking.professional.id}`, {
          state: { preselectedServiceId: booking.service.id },
        });
      },
    });
  };

  return (
    <ClientLayout>
      <BookingDetail
        booking={booking}
        isLoading={isLoading}
        isError={isError}
        isCancelling={isCancelling}
        isRescheduling={isRescheduling}
        onCancel={(id) =>
          cancelBooking(id, {
            onSuccess: () => navigate("/bookings/me"),
          })
        }
        onReschedule={handleReschedule}
      />
    </ClientLayout>
  );
};