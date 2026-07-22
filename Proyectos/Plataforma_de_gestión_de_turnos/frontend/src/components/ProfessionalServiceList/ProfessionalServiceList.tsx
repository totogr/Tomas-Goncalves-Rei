import { useLocation } from "wouter";

import { BookingCalendar } from "@/components/BookingCalendar/BookingCalendar";
import { BookingSuccess } from "@/components/BookingSuccess/BookingSuccess";
import { Professional } from "@/models/Professional";
import { Service } from "@/models/Service";
import { useCreateAppointment } from "@/services/AppointmentServices";
import { useConfirmPromotion } from "@/services/WaitListService";
import { formatHumanDate } from "@/components/BookingCalendar/calendarUtils";

import styles from "./ProfessionalServiceList.module.css";
import { useState } from "react";

type Props = {
  professional: Professional;
};

export const ProfessionalServiceList = ({ professional }: Props) => {
  const [, navigate] = useLocation();

  const searchParams = new URLSearchParams(window.location.search);
  const waitlistDate = searchParams.get("date");
  const waitlistSlot = searchParams.get("slotTime");
  const waitlistServiceId = searchParams.get("serviceId");
  const isWaitlistConfirm = searchParams.get("waitlistConfirm") === "true";

  const preselectedServiceId =
      (window.history.state as { preselectedServiceId?: string } | undefined)
          ?.preselectedServiceId ?? null;

  const activeServices = professional.services.filter((s) => s.active);

  const [selectedService, setSelectedService] = useState<Service | null>(() => {
    const idToFind = preselectedServiceId ?? waitlistServiceId;
    if (!idToFind) return null;
    return activeServices.find((s) => String(s.id) === idToFind) ?? null;
  });

  const [selectedDate, setSelectedDate] = useState<string | null>(waitlistDate ?? null);
  const [selectedSlot, setSelectedSlot] = useState<string | null>(waitlistSlot ?? null);

  const createAppointment = useCreateAppointment();
  const confirmPromotion = useConfirmPromotion();

  const handleSelectService = (service: Service) => {
    setSelectedService(service);
    setSelectedDate(null);
    setSelectedSlot(null);
    createAppointment.reset();
    confirmPromotion.reset();
  };

  const handleSelectDate = (date: string) => {
    setSelectedDate(date);
    setSelectedSlot(null);
  };

  const handleConfirm = () => {
    if (!selectedDate || !selectedSlot || !selectedService?.id) return;

    if (isWaitlistConfirm) {
      const slotStart = `${selectedDate}T${selectedSlot}:00-03:00`;
      confirmPromotion.mutate({
        professionalId: professional.id,
        serviceId: Number(selectedService.id),
        slotStart,
      });
    } else {
      createAppointment.mutate({
        professionalId: professional.id,
        serviceId: selectedService.id,
        date: selectedDate,
        time: selectedSlot,
      });
    }
  };

  const isSuccess = createAppointment.isSuccess || confirmPromotion.isSuccess;
  const isPending = createAppointment.isPending || confirmPromotion.isPending;
  const isError = createAppointment.isError || confirmPromotion.isError;

  if (isSuccess && selectedDate && selectedSlot && selectedService) {
    return (
        <div className={styles.page}>
          <BookingSuccess
              professional={professional}
              service={selectedService}
              date={selectedDate}
              time={selectedSlot}
              onViewMore={() => {
                createAppointment.reset();
                confirmPromotion.reset();
                setSelectedService(null);
                setSelectedDate(null);
                setSelectedSlot(null);
              }}
          />
        </div>
    );
  }

  return (
      <div className={styles.page}>
        <button type="button" className={styles.backLink} onClick={() => navigate("/professionals")}>
          ← Volver al listado
        </button>

        <ProfessionalHeader professional={professional} />

        <div className={styles.bookingLayout}>
          <div className={styles.bookingMain}>
            <section className={styles.section}>
              <h2 className={styles.sectionTitle}>1. Elegí el servicio</h2>
              {activeServices.length === 0 ? (
                  <EmptyServices />
              ) : (
                  <div className={styles.list}>
                    {activeServices.map((service) => {
                      if (!service.id) return null;
                      return (
                          <ServiceCard
                              key={service.id}
                              service={service}
                              selected={selectedService?.id === service.id}
                              onSelect={() => handleSelectService(service)}
                          />
                      );
                    })}
                  </div>
              )}
            </section>

            {selectedService && (
                <BookingCalendar
                    key={selectedService.id}
                    professionalId={professional.id}
                    service={selectedService}
                    selectedDate={selectedDate}
                    selectedSlot={selectedSlot}
                    onSelectDate={handleSelectDate}
                    onSelectSlot={setSelectedSlot}
                />
            )}
          </div>

          <aside className={styles.sidebar}>
            <BookingSummary
                professional={professional}
                service={selectedService}
                selectedDate={selectedDate}
                selectedSlot={selectedSlot}
                isSubmitting={isPending}
                errorMessage={isError ? "No se pudo confirmar la reserva." : null}
                onConfirm={handleConfirm}
                isWaitlistConfirm={isWaitlistConfirm}
            />
          </aside>
        </div>
      </div>
  );
};

/* ── Professional header ── */

const ProfessionalHeader = ({ professional }: { professional: Professional }) => {
  const initials = getInitials(professional.firstName, professional.lastName);
  const locationLabel = formatLocation(professional);

  return (
      <div className={styles.headerCard}>
        <div className={styles.avatar}>{initials}</div>
        <div className={styles.headerInfo}>
          <h1 className={styles.headerName}>
            {professional.firstName} {professional.lastName}
          </h1>
          {(professional.specialty || locationLabel) && (
              <p className={styles.headerMeta}>
                {[professional.specialty, locationLabel].filter(Boolean).join(" · ")}
              </p>
          )}
          {professional.rating !== null && professional.rating !== undefined && (
              <div className={styles.ratingRow}>
                <RatingStars rating={professional.rating} />
                <span className={styles.ratingText}>
                            {professional.rating.toFixed(1)}
                  {professional.reviewCount !== null && professional.reviewCount !== undefined && (
                      <span className={styles.reviewCount}> ({professional.reviewCount} reseñas)</span>
                  )}
                        </span>
              </div>
          )}
        </div>
      </div>
  );
};

const RatingStars = ({ rating }: { rating: number }) => {
  const full = Math.floor(rating);
  const half = rating - full >= 0.5;
  return (
      <div className={styles.stars} aria-label={`${rating} de 5 estrellas`}>
        {[1, 2, 3, 4, 5].map((i) => {
          const isFull = i <= full;
          const isHalf = !isFull && half && i === full + 1;
          return (
              <span key={i} className={isFull ? styles.starFull : isHalf ? styles.starHalf : styles.starEmpty}>
                        ★
                    </span>
          );
        })}
      </div>
  );
};

/* ── Service card ── */

const ServiceCard = ({
                       service,
                       selected,
                       onSelect,
                     }: {
  service: Service;
  selected: boolean;
  onSelect: () => void;
}) => (
    <button
        type="button"
        className={`${styles.serviceCard} ${selected ? styles.serviceCardSelected : ""}`}
        onClick={onSelect}
    >
      <div className={styles.serviceLeft}>
        <span className={styles.serviceName}>{service.name}</span>
        <span className={styles.serviceDuration}>{service.duration_minutes} min</span>
      </div>
      <div className={styles.serviceRight}>
        <span className={styles.servicePrice}>{formatPrice(service.price)}</span>
        <span className={`${styles.serviceSelector} ${selected ? styles.serviceSelectorSelected : ""}`}>
                {selected ? "✓" : ""}
            </span>
      </div>
    </button>
);

/* ── Booking summary sidebar ── */

type SummaryProps = {
  professional: Professional;
  service: Service | null;
  selectedDate: string | null;
  selectedSlot: string | null;
  isSubmitting: boolean;
  errorMessage: string | null;
  onConfirm: () => void;
  isWaitlistConfirm: boolean;
};

const BookingSummary = ({
                          professional,
                          service,
                          selectedDate,
                          selectedSlot,
                          isSubmitting,
                          errorMessage,
                          onConfirm,
                          isWaitlistConfirm,
                        }: SummaryProps) => {
  const canConfirm = Boolean(service && selectedDate && selectedSlot) && !isSubmitting;

  return (
      <div className={styles.summary}>
        {isWaitlistConfirm && (
            <div style={{
              background: "#fef3c7",
              border: "1px solid #f59e0b",
              borderRadius: "8px",
              padding: "12px 16px",
              marginBottom: "16px",
              fontSize: "14px",
              color: "#92400e",
              lineHeight: "1.5",
            }}>
              ⏳ <strong>Lugar disponible en lista de espera.</strong> El turno ya está pre-seleccionado — confirmalo antes de que expire la oferta.
            </div>
        )}

        <div className={styles.summaryHeader}>
                <span className={styles.summaryTitle}>
                    {professional.firstName} {professional.lastName}
                </span>
          <span className={styles.summarySubtitle}>
                    {service ? `${service.name} · ${service.duration_minutes} min` : "Seleccioná un servicio"}
                </span>
        </div>

        {service && (
            <div className={styles.summaryDetails}>
              <SummaryRow label="Fecha" value={selectedDate ? formatHumanDate(selectedDate) : "—"} />
              <SummaryRow label="Hora" value={selectedSlot ? `${selectedSlot} hs` : "—"} />
              <SummaryRow label="Duración" value={`${service.duration_minutes} min`} />
            </div>
        )}

        <div className={styles.summaryPriceRow}>
          <span className={styles.summaryPriceLabel}>Total del servicio</span>
          <span className={styles.summaryTotal}>
                    {service ? formatPrice(service.price) : "—"}
                </span>
        </div>

        <button
            type="button"
            className={styles.confirmButton}
            disabled={!canConfirm}
            onClick={onConfirm}
        >
          {isSubmitting ? "Procesando..." : "Confirmar reserva →"}
        </button>

        {!service && (
            <p className={styles.helpText}>Seleccioná un servicio para comenzar.</p>
        )}
        {service && !selectedDate && (
            <p className={styles.helpText}>Seleccioná una fecha para continuar.</p>
        )}
        {service && selectedDate && !selectedSlot && (
            <p className={styles.helpText}>Seleccioná un horario para continuar.</p>
        )}

        {errorMessage && <span className={styles.errorText}>{errorMessage}</span>}
      </div>
  );
};

const SummaryRow = ({ label, value }: { label: string; value: string }) => (
    <div className={styles.summaryDetailRow}>
      <span className={styles.summaryDetailLabel}>{label}</span>
      <span className={styles.summaryDetailValue}>{value}</span>
    </div>
);

/* ── Empty state ── */

const EmptyServices = () => (
    <div className={styles.empty}>
      <div className={styles.emptyIcon}>
        <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
          <rect x="3" y="4" width="18" height="18" rx="2" />
          <path d="M16 2v4M8 2v4M3 10h18" />
        </svg>
      </div>
      <span className={styles.emptyTitle}>Sin servicios disponibles</span>
      <p className={styles.emptyText}>
        Este profesional todavía no cargó servicios para reservar. Volvé a intentarlo más tarde.
      </p>
    </div>
);

/* ── Helpers ── */

const getInitials = (firstName: string, lastName: string) =>
    `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();

const formatLocation = (professional: Professional): string | null => {
  const loc = professional.location;
  if (!loc) return null;
  return [loc.neighborhood, loc.city].filter(Boolean).join(", ") || null;
};

const formatPrice = (price: number): string =>
    new Intl.NumberFormat("es-AR", {
      style: "currency",
      currency: "ARS",
      maximumFractionDigits: 0,
    }).format(price);