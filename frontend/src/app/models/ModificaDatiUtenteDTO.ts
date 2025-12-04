export interface ModificaDatiUtenteDTO {
  nome: string | null;
  cognome: string | null;
  data: string | null,
  email: string | null;
  nomeAzienda: string | null,
  sedeAzienda: string | null,
  pIva: string | null,
  idUtente?: string | null
}
