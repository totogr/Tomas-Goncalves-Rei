import { useState, useEffect } from "react";
import {
    WorkingHours,
    WorkingHoursDay,
    TimeRange,
    DAY_LABELS,
} from "@/models/WorkingHours";

import styles from "./WorkingHoursConfig.module.css";

type Props = {
    initialWorkingHours: WorkingHours;
    onSave: (workingHours: WorkingHours) => void;
    isSaving: boolean;
    saved: boolean;
};

const isRangeInvalid = (range: TimeRange) =>
    range.from >= range.to;

const rangesOverlap = (a: TimeRange, b: TimeRange) =>
    a.from < b.to && b.from < a.to;

const hasDayOverlap = (day: WorkingHoursDay) =>
    day.ranges.some((range, i) =>
        day.ranges.some((other, j) => i !== j && rangesOverlap(range, other))
    );

export function WorkingHoursConfig({
    initialWorkingHours,
    onSave,
    isSaving,
    saved,
}: Props) {
    const [workingHours, setWorkingHours] =
        useState<WorkingHours>(initialWorkingHours);

    const [showToast, setShowToast] =
        useState(false);

    const [expandedDay, setExpandedDay] =
        useState<number | null>(null);

    const hasInvalidRanges = workingHours.schedule.some(
        (day) =>
            day.enabled &&
            (day.ranges.some(isRangeInvalid) || hasDayOverlap(day))
    );

    useEffect(() => {
        if (saved) {
            setShowToast(true);
            const t = setTimeout(() => setShowToast(false), 4000);
            return () => clearTimeout(t);
        }
    }, [saved]);

    const updateDay = (index: number, updated: WorkingHoursDay) => {
        setWorkingHours((prev) => {
            const next = [...prev.schedule];
            next[index] = updated;
            return { ...prev, schedule: next };
        });
    };

    const toggleDay = (index: number) => {
        const day = workingHours.schedule[index];
        updateDay(index, {
            ...day,
            enabled: !day.enabled,
            ranges:
                !day.enabled && day.ranges.length === 0
                    ? [{ from: "09:00", to: "18:00" }]
                    : day.ranges,
        });
    };

    const addRange = (index: number) => {
        const day = workingHours.schedule[index];
        updateDay(index, {
            ...day,
            ranges: [...day.ranges, { from: "09:00", to: "18:00" }],
        });
    };

    const removeRange = (dayIndex: number, rangeIndex: number) => {
        const day = workingHours.schedule[dayIndex];
        updateDay(dayIndex, {
            ...day,
            ranges: day.ranges.filter((_, i) => i !== rangeIndex),
        });
    };

    const updateRange = (
        dayIndex: number,
        rangeIndex: number,
        field: keyof TimeRange,
        value: string
    ) => {
        const day = workingHours.schedule[dayIndex];
        const ranges = [...day.ranges];
        ranges[rangeIndex] = { ...ranges[rangeIndex], [field]: value };
        updateDay(dayIndex, { ...day, ranges });
    };

    const formatSummary = (day: WorkingHoursDay) =>
        day.ranges.map((r) => `${r.from}–${r.to}`).join(" · ");

    const INTERVAL_OPTIONS = [
        { value: 15, label: "15 min" },
        { value: 30, label: "30 min" },
        { value: 45, label: "45 min" },
        { value: 60, label: "60 min" },
    ];

    return (
        <div className={styles.page}>

            <div className={styles.intervalRow}>
                <div>
                    <div className={styles.intervalLabel}>Intervalo de turnos</div>
                    <div className={styles.intervalHint}>Cada cuánto tiempo aparecen los horarios disponibles</div>
                </div>
                <select
                    className={styles.intervalSelect}
                    value={workingHours.slotIntervalMinutes}
                    onChange={(e) =>
                        setWorkingHours((prev) => ({ ...prev, slotIntervalMinutes: Number(e.target.value) }))
                    }
                >
                    {INTERVAL_OPTIONS.map((opt) => (
                        <option key={opt.value} value={opt.value}>{opt.label}</option>
                    ))}
                </select>
            </div>

            <div className={styles.daysGrid}>
                {workingHours.schedule.map((day, dayIndex) => {
                    const expanded = expandedDay === dayIndex;
                    const overlaps = hasDayOverlap(day);

                    return (
                        <div
                            key={day.day}
                            className={styles.dayCard}
                            onClick={() =>
                                setExpandedDay(expanded ? null : dayIndex)
                            }
                        >
                            <div className={styles.dayHeader}>
                                <div className={styles.dayLeft}>
                                    <label
                                        className={styles.toggle}
                                        onClick={(e) => e.stopPropagation()}
                                    >
                                        <input
                                            type="checkbox"
                                            checked={day.enabled}
                                            onChange={() => toggleDay(dayIndex)}
                                        />
                                        <span className={styles.toggleTrack} />
                                        <span className={styles.toggleThumb} />
                                    </label>

                                    <div>
                                        <div className={styles.dayName}>
                                            {DAY_LABELS[day.day]}
                                        </div>
                                        <div className={styles.daySummary}>
                                            {day.enabled
                                                ? formatSummary(day)
                                                : "Cerrado"}
                                        </div>
                                    </div>
                                </div>

                                <span className={styles.expandIcon}>
                                    {expanded ? "−" : "+"}
                                </span>
                            </div>

                            {expanded && day.enabled && (
                                <div
                                    className={styles.ranges}
                                    onClick={(e) => e.stopPropagation()}
                                >
                                    {day.ranges.map((range, rangeIndex) => {
                                        const invalid = isRangeInvalid(range);
                                        const hasError = invalid || overlaps;

                                        return (
                                            <div
                                                key={rangeIndex}
                                                className={styles.rangeRow}
                                            >
                                                <input
                                                    type="time"
                                                    className={`${styles.timeInput} ${hasError ? styles.timeInputError : ""}`}
                                                    value={range.from}
                                                    onChange={(e) =>
                                                        updateRange(dayIndex, rangeIndex, "from", e.target.value)
                                                    }
                                                />
                                                <span>—</span>
                                                <input
                                                    type="time"
                                                    className={`${styles.timeInput} ${hasError ? styles.timeInputError : ""}`}
                                                    value={range.to}
                                                    onChange={(e) =>
                                                        updateRange(dayIndex, rangeIndex, "to", e.target.value)
                                                    }
                                                />
                                                {day.ranges.length > 1 && (
                                                    <button
                                                        className={styles.removeBtn}
                                                        onClick={() =>
                                                            removeRange(dayIndex, rangeIndex)
                                                        }
                                                    >
                                                        ×
                                                    </button>
                                                )}
                                            </div>
                                        );
                                    })}

                                    {overlaps && (
                                        <p className={styles.overlapError}>
                                            Los rangos horarios se superponen. Revisá que no se pisen entre sí.
                                        </p>
                                    )}

                                    {day.ranges.some(isRangeInvalid) && !overlaps && (
                                        <p className={styles.overlapError}>
                                            El horario de inicio debe ser anterior al de fin.
                                        </p>
                                    )}

                                    <button
                                        className={styles.addRangeBtn}
                                        onClick={() => addRange(dayIndex)}
                                    >
                                        + Agregar rango
                                    </button>
                                </div>
                            )}
                        </div>
                    );
                })}
            </div>

            <div className={styles.footer}>
                {hasInvalidRanges && (
                    <span className={styles.footerError}>
                        Corregí los horarios antes de guardar
                    </span>
                )}
                <button
                    className={styles.saveBtn}
                    onClick={() => onSave(workingHours)}
                    disabled={isSaving || hasInvalidRanges}
                >
                    {isSaving ? "Guardando..." : "Guardar horarios"}
                </button>
            </div>

            {showToast && (
                <div className={styles.toast}>
                    <span className={styles.toastTitle}>Horarios guardados</span>
                    <span className={styles.toastSub}>
                        Los cambios solo aplican a nuevas reservas
                    </span>
                </div>
            )}
        </div>
    );
}