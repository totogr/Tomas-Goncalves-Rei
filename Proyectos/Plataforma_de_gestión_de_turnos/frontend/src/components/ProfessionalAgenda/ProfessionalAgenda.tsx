import { useState, useMemo } from "react";
import { ProfessionalBooking } from "@/models/ProfessionalBooking";
import styles from "./ProfessionalAgenda.module.css";
import { useCancelProfessionalBooking, useMarkAbsent } from "@/services/ProfessionalBookingService";
import { CancelBookingModal } from "@/components/CancelBookingModal/CancelBookingModal";
import { useActivePromotions } from "@/services/ProfessionalWaitListService";

// ── constantes ────────────────────────────────────────────────────────────────

const DAYS = ["LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM"];

const MORNING_START   = 5;
const AFTERNOON_START = 13;
const AFTERNOON_END   = 24;

const HOURS_MORNING   = Array.from({ length: AFTERNOON_START - MORNING_START },   (_, i) => i + MORNING_START);
const HOURS_AFTERNOON = Array.from({ length: AFTERNOON_END   - AFTERNOON_START }, (_, i) => i + AFTERNOON_START);

const CELL_HEIGHT  = 64;
const PERIOD_H     = 33;
const EVENT_INSET  = 4;
const HEADER_H     = 83.5;

// ── helpers ───────────────────────────────────────────────────────────────────

function getWeekStart(base: Date): Date {
  const d = new Date(base);
  const day = d.getDay();
  const diff = day === 0 ? -6 : 1 - day;
  d.setDate(d.getDate() + diff);
  d.setHours(0, 0, 0, 0);
  return d;
}

function addDays(d: Date, n: number): Date {
  const r = new Date(d);
  r.setDate(r.getDate() + n);
  return r;
}

function isoDate(d: Date): string {
  const yyyy = d.getFullYear();
  const mm   = String(d.getMonth() + 1).padStart(2, "0");
  const dd   = String(d.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
}

function formatMonth(d: Date): string {
  return d.toLocaleDateString("es-AR", { month: "long", year: "numeric" });
}

function formatWeekRange(start: Date): string {
  const end  = addDays(start, 6);
  const opts: Intl.DateTimeFormatOptions = { day: "numeric", month: "long", year: "numeric" };
  return `Semana del ${start.toLocaleDateString("es-AR", opts)} al ${end.toLocaleDateString("es-AR", opts)}`;
}

function topPx(time: string): number {
  const [h, m] = time.split(":").map(Number);
  const minutesFromStart = (h - MORNING_START) * 60 + m;
  const raw = (minutesFromStart / 60) * CELL_HEIGHT;
  const tardeOffset = h >= AFTERNOON_START ? PERIOD_H : 0;
  return HEADER_H + PERIOD_H + raw + tardeOffset + EVENT_INSET;
}

function heightPx(startTime: string, duration: number): number {
  const [h, m] = startTime.split(":").map(Number);
  const endMinutes = h * 60 + m + duration;
  const endHour = endMinutes / 60;
  const crossesAfternoon = h < AFTERNOON_START && endHour > AFTERNOON_START;
  const raw = (duration / 60) * CELL_HEIGHT;
  const extra = crossesAfternoon ? PERIOD_H : 0;
  if (duration <= 15) return Math.max(raw + extra - EVENT_INSET, 4);
  return Math.max(raw + extra - EVENT_INSET * 2, 30);
}

// ── layout ────────────────────────────────────────────────────────────────────

type BookingSlot = {
  time: string;
  end: string;
  duration_minutes: number;
  bookings: ProfessionalBooking[];
  col: number;
  totalCols: number;
};

function groupAndLayout(events: ProfessionalBooking[]): BookingSlot[] {
  if (events.length === 0) return [];

  const slotMap = new Map<string, ProfessionalBooking[]>();
  for (const event of events) {
    const key = `${event.time}|${event.end}`;
    const arr = slotMap.get(key) ?? [];
    arr.push(event);
    slotMap.set(key, arr);
  }

  const slots: Omit<BookingSlot, "col" | "totalCols">[] = Array.from(slotMap.values())
      .map(bookings => ({
        time: bookings[0].time,
        end: bookings[0].end,
        duration_minutes: bookings[0].duration_minutes,
        bookings,
      }))
      .sort((a, b) => a.time.localeCompare(b.time));

  const clusters: (typeof slots)[] = [];
  for (const slot of slots) {
    const existing = clusters.find(cluster =>
        cluster.some(s => s.time < slot.end && slot.time < s.end)
    );
    if (existing) existing.push(slot);
    else clusters.push([slot]);
  }

  return clusters.flatMap(cluster =>
      cluster.map((slot, i) => ({ ...slot, col: i, totalCols: cluster.length }))
  );
}

// ── tipos ─────────────────────────────────────────────────────────────────────

type Props = {
  bookings: ProfessionalBooking[];
  isLoading: boolean;
};

// ── componente principal ──────────────────────────────────────────────────────

export function ProfessionalAgenda({ bookings, isLoading }: Props) {
  const [weekStart, setWeekStart]             = useState(() => getWeekStart(new Date()));
  const [selected, setSelected]               = useState<ProfessionalBooking | null>(null);
  const [selectedGroup, setSelectedGroup]     = useState<BookingSlot | null>(null);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showAbsentModal, setShowAbsentModal] = useState(false);
  const [absentError, setAbsentError]         = useState<string | null>(null);

  const cancelMutation     = useCancelProfessionalBooking();
  const markAbsentMutation = useMarkAbsent();
  const promotionsQuery    = useActivePromotions();

  const promotionBookings: ProfessionalBooking[] = useMemo(() => {
    return (promotionsQuery.data ?? []).map(p => {
      const start = new Date(p.slotStart);
      const date  = p.slotStart.slice(0, 10);
      const hh    = String(start.getHours()).padStart(2, "0");
      const mm    = String(start.getMinutes()).padStart(2, "0");
      const time  = `${hh}:${mm}`;
      const endMs = start.getTime() + p.durationMinutes * 60_000;
      const endD  = new Date(endMs);
      const end   = `${String(endD.getHours()).padStart(2, "0")}:${String(endD.getMinutes()).padStart(2, "0")}`;

      return {
        id: -(p.id),
        status: "PENDING" as ProfessionalBooking["status"],
        cancelled_by: null,
        service_name: p.serviceName,
        client_name: p.clientName,
        client_email: p.clientEmail,
        date,
        time,
        end,
        duration_minutes: p.durationMinutes,
        marked_absent_at: null,
      };
    });
  }, [promotionsQuery.data]);

  const byDate = useMemo(() => {
    const all = [...bookings, ...promotionBookings];
    const map: Record<string, ProfessionalBooking[]> = {};
    for (const b of all) {
      if (!map[b.date]) map[b.date] = [];
      map[b.date].push(b);
    }
    return map;
  }, [bookings, promotionBookings]);

  const weekDates = Array.from({ length: 7 }, (_, i) => addDays(weekStart, i));
  const today     = isoDate(new Date());

  const openSingle = (b: ProfessionalBooking) => {
    setSelectedGroup(null);
    setSelected(b);
    setAbsentError(null);
  };

  return (
      <div className={styles.page}>

        {/* header */}
        <div className={styles.header}>
          <div>
            <h1 className={styles.title}>Mi agenda</h1>
            <p className={styles.subtitle}>{formatWeekRange(weekStart)}</p>
          </div>
          <div className={styles.controls}>
            <button
                className={styles.navBtn}
                onClick={() => setWeekStart(w => addDays(w, -7))}
                aria-label="Semana anterior"
            >
              <i className="ti ti-chevron-left" />
            </button>
            <span className={styles.monthLabel}>{formatMonth(weekStart)}</span>
            <button
                className={styles.navBtn}
                onClick={() => setWeekStart(w => addDays(w, 7))}
                aria-label="Semana siguiente"
            >
              <i className="ti ti-chevron-right" />
            </button>
            <button
                className={styles.todayBtn}
                onClick={() => setWeekStart(getWeekStart(new Date()))}
            >
              Hoy
            </button>
          </div>
        </div>

        {/* calendario */}
        <div className={styles.calendarWrapper}>
          <div className={styles.bodyWrapper}>

            {/* columna de horas */}
            <div className={styles.hoursColumn}>
              <div className={styles.timeGutterHeader} />
              <div className={styles.periodLabel}>Mañana</div>
              {HOURS_MORNING.map(h => (
                  <div key={`tm-${h}`} className={styles.timeCell}>
                    {String(h).padStart(2, "0")}:00
                  </div>
              ))}
              <div className={styles.periodLabel}>Tarde</div>
              {HOURS_AFTERNOON.map(h => (
                  <div key={`ta-${h}`} className={styles.timeCell}>
                    {String(h).padStart(2, "0")}:00
                  </div>
              ))}
            </div>

            {/* columnas de días */}
            <div className={styles.daysWrapper}>
              {weekDates.map((d, colIdx) => {
                const iso         = isoDate(d);
                const isToday     = iso === today;
                const dayBookings = (byDate[iso] ?? []).filter(b => b.status !== "CANCELLED");

                return (
                    <div key={colIdx} className={styles.dayColumn}>

                      <div className={styles.dayHeader}>
                        <span className={styles.dayLabel}>{DAYS[colIdx]}</span>
                        <span className={`${styles.dayNumber} ${isToday ? styles.dayNumberToday : ""}`}>
                          {d.getDate()}
                        </span>
                      </div>

                      <div className={styles.periodSeparator} />
                      {HOURS_MORNING.map(h => <div key={`cm-${h}`} className={styles.cell} />)}
                      <div className={styles.periodSeparator} />
                      {HOURS_AFTERNOON.map(h => <div key={`ca-${h}`} className={styles.cell} />)}

                      {!isLoading && groupAndLayout(dayBookings).map((slot, si) => {
                        const multiCol = slot.totalCols > 1;
                        const colStyle = multiCol ? {
                          left:  `calc(${(slot.col / slot.totalCols) * 100}% + 2px)`,
                          width: `calc(${(1 / slot.totalCols) * 100}% - 4px)`,
                          right: "auto" as const,
                        } : {};

                        const isShortSlot = slot.duration_minutes <= 15;

                        if (slot.bookings.length > 1) {
                          const status = slot.bookings[0].status;
                          return (
                              <button
                                  key={si}
                                  className={`${styles.event} ${styles[`event${status}`]}`}
                                  style={{
                                    top:    `${topPx(slot.time)}px`,
                                    height: `${heightPx(slot.time, slot.duration_minutes)}px`,
                                    ...colStyle,
                                  }}
                                  onClick={() => setSelectedGroup(slot)}
                                  title={`${slot.bookings.length} turnos (${slot.time}→${slot.end})`}
                              >
                                {!isShortSlot && (
                                    <>
                                      <span className={styles.eventClient}>{slot.bookings.length} turnos</span>
                                      <span className={styles.eventService}>{slot.time}→{slot.end}</span>
                                    </>
                                )}
                              </button>
                          );
                        }

                        const b = slot.bookings[0];
                        return (
                            <button
                                key={si}
                                className={`${styles.event} ${styles[`event${b.status}`]}`}
                                style={{
                                  top:    `${topPx(b.time)}px`,
                                  height: `${heightPx(b.time, b.duration_minutes)}px`,
                                  ...colStyle,
                                }}
                                onClick={() => openSingle(b)}
                                title={`${b.client_name} - ${b.service_name} (${b.time}→${b.end})`}
                            >
                              {!isShortSlot && (
                                  <>
                                    <span className={styles.eventClient}>{b.client_name}</span>
                                    <span className={styles.eventService}>{b.service_name}</span>
                                  </>
                              )}
                            </button>
                        );
                      })}
                    </div>
                );
              })}
            </div>

          </div>
        </div>

        {/* drawer grupo */}
        {selectedGroup && (
            <GroupDrawer
                slot={selectedGroup}
                onClose={() => setSelectedGroup(null)}
                onSelectBooking={openSingle}
            />
        )}

        {/* drawer individual */}
        {selected && (
            <BookingDrawer
                booking={selected}
                onClose={() => { setSelected(null); setAbsentError(null); }}
                onCancel={() => setShowCancelModal(true)}
                onMarkAbsent={() => setShowAbsentModal(true)}
                isCancelling={cancelMutation.isPending}
                errorMessage={absentError}
            />
        )}

        {/* modal cancelación */}
        <CancelBookingModal
            isOpen={showCancelModal}
            isCancelling={cancelMutation.isPending}
            onConfirm={() => {
              if (selected) {
                cancelMutation.mutate(selected.id, {
                  onSuccess: () => {
                    setShowCancelModal(false);
                    setSelected(null);
                  },
                });
              }
            }}
            onClose={() => setShowCancelModal(false)}
        />

        {/* modal ausencia */}
        {showAbsentModal && selected && (
            <AbsentConfirmModal
                clientName={selected.client_name}
                isLoading={markAbsentMutation.isPending}
                onConfirm={() => {
                  markAbsentMutation.mutate(selected.id, {
                    onSuccess: () => {
                      setShowAbsentModal(false);
                      setSelected(null);
                      setAbsentError(null);
                    },
                    onError: (error: unknown) => {
                      setShowAbsentModal(false);
                      const message = error instanceof Error ? error.message : String(error);
                      try {
                        const json = JSON.parse(message.replace(/^Failed with status \d+: /, ""));
                        setAbsentError(json.message ?? message);
                      } catch {
                        setAbsentError(message);
                      }
                    },
                  });
                }}
                onClose={() => setShowAbsentModal(false)}
            />
        )}
      </div>
  );
}

// ── drawer de grupo ───────────────────────────────────────────────────────────

function GroupDrawer({
                       slot,
                       onClose,
                       onSelectBooking,
                     }: {
  slot: BookingSlot;
  onClose: () => void;
  onSelectBooking: (b: ProfessionalBooking) => void;
}) {
  return (
      <div className={styles.overlay} onClick={onClose}>
        <div className={styles.drawer} onClick={e => e.stopPropagation()}>
          <button className={styles.drawerClose} onClick={onClose}>
            <i className="ti ti-x" />
          </button>
          <h2 className={styles.drawerTitle}>{slot.bookings.length} turnos</h2>
          <p className={styles.drawerEmail}>{slot.time} → {slot.end}</p>
          <hr className={styles.drawerDivider} />
          <div className={styles.groupList}>
            {slot.bookings.map(b => (
                <button
                    key={b.id}
                    className={styles.groupItem}
                    onClick={() => onSelectBooking(b)}
                >
                  <div className={styles.groupItemName}>{b.client_name}</div>
                  <div className={styles.groupItemService}>{b.service_name}</div>
                  <i className={`ti ti-chevron-right ${styles.groupItemChevron}`} />
                </button>
            ))}
          </div>
        </div>
      </div>
  );
}

// ── drawer individual ─────────────────────────────────────────────────────────

function BookingDrawer({
                         booking,
                         onClose,
                         onCancel,
                         onMarkAbsent,
                         isCancelling,
                         errorMessage,
                       }: {
  booking: ProfessionalBooking;
  onClose: () => void;
  onCancel: () => void;
  onMarkAbsent: () => void;
  isCancelling: boolean;
  errorMessage?: string | null;
}) {
  const dateLabel = new Date(booking.date + "T00:00:00").toLocaleDateString("es-AR", {
    weekday: "long", day: "numeric", month: "long", year: "numeric",
  });

  const alreadyAbsent  = booking.marked_absent_at != null;
  const isPending      = (booking.status as string) === "PENDING";

  return (
      <div className={styles.overlay} onClick={onClose}>
        <div className={styles.drawer} onClick={e => e.stopPropagation()}>
          <button className={styles.drawerClose} onClick={onClose}>
            <i className="ti ti-x" />
          </button>

          <h2 className={styles.drawerTitle}>{booking.client_name}</h2>
          <p className={styles.drawerEmail}>{booking.client_email}</p>

          <hr className={styles.drawerDivider} />

          <div className={styles.drawerRow}>
            <i className="ti ti-cut" />
            <span>{booking.service_name}</span>
          </div>
          <div className={styles.drawerRow}>
            <i className="ti ti-clock" />
            <span>{booking.time} → {booking.end} ({booking.duration_minutes} min)</span>
          </div>
          <div className={styles.drawerRow}>
            <i className="ti ti-calendar" />
            <span>{dateLabel}</span>
          </div>

          <div className={styles.drawerStatus}>
            <span className={`${styles.statusBadge} ${styles[`status${booking.status}`]}`}>
              {isPending         ? "A confirmar"
                  : booking.status === "CONFIRMED"  ? "Confirmado"
                      : booking.status === "COMPLETED"  ? "Completado"
                          : "Cancelado"}
            </span>
            {alreadyAbsent && (
                <span className={styles.absentBadge}>Ausente registrado</span>
            )}
          </div>

          {errorMessage && (
              <div className={styles.drawerError}>
                <i className="ti ti-alert-circle" />
                <span>{errorMessage}</span>
              </div>
          )}

          {/* Sin botones para turnos pendientes de confirmación */}
          {!isPending && booking.status === "CONFIRMED" && (
              <button
                  className={styles.cancelBtn}
                  onClick={onCancel}
                  disabled={isCancelling}
              >
                Cancelar turno
              </button>
          )}

          {!isPending && (booking.status === "CONFIRMED" || booking.status === "COMPLETED") && !alreadyAbsent && (
              <button
                  className={styles.absentBtn}
                  onClick={onMarkAbsent}
              >
                Registrar ausencia
              </button>
          )}
        </div>
      </div>
  );
}

// ── modal ausencia ────────────────────────────────────────────────────────────

function AbsentConfirmModal({
                              clientName,
                              isLoading,
                              onConfirm,
                              onClose,
                            }: {
  clientName: string;
  isLoading: boolean;
  onConfirm: () => void;
  onClose: () => void;
}) {
  return (
      <div className={styles.overlay} onClick={onClose}>
        <div className={styles.modal} onClick={e => e.stopPropagation()}>
          <button className={styles.drawerClose} onClick={onClose}>
            <i className="ti ti-x" />
          </button>
          <div className={styles.iconWrapper}>
            <i className="ti ti-user-off" style={{ fontSize: 28 }} />
          </div>
          <h3 className={styles.title}>Registrar ausencia</h3>
          <p className={styles.description}>
            ¿Confirmar ausencia de <strong>{clientName}</strong>?<br />
            El turno quedará marcado como no asistido.
          </p>
          <div className={styles.actions}>
            <button
                className={styles.confirmAbsentBtn}
                onClick={onConfirm}
                disabled={isLoading}
            >
              {isLoading ? "Registrando..." : "Sí, registrar ausencia"}
            </button>
            <button
                className={styles.cancelAbsentBtn}
                onClick={onClose}
                disabled={isLoading}
            >
              Cancelar
            </button>
          </div>
        </div>
      </div>
  );
}