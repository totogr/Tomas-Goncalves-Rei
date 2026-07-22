import styles from "./Slotbutton.module.css";

type Props = {
    time: string;
    isSelected: boolean;
    onSelect: () => void;
};

export const SlotButton = ({ time, isSelected, onSelect }: Props) => {
    return (
        <button
            type="button"
            className={[styles.slot, isSelected ? styles.selected : ""].filter(Boolean).join(" ")}
            onClick={onSelect}
            title={time}
        >
            <span className={styles.time}>{time}</span>
        </button>
    );
};