import { useEffect, useMemo, useState } from "react";

import { ClientInfo } from "@/models/BlockedClient";
import { useBlockedClientIds, useBlockClient, useUnblockClient, useAllClients } from "@/services/BlockedClientServices";
import { useToken } from "@/services/TokenContext";

import styles from "./BlockedClientsConfig.module.css";

export function BlockedClientsConfig() {
  const [tokenState] = useToken();
  const professionalId =
    tokenState.state === "LOGGED_IN" ? tokenState.tokens.id : 0;

  const { data: allClientsData, isLoading: allClientsLoading } = useAllClients();
  const { data: blockedIds, isLoading: blockedLoading } = useBlockedClientIds(professionalId);

  const blockClient = useBlockClient(professionalId);
  const unblockClient = useUnblockClient(professionalId);

  const [blockedSearchTerm, setBlockedSearchTerm] = useState("");
  const [notBlockedSearchTerm, setNotBlockedSearchTerm] = useState("");
  const [confirmBlock, setConfirmBlock] = useState<ClientInfo | null>(null);
  const [confirmUnblock, setConfirmUnblock] = useState<ClientInfo | null>(null);
  const [blockError, setBlockError] = useState<string | null>(null);
  const [unblockError, setUnblockError] = useState<string | null>(null);
  const [toast, setToast] = useState<string | null>(null);

  useEffect(() => {
    if (!toast) return;
    const timer = window.setTimeout(() => setToast(null), 3500);
    return () => window.clearTimeout(timer);
  }, [toast]);

  const allClients = useMemo(() => {
    if (!allClientsData) return [];
    return allClientsData;
  }, [allClientsData]);

  const blockedSet = useMemo(() => new Set(blockedIds ?? []), [blockedIds]);

  const notBlockedClients = useMemo(
    () => allClients.filter((c) => !blockedSet.has(c.clientId)),
    [allClients, blockedSet],
  );

  const blockedClients = useMemo(
    () => allClients.filter((c) => blockedSet.has(c.clientId)),
    [allClients, blockedSet],
  );

  const filteredNotBlockedClients = useMemo(() => {
    const term = notBlockedSearchTerm.toLowerCase().trim();
    if (!term) return notBlockedClients;
    return notBlockedClients.filter((c) =>
      c.clientName.toLowerCase().includes(term),
    );
  }, [notBlockedClients, notBlockedSearchTerm]);

  const filteredBlockedClients = useMemo(() => {
    const term = blockedSearchTerm.toLowerCase().trim();
    if (!term) return blockedClients;
    return blockedClients.filter((c) =>
      c.clientName.toLowerCase().includes(term),
    );
  }, [blockedClients, blockedSearchTerm]);

  const handleBlock = (client: ClientInfo) => {
    setBlockError(null);
    setConfirmBlock(client);
  };

  const confirmBlockAction = () => {
    if (!confirmBlock) return;
    blockClient.mutate(confirmBlock.clientId, {
      onSuccess: () => {
        setConfirmBlock(null);
        setBlockError(null);
        setToast(`Cliente ${confirmBlock.clientName} bloqueado`);
      },
      onError: (error: Error) => {
        setBlockError(error.message);
      },
    });
  };

  const handleUnblock = (client: ClientInfo) => {
    setUnblockError(null);
    setConfirmUnblock(client);
  };

  const confirmUnblockAction = () => {
    if (!confirmUnblock) return;
    unblockClient.mutate(confirmUnblock.clientId, {
      onSuccess: () => {
        setConfirmUnblock(null);
        setUnblockError(null);
        setToast(`Cliente ${confirmUnblock.clientName} desbloqueado`);
      },
      onError: (error: Error) => {
        setUnblockError(error.message);
      },
    });
  };

  const isLoading = allClientsLoading || blockedLoading;

  return (
    <div className={styles.page}>
      {/* ── Clientes bloqueados ── */}
      <div className={styles.listCard}>
        <div className={styles.listHeader}>
          <div>
            <h2 className={styles.listTitle}>Clientes bloqueados</h2>
            <p className={styles.listSubtitle}>
              Estos clientes no pueden ver tu perfil ni pedir turnos con vos.
            </p>
          </div>
          <span className={styles.countBadge}>
            {filteredBlockedClients.length} bloqueado{filteredBlockedClients.length !== 1 ? "s" : ""}
          </span>
        </div>

        <div className={styles.searchWrapper}>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="11" cy="11" r="8" />
            <path d="m21 21-4.35-4.35" />
          </svg>
          <input
            type="text"
            placeholder="Buscar bloqueado por nombre o apellido..."
            value={blockedSearchTerm}
            onChange={(e) => setBlockedSearchTerm(e.target.value)}
            className={styles.searchInput}
          />
        </div>

        {isLoading ? (
          <p className={styles.emptyState}>Cargando...</p>
        ) : filteredBlockedClients.length === 0 ? (
          <div className={styles.emptyStateBox}>
            <i className="ti ti-user-off" aria-hidden="true" />
            <p className={styles.emptyStateTitle}>
              {blockedSearchTerm.trim() ? "No se encontraron clientes bloqueados" : "No hay clientes bloqueados"}
            </p>
            <p className={styles.emptyStateText}>
              {blockedSearchTerm.trim()
                ? "Probá con otro nombre o apellido."
                : "Bloqueá clientes desde la sección de abajo para que no puedan verte."}
            </p>
          </div>
        ) : (
          <div className={styles.clientsList}>
            {filteredBlockedClients.map((client) => (
              <div key={client.clientId} className={styles.clientRow}>
                <div className={styles.clientInfo}>
                  <span className={styles.clientName}>{client.clientName}</span>
                </div>
                <button
                  className={`${styles.actionBtn} ${styles.unblockBtn}`}
                  onClick={() => handleUnblock(client)}
                  disabled={unblockClient.isPending}
                >
                  {unblockClient.isPending ? "..." : "Desbloquear"}
                </button>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className={styles.sectionGap} />

      {/* ── Todos los clientes ── */}
      <div className={styles.listCard}>
        <div className={styles.listHeader}>
          <div>
            <h2 className={styles.listTitle}>Todos los clientes</h2>
            <p className={styles.listSubtitle}>
              Bloqueá clientes para que dejen de ver tu perfil y no puedan agendar turnos.
            </p>
          </div>
          <span className={styles.countBadge}>
            {filteredNotBlockedClients.length} cliente{filteredNotBlockedClients.length !== 1 ? "s" : ""}
          </span>
        </div>

        <div className={styles.searchWrapper}>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="11" cy="11" r="8" />
            <path d="m21 21-4.35-4.35" />
          </svg>
          <input
            type="text"
            placeholder="Buscar cliente por nombre o apellido..."
            value={notBlockedSearchTerm}
            onChange={(e) => setNotBlockedSearchTerm(e.target.value)}
            className={styles.searchInput}
          />
        </div>

        {isLoading ? (
          <p className={styles.emptyState}>Cargando...</p>
        ) : filteredNotBlockedClients.length === 0 ? (
          <div className={styles.emptyStateBox}>
            <i className="ti ti-users" aria-hidden="true" />
            <p className={styles.emptyStateTitle}>
              {notBlockedSearchTerm.trim()
                ? "No se encontraron clientes"
                : notBlockedClients.length === 0
                  ? "No hay más clientes para bloquear"
                  : "No se encontraron clientes"}
            </p>
            <p className={styles.emptyStateText}>
              {notBlockedSearchTerm.trim()
                ? "Probá con otro nombre o apellido."
                : notBlockedClients.length === 0
                  ? "Todos los clientes que podian ser bloqueados ya fueron bloqueados."
                  : "Bloqueá clientes para que dejen de ver tu perfil y no puedan agendar turnos."}
            </p>
          </div>
        ) : (
          <div className={styles.clientsList}>
            {filteredNotBlockedClients.map((client) => (
              <div key={client.clientId} className={styles.clientRow}>
                <div className={styles.clientInfo}>
                  <span className={styles.clientName}>{client.clientName}</span>
                </div>
                <button
                  className={`${styles.actionBtn} ${styles.blockBtn}`}
                  onClick={() => handleBlock(client)}
                  disabled={blockClient.isPending}
                >
                  {blockClient.isPending ? "..." : "Bloquear"}
                </button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* ── Modal confirmar bloqueo ── */}
      {confirmBlock && (
        <div className={styles.modalOverlay} onClick={() => { setConfirmBlock(null); setBlockError(null); }}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={`${styles.modalIcon} ${styles.modalIconDanger}`}>
              <i className="ti ti-user-off" aria-hidden="true" />
            </div>
            <h3 className={styles.modalTitle}>Bloquear cliente</h3>
            <p className={styles.modalDesc}>
              ¿Estás seguro que querés bloquear a{" "}
              <strong>{confirmBlock.clientName}</strong>?
            </p>
            <p className={styles.modalWarning}>
              <i className="ti ti-alert-triangle" aria-hidden="true" />{" "}
              Si bloqueás a este cliente, todos los turnos que tengas con él serán cancelados.
            </p>
            <p className={styles.modalDesc}>
              Dejará de ver tu perfil y no podrá agendar turnos con vos.
            </p>
            {blockError && <p className={styles.modalError}>{blockError}</p>}
            <div className={styles.modalActions}>
              <button
                className={styles.modalPrimaryBtn}
                onClick={confirmBlockAction}
                disabled={blockClient.isPending}
              >
                {blockClient.isPending ? "Bloqueando..." : "Sí, bloquear"}
              </button>
              <button
                className={styles.modalCancelBtn}
                onClick={() => { setConfirmBlock(null); setBlockError(null); }}
                disabled={blockClient.isPending}
              >
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ── Modal confirmar desbloqueo ── */}
      {confirmUnblock && (
        <div className={styles.modalOverlay} onClick={() => { setConfirmUnblock(null); setUnblockError(null); }}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalIcon}>
              <i className="ti ti-user-check" aria-hidden="true" />
            </div>
            <h3 className={styles.modalTitle}>Desbloquear cliente</h3>
            <p className={styles.modalDesc}>
              ¿Querés desbloquear a{" "}
              <strong>{confirmUnblock.clientName}</strong>?
              Volverá a ver tu perfil y podrá agendar turnos nuevamente.
            </p>
            {unblockError && <p className={styles.modalError}>{unblockError}</p>}
            <div className={styles.modalActions}>
              <button
                className={styles.modalPrimaryBtn}
                style={{ background: "#1a3320" }}
                onClick={confirmUnblockAction}
                disabled={unblockClient.isPending}
              >
                {unblockClient.isPending ? "Desbloqueando..." : "Sí, desbloquear"}
              </button>
              <button
                className={styles.modalCancelBtn}
                onClick={() => { setConfirmUnblock(null); setUnblockError(null); }}
                disabled={unblockClient.isPending}
              >
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ── Toast ── */}
      {toast && (
        <div className={styles.toast}>
          <span className={styles.toastTitle}>{toast}</span>
        </div>
      )}
    </div>
  );
}