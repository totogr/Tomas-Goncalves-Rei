import { useState } from "react";
import { Service } from "@/models/Service";
import { useServices, useUpdateService, useCreateService, useDeleteService } from "@/services/ServiceServices";
import styles from "./ProfessionalServicesConfig.module.css";

type EditingState = {
    name: string;
    duration_minutes: number;
    price: number;
    max_capacity: number;
    active: boolean;
};

function emptyEditing(): EditingState {
    return { name: "", duration_minutes: 30, price: 0, max_capacity: 1, active: true };
}

function isInvalid(s: EditingState) {
    return s.name.trim() === "" || s.duration_minutes < 5 || s.price < 0 || s.max_capacity < 1;
}

export function ProfessionalServicesConfig() {
    const { data, isLoading } = useServices();
    const services = data?.services ?? [];
    const updateService = useUpdateService();
    const createService = useCreateService();
    const deleteService = useDeleteService();

    const [editingId, setEditingId] = useState<string | "new" | null>(null);
    const [editingData, setEditingData]   = useState<EditingState>(emptyEditing());
    const [deletingId, setDeletingId] = useState<string | null>(null);
    const [deleteError, setDeleteError] = useState<string | null>(null);

    const startEdit = (service: Service) => {
        setEditingId(service.id!);
        setEditingData({
            name: service.name,
            duration_minutes: service.duration_minutes,
            price: service.price,
            max_capacity: service.max_capacity,
            active: service.active,
        });
    };

    const startNew = () => {
        setEditingId("new");
        setEditingData(emptyEditing());
    };

    const cancelEdit = () => {
        setEditingId(null);
    };

    const saveEdit = (service: Service) => {
        updateService.mutate(
            { ...service, ...editingData },
            { onSuccess: () => setEditingId(null) }
        );
    };

    const saveNew = () => {
        createService.mutate(
            { ...editingData, id: undefined } as Service,
            { onSuccess: () => setEditingId(null) }
        );
    };

    const confirmDelete = (id: string) => {
    deleteService.mutate(id, {
        onSuccess: () => {
            setDeletingId(null);
            setDeleteError(null);
        },
        onError: (error: Error) => {
            setDeleteError(error.message);
        },
    });
};

    const update = (field: keyof EditingState, value: string | number | boolean) => {
        setEditingData(prev => ({ ...prev, [field]: value }));
    };

    return (
        <>
            <div>

                {isLoading ? (
                    <p className={styles.empty}>Cargando servicios...</p>
                ) : (
                    <>
                        <div className={styles.tableWrapper}>
                            <div className={styles.tableHeader}>
                                <span className={styles.tableHeaderCell}>Nombre del servicio</span>
                                <span className={styles.tableHeaderCell}>Duración</span>
                                <span className={styles.tableHeaderCell}>Precio</span>
                                <span className={styles.tableHeaderCell}>Capacidad</span>
                                <span className={styles.tableHeaderCell}>Activo</span>
                                <span />
                            </div>

                            {services.map((service) => {
                                const isEditing = editingId === service.id;

                                return (
                                    <div key={service.id} className={styles.tableRow}>
                                        {isEditing ? (
                                            <>
                                                <input
                                                    className={`${styles.input} ${editingData.name.trim() === "" ? styles.error : ""}`}
                                                    value={editingData.name}
                                                    onChange={e => update("name", e.target.value)}
                                                    placeholder="Ej: Corte de cabello"
                                                />
                                                <input
                                                    className={`${styles.input} ${editingData.duration_minutes < 5 ? styles.error : ""}`}
                                                    type="number" min={5} step={5}
                                                    value={editingData.duration_minutes}
                                                    onChange={e => update("duration_minutes", Number(e.target.value))}
                                                />
                                                <div className={styles.inputPrefix}>
                                                    <span>$</span>
                                                    <input
                                                        className={`${styles.input} ${editingData.price < 0 ? styles.error : ""}`}
                                                        type="number" min={0}
                                                        value={editingData.price}
                                                        onChange={e => update("price", Number(e.target.value))}
                                                    />
                                                </div>
                                                <input
                                                    className={`${styles.input} ${editingData.max_capacity < 1 ? styles.error : ""}`}
                                                    type="number" min={1}
                                                    value={editingData.max_capacity}
                                                    onChange={e => update("max_capacity", Number(e.target.value))}
                                                />
                                                <label className={styles.checkboxWrapper}>
                                                    <input
                                                        type="checkbox"
                                                        checked={editingData.active}
                                                        onChange={e => update("active", e.target.checked)}
                                                    />
                                                    {editingData.active ? "Sí" : "No"}
                                                </label>
                                                <div className={styles.rowActions}>
                                                    <button
                                                        className={styles.saveBtn}
                                                        onClick={() => saveEdit(service)}
                                                        disabled={isInvalid(editingData) || updateService.isPending}
                                                    >
                                                        {updateService.isPending ? "..." : "Guardar"}
                                                    </button>
                                                    <button className={styles.cancelEditBtn} onClick={cancelEdit}>
                                                        Cancelar
                                                    </button>
                                                </div>
                                            </>
                                        ) : (
                                            <>
                                                <span className={styles.cellText}>{service.name}</span>
                                                <span className={styles.cellText}>{service.duration_minutes} min</span>
                                                <span className={styles.cellText}>${service.price}</span>
                                                <span className={styles.cellText}>{service.max_capacity}</span>
                                                <span className={styles.cellText}>{service.active ? "Sí" : "No"}</span>
                                                <div className={styles.rowActions}>
                                                    <button
                                                        className={styles.editBtn}
                                                        onClick={() => startEdit(service)}
                                                        disabled={editingId !== null}
                                                    >
                                                        Editar
                                                    </button>
                                                    <button
                                                        className={styles.deleteBtn}
                                                        onClick={() => setDeletingId(service.id!)}
                                                        disabled={editingId !== null}
                                                    >
                                                        <i className="ti ti-trash" />
                                                    </button>
                                                </div>
                                            </>
                                        )}
                                    </div>
                                );
                            })}

                            {/* fila de nuevo servicio */}
                            {editingId === "new" && (
                                <div className={styles.tableRow}>
                                    <input
                                        className={`${styles.input} ${editingData.name.trim() === "" ? styles.error : ""}`}
                                        value={editingData.name}
                                        onChange={e => update("name", e.target.value)}
                                        placeholder="Ej: Corte de cabello"
                                        autoFocus
                                    />
                                    <input
                                        className={`${styles.input} ${editingData.duration_minutes < 5 ? styles.error : ""}`}
                                        type="number" min={5} step={5}
                                        value={editingData.duration_minutes}
                                        onChange={e => update("duration_minutes", Number(e.target.value))}
                                    />
                                    <div className={styles.inputPrefix}>
                                        <span>$</span>
                                        <input
                                            className={`${styles.input} ${editingData.price < 0 ? styles.error : ""}`}
                                            type="number" min={0}
                                            value={editingData.price}
                                            onChange={e => update("price", Number(e.target.value))}
                                        />
                                    </div>
                                    <input
                                        className={`${styles.input} ${editingData.max_capacity < 1 ? styles.error : ""}`}
                                        type="number" min={1}
                                        value={editingData.max_capacity}
                                        onChange={e => update("max_capacity", Number(e.target.value))}
                                    />
                                    <label className={styles.checkboxWrapper}>
                                        <input
                                            type="checkbox"
                                            checked={editingData.active}
                                            onChange={e => update("active", e.target.checked)}
                                        />
                                        {editingData.active ? "Sí" : "No"}
                                    </label>
                                    <div className={styles.rowActions}>
                                        <button
                                            className={styles.saveBtn}
                                            onClick={saveNew}
                                            disabled={isInvalid(editingData) || createService.isPending}
                                        >
                                            {createService.isPending ? "..." : "Guardar"}
                                        </button>
                                        <button className={styles.cancelEditBtn} onClick={cancelEdit}>
                                            Cancelar
                                        </button>
                                    </div>
                                </div>
                            )}
                        </div>

                        {editingId === null && (
                            <button className={styles.addBtn} onClick={startNew}>
                                + Agregar servicio
                            </button>
                        )}
                    </>
                )}
            </div>

            {/* modal de confirmación de borrado */}
            {deletingId !== null && (
                <div className={styles.modalOverlay} onClick={() => { setDeletingId(null); setDeleteError(null); }}>
                    <div className={styles.modal} onClick={e => e.stopPropagation()}>
                        <div className={styles.modalIcon}>
                            <i className="ti ti-trash" />
                        </div>
                        <h3 className={styles.modalTitle}>Eliminar servicio</h3>
                        <p className={styles.modalDesc}>
                            ¿Estás seguro que querés eliminar este servicio? Esta acción no se puede deshacer.
                        </p>
                        {deleteError && (
                            <p className={styles.modalError}>{deleteError}</p>
                        )}
                        <div className={styles.modalActions}>
                            <button
                                className={styles.modalDeleteBtn}
                                onClick={() => confirmDelete(deletingId)}
                                disabled={deleteService.isPending || deleteError !== null}
                            >
                                {deleteService.isPending ? "Eliminando..." : "Sí, eliminar"}
                            </button>
                            <button
                                className={styles.modalCancelBtn}
                                onClick={() => { setDeletingId(null); setDeleteError(null); }}
                                disabled={deleteService.isPending}
                            >
                                Cancelar
                            </button>
                        </div>
                    </div>
                </div>
            )}

        </>
    );
    
}