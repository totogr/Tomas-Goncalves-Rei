import { useLocation } from "wouter";
import { BookingCard } from "@/components/BookingCard/BookingCard";
import { WaitListCard } from "@/components/WaitList/WaitListCardClient";
import { ClientBooking } from "@/models/Booking";
import { WaitListEntry } from "@/models/WaitList";
import { TabStatus } from "@/services/BookingServices";
import styles from "./ClientBookingsList.module.css";

// Extendemos el tipo de la Tab para que soporte "waitlist" de forma local en el Front
export type ClientTabStatus = TabStatus | "waitlist";

type Props = {
    bookings: ClientBooking[];
    waitlistEntries?: WaitListEntry[];
    isLoading: boolean;
    activeTab: ClientTabStatus;
    onTabChange: (tab: ClientTabStatus) => void;
    onLeaveWaitlist: (entry: WaitListEntry) => void;
};

const tabs: { key: ClientTabStatus; label: string }[] = [
    { key: "upcoming", label: "Próximos" },
    { key: "waitlist", label: "En espera" },
    { key: "past", label: "Anteriores" },
    { key: "cancelled", label: "Cancelados" },
];

export const ClientBookingsList = ({
                                       bookings,
                                       waitlistEntries = [],
                                       isLoading,
                                       activeTab,
                                       onTabChange,
                                       onLeaveWaitlist
                                   }: Props) => {
    const [, navigate] = useLocation();

    const isCurrentTabEmpty = activeTab === "waitlist"
        ? waitlistEntries.length === 0
        : bookings.length === 0;

    return (
        <div className={styles.page}>
            <div className={styles.header}>
                <h1 className={styles.title}>Mis turnos</h1>
                <p className={styles.subtitle}>Gestioná tus reservas y revisá tu historial.</p>
            </div>

            <div className={styles.tabs}>
                {tabs.map((tab) => (
                    <button
                        key={tab.key}
                        type="button"
                        className={`${styles.tab} ${activeTab === tab.key ? styles.tabActive : ""}`}
                        onClick={() => onTabChange(tab.key)}
                    >
                        {tab.label}
                    </button>
                ))}
            </div>

            {isLoading && (
                <div className={styles.empty}>
                    <p className={styles.emptyText}>Cargando turnos...</p>
                </div>
            )}

            {!isLoading && isCurrentTabEmpty && (
                <div className={styles.empty}>
                    <p className={styles.emptyText}>
                        {activeTab === "waitlist" ? (
                            "No estás anotado en ninguna lista de espera activa."
                        ) : (
                            <>No tenés turnos {activeTab === "upcoming" ? "próximos" : activeTab === "past" ? "anteriores" : "cancelados"}.</>
                        )}
                    </p>
                    {(activeTab === "upcoming" || activeTab === "waitlist") && (
                        <button type="button" className={styles.primaryButton} onClick={() => navigate("/professionals")}>
                            Buscar un profesional
                        </button>
                    )}
                </div>
            )}

            {!isLoading && !isCurrentTabEmpty && (
                <div className={styles.list}>
                    {activeTab === "waitlist" ? (
                        waitlistEntries.map((entry) => (
                            <WaitListCard
                                key={`${entry.professionalId}-${entry.serviceId}-${entry.slotStart}`}
                                entry={entry}
                                onLeave={onLeaveWaitlist}
                            />
                        ))
                    ) : (
                        bookings.map((booking) => (
                            <BookingCard
                                key={booking.id}
                                booking={booking}
                                onClick={() => navigate(`/bookings/${booking.id}`)}
                            />
                        ))
                    )}
                </div>
            )}
        </div>
    );
};