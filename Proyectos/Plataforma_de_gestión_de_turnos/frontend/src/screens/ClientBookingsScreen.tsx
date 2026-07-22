import { useState } from "react";
import { ClientLayout } from "@/components/ClientLayout/ClientLayout";
import { ClientBookingsList, ClientTabStatus } from "@/components/ClientBookingsList/ClientBookingsList";
import { LeaveWaitListModal } from "@/components/LeaveWaitlistModal/LeaveWaitListModal.tsx";
import { useClientBookings, TabStatus } from "@/services/BookingServices";
import { useMyWaitListEntries, useLeaveWaitList } from "@/services/WaitListService";
import { WaitListEntry } from "@/models/WaitList";

export const ClientBookingsScreen = () => {
    const [activeTab, setActiveTab] = useState<ClientTabStatus>("upcoming");
    const [pendingLeave, setPendingLeave] = useState<WaitListEntry | null>(null);

    const isWaitlistTab = activeTab === "waitlist";

    const { data: bookings = [], isLoading: isLoadingBookings } = useClientBookings(
        isWaitlistTab ? "upcoming" : (activeTab as TabStatus)
    );

    const {
        data: waitlistEntries = [],
        isLoading: isLoadingWaitlist,
        fetchStatus
    } = useMyWaitListEntries(isWaitlistTab);

    const leaveWaitListMutation = useLeaveWaitList();

    const isLoading = isWaitlistTab
        ? (isLoadingWaitlist && fetchStatus !== "idle")
        : isLoadingBookings;

    const handleLeaveWaitlist = (entry: WaitListEntry) => {
        setPendingLeave(entry);
    };

    const confirmLeave = () => {
        if (!pendingLeave) return;
        leaveWaitListMutation.mutate(
            {
                professionalId: Number(pendingLeave.professionalId),
                serviceId: Number(pendingLeave.serviceId),
                slotStart: pendingLeave.slotStart,
            },
            {
                onSuccess: () => setPendingLeave(null),
            }
        );
    };

    return (
        <ClientLayout>
            <ClientBookingsList
                bookings={isWaitlistTab ? [] : bookings}
                waitlistEntries={waitlistEntries}
                isLoading={isLoading}
                activeTab={activeTab}
                onTabChange={setActiveTab}
                onLeaveWaitlist={handleLeaveWaitlist}
            />

            <LeaveWaitListModal
                isOpen={pendingLeave !== null}
                isLeaving={leaveWaitListMutation.isPending}
                serviceName={pendingLeave?.serviceName ?? ""}
                professionalName={pendingLeave?.professionalName ?? ""}
                onConfirm={confirmLeave}
                onClose={() => setPendingLeave(null)}
            />
        </ClientLayout>
    );
};
