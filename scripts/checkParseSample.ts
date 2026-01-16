import { parseGame } from '../src/parseGame';

// ...existing code...
// Replace the array below with your real payload (or paste the long JSON array you showed).
const samplePayload = [
  {
    "end_date": null,
    "primary_event_tickers": ["KXNCAAMBGAME-26JAN12IWSFA","KXNCAAMBSPREAD-26JAN12IWSFA","KXNCAAMBTOTAL-26JAN12IWSFA"],
    "details": {
      "home_team_id": "302ef488-6100-4b72-aec2-cc70a8685d8d",
      "league": "NCAAMB",
      "away_team_id": "ebce79fa-f85b-4375-977f-fc23e5766b3d",
      "main_game_event_ticker": "KXNCAAMBGAME-26JAN12IWSFA",
      "status": "complete"
    },
    "last_updated_ts": "0001-01-01T00:00:00Z",
    "id": "b8feaa2c-498a-4482-8478-3b0935d8e529",
    "related_event_tickers": ["KXNCAAMBGAME-26JAN12IWSFA","KXNCAAMBSPREAD-26JAN12IWSFA","KXNCAAMBTOTAL-26JAN12IWSFA"],
    "source_id": "ce412df3-4ce4-4ed0-ab22-63a4139e5b2a",
    "category": "Sports",
    "notification_message": "The game is about to begin.",
    "title": "Incarnate Word at Stephen F. Austin",
    "type": "basketball_game",
    "start_date": "2026-01-13T00:00:00Z"
  },
  // ... add more items or paste the whole chunk from your payload ...
];

function run() {
  for (const raw of samplePayload) {
    const parsed = parseGame(raw);
    // Print a concise summary to verify fields large-scale
    console.log(JSON.stringify({
      id: parsed.id,
      title: parsed.title,
      type: parsed.type,
      startDate: parsed.startDate,
      status: parsed.status,
      league: parsed.league,
      homeTeamId: parsed.homeTeamId,
      awayTeamId: parsed.awayTeamId,
      mainGameTicker: parsed.mainGameTicker,
      primaryEventTickersCount: parsed.primaryEventTickers.length
    }, null, 2));
  }
}

run();

