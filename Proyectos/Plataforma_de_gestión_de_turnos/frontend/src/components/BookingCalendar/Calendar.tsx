import { useState } from "react";

import {
  addMonths,
  DayCell,
  formatMonthYear,
  generateMonthGrid,
  startOfMonth,
  WEEKDAY_LABELS,
} from "./calendarUtils";
import styles from "./BookingCalendar.module.css";

type Props = {
  selectedDate: string | null;
  onSelectDate: (date: string) => void;
};

export const Calendar = ({ selectedDate, onSelectDate }: Props) => {
  const [viewMonth, setViewMonth] = useState<Date>(() => startOfMonth(new Date()));

  const days = generateMonthGrid(viewMonth);

  const goPrevMonth = () => setViewMonth(addMonths(viewMonth, -1));
  const goNextMonth = () => setViewMonth(addMonths(viewMonth, 1));

  return (
    <div className={styles.calendar}>
      <header className={styles.calendarHeader}>
        <button type="button" className={styles.navButton} onClick={goPrevMonth} aria-label="Mes anterior">
          ‹
        </button>
        <span className={styles.monthLabel}>{formatMonthYear(viewMonth)}</span>
        <button type="button" className={styles.navButton} onClick={goNextMonth} aria-label="Mes siguiente">
          ›
        </button>
      </header>

      <div className={styles.weekdays}>
        {WEEKDAY_LABELS.map((label, idx) => (
          <span key={idx} className={styles.weekdayLabel}>
            {label}
          </span>
        ))}
      </div>

      <div className={styles.daysGrid}>
        {days.map((day) => (
          <DayButton
            key={day.iso}
            day={day}
            isSelected={day.iso === selectedDate}
            onClick={() => onSelectDate(day.iso)}
          />
        ))}
      </div>
    </div>
  );
};

type DayButtonProps = {
  day: DayCell;
  isSelected: boolean;
  onClick: () => void;
};

const DayButton = ({ day, isSelected, onClick }: DayButtonProps) => {
  const disabled = day.isPast || !day.isCurrentMonth;

  const className = [
    styles.dayCell,
    !day.isCurrentMonth && styles.dayOtherMonth,
    day.isPast && styles.dayPast,
    isSelected && styles.daySelected,
  ]
    .filter(Boolean)
    .join(" ");

  return (
    <button type="button" className={className} onClick={onClick} disabled={disabled}>
      {day.dayNumber}
    </button>
  );
};
