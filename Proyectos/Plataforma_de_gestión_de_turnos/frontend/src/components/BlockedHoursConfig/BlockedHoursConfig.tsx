import { useEffect, useMemo, useState } from "react";

import { BlockedSchedule } from "@/models/BlockedSchedule";
import { useBlockedSchedules, useCreateBlockedSchedule, useDeleteBlockedSchedule } from "@/services/BlockedScheduleServices";

import styles from "./BlockedHoursConfig.module.css";

type EditingState = {
    blockDate: string;
    startTime: string;
    endTime: string;
};

const emptyEditing = (): EditingState => ({
    blockDate: "",
    startTime: "10:00",
    endTime: "16:00",
});

const isInvalid = (data: EditingState) => {
    if (data.blockDate.trim() === "") return true;
    if (!/^\d{2}:\d{2}$/.test(data.startTime) || !/^\d{2}:\d{2}$/.test(data.endTime)) return true;
    return data.startTime >= data.endTime;
};

const formatBlockDate = (date: string) =>
    new Date(`${date}T00:00:00`).toLocaleDateString("es-AR", {
        weekday: "long",
        day: "2-digit",
        month: "long",
        year: "numeric",
    });

const formatBlockRange = (block: BlockedSchedule) => `${block.startTime} · ${block.endTime}`;

export function BlockedHoursConfig() {
    const { data, isLoading } = useBlockedSchedules();
    const blocks = data ?? [];
    const createBlockedSchedule = useCreateBlockedSchedule();
    const deleteBlockedSchedule = useDeleteBlockedSchedule();

    const [editingData, setEditingData] = useState<EditingState>(emptyEditing());
    const [saved, setSaved] = useState(false);
    const [cancelledCount, setCancelledCount] = useState(0);
    const [showCreateConfirm, setShowCreateConfirm] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);
    const [deleteError, setDeleteError] = useState<string | null>(null);

    useEffect(() => {
        if (!saved) return;
        const timer = window.setTimeout(() => setSaved(false), 3500);
        return () => window.clearTimeout(timer);
    }, [saved]);

    const update = (field: keyof EditingState, value: string) => {
        setEditingData((prev) => ({ ...prev, [field]: value }));
    };

    const createBlock = () => {
        setShowCreateConfirm(true);
    };

    const confirmCreateBlock = () => {
        createBlockedSchedule.mutate(editingData, {
            onSuccess: (result) => {
                setEditingData(emptyEditing());
                setShowCreateConfirm(false);
                setCancelledCount(result.cancelledAppointments);
                setSaved(true);
            },
            onError: () => {
                setShowCreateConfirm(false);
            },
        });
    };

    const confirmDelete = (id: number) => {
        setDeleteError(null);
        deleteBlockedSchedule.mutate(id, {
            onSuccess: () => {
                setDeleteId(null);
                setDeleteError(null);
            },
            onError: (error: Error) => {
                setDeleteError(error.message);
            },
        });
    };

    const invalid = useMemo(() => isInvalid(editingData), [editingData]);

    return (
        <div className={styles.page}>
            <div className={styles.formCard}>
                <div className={styles.formHeader}>
                    <div>
                        <h2 className={styles.sectionTitle}>Bloquear nuevo horario momentaneamente</h2>
                        <p className={styles.sectionSubtitle}>
                            Elegí una fecha y un rango horario para ocultar disponibilidad y evitar nuevas reservas.
                        </p>
                    </div>

                    <button
                        className={styles.primaryBtn}
                        onClick={createBlock}
                        disabled={invalid || createBlockedSchedule.isPending}
                    >
                        {createBlockedSchedule.isPending ? "Guardando..." : "Crear bloqueo"}
                    </button>
                </div>

                <div className={styles.formGrid}>
                    <label className={styles.field}>
                        <span className={styles.fieldLabel}>Fecha</span>
                        <input
                            className={styles.input}
                            type="date"
                            value={editingData.blockDate}
                            onChange={(e) => update("blockDate", e.target.value)}
                        />
                    </label>

                    <label className={styles.field}>
                        <span className={styles.fieldLabel}>Hora de inicio</span>
                        <input
                            className={styles.input}
                            type="time"
                            value={editingData.startTime}
                            onChange={(e) => update("startTime", e.target.value)}
                        />
                    </label>

                    <label className={styles.field}>
                        <span className={styles.fieldLabel}>Hora de fin</span>
                        <input
                            className={styles.input}
                            type="time"
                            value={editingData.endTime}
                            onChange={(e) => update("endTime", e.target.value)}
                        />
                    </label>
                </div>

                {invalid && (
                    <p className={styles.helperError}>
                        Revisá que la fecha esté completa y que la hora de inicio sea anterior a la de fin.
                    </p>
                )}

                {createBlockedSchedule.isError && (
                    <p className={styles.helperError}>
                        {createBlockedSchedule.error instanceof Error
                            ? createBlockedSchedule.error.message
                            : "No se pudo crear el bloqueo."}
                    </p>
                )}
            </div>

            <div className={styles.listCard}>
                <div className={styles.listHeader}>
                    <div>
                        <h3 className={styles.listTitle}>Bloqueos cargados</h3>
                        <p className={styles.listSubtitle}>
                            Estos rangos se excluyen de la disponibilidad y de la reserva de turnos.
                        </p>
                    </div>
                    <span className={styles.countBadge}>{blocks.length} bloqueos</span>
                </div>

                {isLoading ? (
                    <p className={styles.emptyState}>Cargando bloqueos...</p>
                ) : blocks.length === 0 ? (
                    <div className={styles.emptyStateBox}>
                        <i className="ti ti-calendar-off" aria-hidden="true" />
                        <p className={styles.emptyStateTitle}>No hay bloqueos cargados</p>
                        <p className={styles.emptyStateText}>Creá uno arriba para reservarte tiempo en tu agenda.</p>
                    </div>
                ) : (
                    <div className={styles.blocksList}>
                        {blocks.map((block) => (
                            <div key={block.id} className={styles.blockRow}>
                                <div className={styles.blockInfo}>
                                    <span className={styles.blockDate}>{formatBlockDate(block.blockDate)}</span>
                                    <span className={styles.blockRange}>{formatBlockRange(block)}</span>
                                </div>

                                <button
                                    className={styles.deleteBtn}
                                    onClick={() => setDeleteId(block.id ?? null)}
                                    disabled={deleteBlockedSchedule.isPending}
                                >
                                    <i className="ti ti-trash" aria-hidden="true" />
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {deleteId !== null && (
                <div className={styles.modalOverlay} onClick={() => { setDeleteId(null); setDeleteError(null); }}>
                    <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
                        <div className={styles.modalIcon}>
                            <i className="ti ti-calendar-off" aria-hidden="true" />
                        </div>
                        <h3 className={styles.modalTitle}>Eliminar bloqueo</h3>
                        <p className={styles.modalDesc}>
                            Esta acción volverá a mostrar disponibilidad para ese rango horario.
                        </p>
                        {deleteError && <p className={styles.modalError}>{deleteError}</p>}
                        <div className={styles.modalActions}>
                            <button
                                className={styles.modalDeleteBtn}
                                onClick={() => confirmDelete(deleteId)}
                                disabled={deleteBlockedSchedule.isPending}
                            >
                                {deleteBlockedSchedule.isPending ? "Eliminando..." : "Sí, eliminar"}
                            </button>
                            <button
                                className={styles.modalCancelBtn}
                                onClick={() => { setDeleteId(null); setDeleteError(null); }}
                                disabled={deleteBlockedSchedule.isPending}
                            >
                                Cancelar
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showCreateConfirm && (
                <div className={styles.modalOverlay} onClick={() => setShowCreateConfirm(false)}>
                    <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
                        <div className={styles.modalIcon}>
                            <i className="ti ti-alert-triangle" aria-hidden="true" />
                        </div>
                        <h3 className={styles.modalTitle}>Confirmar bloqueo de horario</h3>
                        <p className={styles.modalDesc}>
                            Al bloquear este horario, se cancelarán los turnos que ya hayan sido solicitados dentro de este rango. ¿Estás seguro que deseás continuar?
                        </p>
                        <div className={styles.modalActions}>
                            <button
                                className={styles.modalDeleteBtn}
                                onClick={confirmCreateBlock}
                                disabled={createBlockedSchedule.isPending}
                            >
                                {createBlockedSchedule.isPending ? "Guardando..." : "Sí, bloquear horario"}
                            </button>
                            <button
                                className={styles.modalCancelBtn}
                                onClick={() => setShowCreateConfirm(false)}
                                disabled={createBlockedSchedule.isPending}
                            >
                                Cancelar
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {saved && (
                <div className={styles.toast}>
                    <span className={styles.toastTitle}>Bloqueo creado</span>
                    <span className={styles.toastSub}>
                        {cancelledCount > 0
                            ? `Se cancelaron ${cancelledCount} turno${cancelledCount === 1 ? "" : "s"} en ese rango.`
                            : "No había turnos para cancelar en ese rango."}
                    </span>
                </div>
            )}
        </div>
    );
}