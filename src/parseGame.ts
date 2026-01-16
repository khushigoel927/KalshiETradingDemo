export type RawGame = any;

export type ParsedGame = {
  id: string | null;
  title: string | null;
  type: string | null;
  category: string | null;
  sourceId: string | null;
  notificationMessage: string | null;
  startDate: string | null; // ISO string or null
  endDate: string | null;
  status: string | null; // prefer details.status
  league: string | null;
  homeTeamId: string | null;
  awayTeamId: string | null;
  mainGameTicker: string | null;
  primaryEventTickers: string[];
  relatedEventTickers: string[];
  raw: any;
};

function toStringOrNull(v: any): string | null {
  if (v === undefined || v === null) return null;
  return String(v);
}

function toStringArray(v: any): string[] {
  if (!Array.isArray(v)) return [];
  return v.map((x) => String(x));
}

export function parseGame(raw: RawGame): ParsedGame {
  // ...existing code...
  // Defensive extraction
  const id = toStringOrNull(raw.id ?? raw.event_id ?? null);
  const title = toStringOrNull(raw.title ?? raw.name ?? null);
  const type = toStringOrNull(raw.type ?? null);
  const category = toStringOrNull(raw.category ?? null);
  const sourceId = toStringOrNull(raw.source_id ?? raw.sourceId ?? null);
  const notificationMessage = toStringOrNull(raw.notification_message ?? raw.notificationMessage ?? null);

  const startDateRaw = raw.start_date ?? raw.startDate ?? null;
  let startDate: string | null = null;
  if (startDateRaw) {
    // try to parse into a normalized ISO string
    const d = new Date(startDateRaw);
    if (!isNaN(d.getTime())) startDate = d.toISOString();
    else startDate = toStringOrNull(startDateRaw);
  }

  const endDateRaw = raw.end_date ?? raw.endDate ?? null;
  let endDate: string | null = null;
  if (endDateRaw) {
    const d = new Date(endDateRaw);
    if (!isNaN(d.getTime())) endDate = d.toISOString();
    else endDate = toStringOrNull(endDateRaw);
  }

  const details = (raw.details && typeof raw.details === 'object') ? raw.details : {};
  const status = toStringOrNull(details.status ?? raw.status ?? null);
  const league = toStringOrNull(details.league ?? null);
  const homeTeamId = toStringOrNull(details.home_team_id ?? details.homeTeamId ?? null);
  const awayTeamId = toStringOrNull(details.away_team_id ?? details.awayTeamId ?? null);
  const mainGameTicker = toStringOrNull(details.main_game_event_ticker ?? null);

  const primaryEventTickers = toStringArray(raw.primary_event_tickers ?? raw.primaryEventTickers ?? []);
  const relatedEventTickers = toStringArray(raw.related_event_tickers ?? raw.relatedEventTickers ?? []);

  return {
    id,
    title,
    type,
    category,
    sourceId,
    notificationMessage,
    startDate,
    endDate,
    status,
    league,
    homeTeamId,
    awayTeamId,
    mainGameTicker,
    primaryEventTickers,
    relatedEventTickers,
    raw, // include original for debugging
  };
}

